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

AI Simple Derivative Test

Ollama AI needs to have the gpt-oss:20b model loaded

Algorithm influenced generally by Agentic AI patterns

Uses some example code from:   https://markaicode.com/process-images-ollama-multimodal-ai/

"""


import argparse
import requests
import json
import base64
import re


import sys
from typing import Optional, Tuple

import sympy
from sympy.parsing.sympy_parser import parse_expr, standard_transformations, implicit_multiplication_application


import AiCommonRoutines

import AiCommonVerification

import AiCommonExecution








"""
Requests for Ollama AI to determine and verify the derivative


Args:

expressionStr -- The expression for which to calculate the derivative
wrt -- String containing the variable for which to calculate the derivative.
f -- The file to which to write logging (if file logging is turned on)

Return:

The result string, or None if the result can't be verified.
"""
def verifyDerivative( expressionStr , wrt , f ) : 

    retryCount = 0

    while retryCount < 10 :

        promptStrA = "Format the answer as a SymPy expression.  Calculus First Derivative of ( " + expressionStr + " ) with respect to " + wrt

        resultStr = AiCommonExecution.fullExecution( promptStrA , f )

        AiCommonRoutines.writeDebug( "verify resultStr" , f )
        AiCommonRoutines.writeDebug( resultStr , f )

        if resultStr is not None : 
            if ( "Derivative(" in resultStr ) or ( "Integral(" in resultStr ) or ( "diff(" in resultStr )  or ( "integrate(" in resultStr ) : 
                AiCommonRoutines.writeDebug( "Dropped due to deriv/int term" , f )
                resultStr = null

        if resultStr is not None : 

            promptStrA = "Answer Yes/No, does the calculus first derivative of ( " + expressionStr + " ) with respect to " + wrt + " yield the answer ( " + resultStr + " ) ? "

            result = AiCommonVerification.fullVerification( promptStrA , f )

            if result : 
                return resultStr

        retryCount = retryCount + 1
    

    return None






"""
Runs the overall AI derivative algorithm


Args:

args -- The input command-line arguments.
f -- The file to which to write logging (if file logging is turned on)

Return:

None.
"""
def overallRun( args , f ) : 

    
    AiCommonRoutines.setDebugModes( args.writeDebugConsole != 0 , args.writeDebugLogFile != 0 )
    AiCommonRoutines.setTacModes( args.writeTacConsole != 0 , args.writeTacLogFile != 0 )

    expressionStr = args.expr

    wrt = args.wrt

    resultStr = verifyDerivative( expressionStr , wrt , f )

    AiCommonRoutines.writeTac( "@@@ Result : " , f )
    AiCommonRoutines.writeTac( resultStr  , f)

    if f is not None :
        f.flush()

    try:
        AiCommonRoutines.writeDebug( "Attempting Parse" , f )
        tree = AiCommonRoutines.parse_to_ast( resultStr )

        AiCommonRoutines.writeTac("\n=== TAC-like output ===" , f )

        AiCommonRoutines.resetTacNum()

        tn = AiCommonRoutines.tac_print_tree(tree,f)

        AiCommonRoutines.writeTac( "Final: " + tn , f )

        if f is not None : 
            f.flush()

    except Exception as e:
        print(f"\n[ERROR] Failed to parse expression: {e}")
        





"""
Runs external command line invocation of main
"""
if __name__ == "__main__" :


    parser = argparse.ArgumentParser(description='AI Simple Dereivative')
    parser.add_argument('--expr', type=str, default= "4 * x ** 3 + 5 * x ** 2 + 7 * x" , 
        help='Expression to be parsed.  Must be understandable by the AI.')
    parser.add_argument('--wrt', type=str, default="x" ,  
        help='Variable with respect to which to take the derivative.  Since this is processed by AI, single-letter variables are likely preferred.')
    parser.add_argument('--logfile', type=str, default="None" ,  
        help='Output log file path. None for no log file.')
    parser.add_argument('--writeDebugConsole', type=int, default="0" ,  
        help='Whether to write debug text to the console.')
    parser.add_argument('--writeDebugLogFile', type=int, default="0" ,  
        help='Whether to write debug text to the log file.')
    parser.add_argument('--writeTacConsole', type=int, default="1" ,  
        help='Whether to write TAC-like text to the console.')
    parser.add_argument('--writeTacLogFile', type=int, default="1" ,  
        help='Whether to write TAC-like text to the log file.')
    args = parser.parse_args()

    
    if args.logfile != "None" :
        with open( "FinalResults.txt" , "w" , encoding="utf-8" ) as f :


            overallRun( args , f )
    else: 

        overallRun( args , None )




    