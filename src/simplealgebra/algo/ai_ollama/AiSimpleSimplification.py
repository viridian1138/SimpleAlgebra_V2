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

AI Simple Simplification Test

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
Requests for Ollama AI to determine and verify the simplification


Args:

expressionStr -- The expression for which to calculate the simplification
nonCommutative-- Whether the algebra is non-commutative
nonAssociative-- Whether the algebra is non-associative
f -- The file to which to write logging (if file logging is turned on)

Return:

The result string, or None if the result can't be verified.
"""
def verifySimplify( expressionStrIn , nonCommutative , nonAssociative , f ) : 

    retryCount = 0

    expressionStr = expressionStrIn

    while retryCount < 10 :

        prevStrA = ""

        if nonCommutative or nonAssociative : 
            prevStrA = "For an algebra where"

        if nonCommutative : 
            prevStrA = prevStrA + " products are non-commutative"

        if nonCommutative and nonAssociative : 
            prevStrA = prevStrA + " and"

        if nonAssociative : 
            prevStrA = prevStrA + " products are non-associative"

        if len( prevStrA ) > 0 :
            prevStrA = prevStrA + ", "

        simpStrA = "Simplify "

        if len( prevStrA ) > 0 :
            simpStrA = prevStrA + "simplify"

        promptStrA = "Format the answer as a SymPy expression.  " + simpStrA + " The Expression: " + expressionStr

        resultStr = AiCommonExecution.fullExecution( promptStrA , f )

        AiCommonRoutines.writeDebug( "verify resultStr" , f )
        AiCommonRoutines.writeDebug( resultStr , f )

        if resultStr is not None : 

            promptStrA = "Answer Yes/No, " + ( prevStrA.lower() ) + " does the simplification of ( " + expressionStr + " ) yield the answer ( " + resultStr + " ) ? "

            result = AiCommonVerification.fullVerification( promptStrA , f )

            if result : 
                return resultStr

        retryCount = retryCount + 1
    

    return None






"""
Runs the overall AI simplification algorithm


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

    nonCommutative = args.nonCommutative != 0
    nonAssociative = args.nonAssociative != 0

    resultStr = verifySimplify( expressionStr , nonCommutative , nonAssociative , f )

    AiCommonRoutines.writeTac( "@@@ Result : " , f )
    AiCommonRoutines.writeTac( resultStr , f )

    if f is not None :
        f.flush()

    try:
        AiCommonRoutines.writeDebug( "Attempting Parse" , f )
        tree = AiCommonRoutines.parse_to_ast( resultStr )

        AiCommonRoutines.writeTac("\n=== TAC-like output ===" , f)

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


    parser = argparse.ArgumentParser(description='AI Simple Simplification')
    parser.add_argument('--expr', type=str, default="c * a + c * b" ,  
        help='Expression to be parsed.  Must be understandable by the AI.')
    parser.add_argument('--nonCommutative', type=int, default="0" ,  
        help='Whether the algebra is generally non-commutative.')
    parser.add_argument('--nonAssociative', type=int, default="0" ,  
        help='Whether the algebra is generally non-associative.')
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




    
