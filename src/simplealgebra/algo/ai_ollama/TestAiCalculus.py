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

Test AI Calculus

Ollama AI needs to have the gpt-oss:20b model loaded

Algorithm influenced generally by Agentic AI patterns

Uses some example code from:   https://markaicode.com/process-images-ollama-multimodal-ai/

"""


import argparse
import requests
import json
import base64
import re

from argparse import Namespace



import sys
from typing import Optional, Tuple

import sympy
from sympy.parsing.sympy_parser import parse_expr, standard_transformations, implicit_multiplication_application


import AiCommonRoutines

import AiCommonVerification

import AiCommonExecution



import AiSimpleIntegral

import AiSimpleDerivative
        

        







"""
Runs the overall personal training AI algorithm sequence
"""
def overallRun() : 

    
    with open( "FinalResults.txt" , "w" , encoding="utf-8" ) as f :

        while True : 



            args = Namespace( expr = "x*sin(2*x)" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "log(2*x+1)" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "4 * x ** 3 + 5 * x ** 2 + 7 * x" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "exp( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "sin( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "cos( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "sinh( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "cosh( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "1 / x" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "sqrt( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "3.45" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "pi" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "e" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "cos( y )" , wrt = "y" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleIntegral.overallRun( args , f )



            args = Namespace( expr = "4 * x ** 3 + 5 * x ** 2 + 7 * x" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "exp( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "sin( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "cos( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "sinh( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "cosh( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "log( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "sqrt( x )" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "3.45" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "pi" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "e" , wrt = "x" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )



            args = Namespace( expr = "cos( y )" , wrt = "y" , writeDebugConsole=1 , writeDebugLogFile=1 , writeTacConsole=1 , writeTacLogFile=1 )

            AiSimpleDerivative.overallRun( args , f )










"""
Runs external command line invocation of main
"""
if __name__ == "__main__":


    overallRun()







