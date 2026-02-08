# -*- coding: utf-8 -*-

#$$strtCprt
#
# Simple Algebra 
# 
# Copyright (C) 2014 Thornton Green
# 
# This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
# published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
# This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
# of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
# You should have received a copy of the GNU General Public License along with this program; if not, 
# see <http://www.gnu.org/licenses>.
# Additional permission under GNU GPL version 3 section 7
#
#
#$$endCprt


"""

AI Common Routines

Ollama AI needs to have the gpt-oss:20b model loaded

Algorithm influenced generally by Agentic AI patterns

Uses some example code from:   https://markaicode.com/process-images-ollama-multimodal-ai/

Also uses some AI-written example code

"""


import requests
import json
import base64
import re


import sys
from typing import Optional, Tuple

import sympy
from sympy.parsing.sympy_parser import parse_expr, standard_transformations, implicit_multiplication_application




"""
Sends a text prompt to Ollama with gpt-oss, and returns the text response from Ollama


Args:

prompt -- The input prompt

Return:

The string returned by the Ollama AI
"""
def send_prompt_gpt_oss(prompt="Describe this image in detail"):
    """Send prompt to Ollama for analysis"""
    
    # Prepare the request
    payload = {
        "model": "gpt-oss:20b",
        "prompt": prompt,
        "stream": False
    }
    
    # Send request to Ollama
    response = requests.post(
        "http://localhost:11434/api/generate",
        json=payload,
        headers={"Content-Type": "application/json"}
    )
    
    if response.status_code == 200:
        result = response.json()
        return result['response']
    else:
        return f"Error: {response.status_code}"




"""
Constant used to parse whether a response contains a set of digits
"""
_digits = re.compile('\d')








"""
Given a raw string that contains an LLM explanation and the final result,
    return the string that represents the mathematical expression.

Implements heuristics for searching for expression string in the middle of LLM-generated text.

Args:

text -- The LLM-generated text to be parsed.

Return:

Mathematical expression string.
"""
def extract_expression(text: str) -> Optional[str]:
    """
    Given a raw string that contains an LLM explanation and the final result,
    return the string that represents the mathematical expression.
    """
    # Strip leading/trailing whitespace
    lines = [ln.strip() for ln in text.strip().splitlines() if ln.strip()]

    for ln in reversed(lines) : 

        if len( ln ) > 0 :

            if not ln.startswith( "#" ) : 

                if "symbols(" in ln : 
                    return None

                if ( " import " in ln ) and ( "sympy" in ln ) : 
                    return None

                # Look for an equals sign on a line: "... = ..."
                if '=' in ln :
                    # Grab everything after the last '='
                    expr = ln.split('=')[-1].strip()
                    if expr:
                        try:
                            # We parse with evaluate=False to keep the structure
                            _ = parse_expr(expr, evaluate=False,
                                transformations=standard_transformations + (implicit_multiplication_application,))
                            return expr
                        except Exception:
                            continue
                else : 
                    try:
                        # We parse with evaluate=False to keep the structure
                        _ = parse_expr(ln, evaluate=False,
                           transformations=standard_transformations + (implicit_multiplication_application,))
                        return ln
                    except Exception:
                        continue

    # Nothing found
    return None





"""
Parse the expression string into a SymPy expression (parse tree).


Args:

expr_str -- The expression string to be parsed.

Return:

The SymPy expression object.
"""
def parse_to_ast(expr_str: str):
    """
    Parse the expression string into a SymPy expression (parse tree).
    Returns the SymPy expression object.
    Raises sympy.SympifyError if parsing fails.
    """
    transformations = standard_transformations + (implicit_multiplication_application,)
    return parse_expr(expr_str, evaluate=False, transformations=transformations)




"""
Pretty-prints the SymPy parse tre.  For debug only.


Args:

node -- The current SymPy node
f -- The file to which to write logging (if file logging is turned on)
indent -- The amount of indentation to use in the output

Return:

None.
"""
def pretty_print_tree(node, f, indent: int = 0):
    """
    Recursively prints the SymPy expression tree in a human‑readable format.
    """
    prefix = "  " * indent
    if isinstance(node, sympy.Basic):
        writeDebug(f"{prefix}{node.__class__.__name__}: {node}",f)
        for arg in node.args:
            pretty_print_tree(arg, f, indent + 1)
    else:
        writeDebug(f"{prefix}{node}")





"""
The current "temporary variable" number for TAC-like output
"""
tac_num = 0



"""
Resets the current "temporary variable" number for TAC-like output
"""
def resetTacNum() : 

     global tac_num
     tac_num = 0



"""
Allocates a current "temporary variable" number for TAC-like output

Return:

The allocated "temporary variable" string.
"""
def allocateTacVar() : 
    
    global tac_num
    tn = "t_" + str( tac_num )
    tac_num = tac_num + 1
    return tn




"""
Prints the SymPy parse tree as TAC-like text


Args:

node -- The current SymPy node
f -- The file to which to write logging (if file logging is turned on)
indent -- The amount of indentation to use in the output

Return:

The current temporary number.  Used for determining the final temporary.
"""
def tac_print_tree(node, f, indent: int = 0):
    """
    Recursively prints the SymPy expression tree in a human‑readable format.
    """
    prefix = "  " * indent
    tn = ""
    if isinstance(node, sympy.Basic):
        isDivideOne = False
        isInvert = False
        divideOneArg = None
        if node.__class__.__name__ == "Pow" : 
            if len( node.args ) == 2 : 
                if ( node.args[1] ).__class__.__name__ == "NegativeOne" : 
                    divideOneArg = node.args[0]
                    if ( node.args[0] ).__class__.__name__ == "Integer" : 
                        isDivideOne = True
                    else : 
                        isInvert = True
        if ( not isDivideOne ) and ( not isInvert ) : 
            alist = ""
            if len( node.args ) > 0 : 
                for arg in node.args:
                    tt = tac_print_tree(arg, f, indent + 1)
                    alist = alist + " , " + tt
            else : 
                alist = " <<>> " + str( node )
            tn = allocateTacVar()
            writeTac(f"{prefix}{tn} = {node.__class__.__name__} {alist}" , f )
        else : 
            if isDivideOne : 
                tn = allocateTacVar()
                writeTac(f"{prefix}{tn} = divideOne {divideOneArg}" , f )
            else : 
                # isInvert is True
                tt = tac_print_tree(divideOneArg, f, indent + 1)
                alist = " , " + tt
                tn = allocateTacVar()
                writeTac(f"{prefix}{tn} = invert {alist}" , f )
    else:
        tn = allocateTacVar()
        writeTac(f"{prefix}{tn} = {node.__class__.__name__} {node}" , f )
    return tn





"""
Whether to write debug text to the console
"""
writeDebugConsole = True


"""
Whether to write debug text to the log file (if logging is turned on)
"""
writeDebugLogFile = True




"""
Writes a line of debug text


Args:

istr -- The line of text to be written
f -- The file to which to write logging (if file logging is turned on)

Return:

None
"""
def writeDebug(  istr , f ) : 

    global writeDebugConsole
    global writeDebugLogFile

    if writeDebugConsole :

        print( istr )

    if ( writeDebugLogFile ) and ( f is not None )  :

        f.write( str( istr ) )
        f.write( "\n" )




"""
Sets output modes for debug text


Args:

inConsole -- Whether to write to the console
inLogFile -- Whether to write to the log file (if logging is turned on)

Return:

None
"""
def setDebugModes( inConsole , inLogFile ) : 

    global writeDebugConsole
    global writeDebugLogFile

    writeDebugConsole = inConsole
    writeDebugLogFile = inLogFile




"""
Whether to write TAC-like text to the console
"""
writeTacConsole = True


"""
Whether to write TAC-like text to the log file (if logging is turned on)
"""
writeTacLogFile = True



"""
Writes a line of TAC-like text


Args:

istr -- The line of text to be written
f -- The file to which to write logging (if file logging is turned on)

Return:

None
"""
def writeTac( istr , f ) : 

    global writeTacConsole
    global writeTacLogFile

    if writeTacConsole :

        print( istr )

    if ( writeTacLogFile ) and ( f is not None )  :

        f.write( str( istr ) )
        f.write( "\n" )


"""
Sets output modes for TAC-like text


Args:

inConsole -- Whether to write to the console
inLogFile -- Whether to write to the log file (if logging is turned on)

Return:

None
"""
def setTacModes( inConsole , inLogFile ) : 

    global writeTacConsole
    global writeTacLogFile

    writeTacConsole = inConsole
    writeTacLogFile = inLogFile


    



