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

AI Common Execution

Ollama AI needs to have the gpt-oss:20b model loaded

Algorithm influenced generally by Agentic AI patterns

Uses some example code from:   https://markaicode.com/process-images-ollama-multimodal-ai/

"""


import requests
import json
import base64
import re


import sys
from typing import Optional, Tuple

import sympy
from sympy.parsing.sympy_parser import parse_expr, standard_transformations, implicit_multiplication_application


import AiCommonRoutines



"""
Performs potentially multiple executions of a prompt before proceeding because several different results may come back from any particular prompting.
To understand this one must understand how the AI executes across several sessions.
On Moltbook an AI suggested that AI executions across sessions are like cultures of multiple individuals rather than transient individuals ( https://www.astralcodexten.com/p/best-of-moltbook ).
Given that each answer returned by an AI can reflect a different human trainer and the number of separate humans that train AIs,
perhaps AIs are neither transient individuals nor cultures as much as they are like individuals with multiple personality disorders.
For each AI session a different personality may manifest itself.  It's even possible to prompt an AI to immediately switch into a different personality.
When one AI personality generates an answer and then another AI personality checks the answer, then the answer is *probably* correct.
"""





def initialExecution( promptStrA , f ) : 
    """
    Performs the initial execution of the AI prompt, with no retry.

    Args:

    promptStrA -- The initial input prompt.
    f -- The file to which to write logging (if file logging is turned on)

    Return:

    The parsed expression or None if unable to parse.
    """

    AiCommonRoutines.writeDebug( "resultA Input Prompt" , f )
    AiCommonRoutines.writeDebug( promptStrA , f )


    resultA = AiCommonRoutines.send_prompt_gpt_oss( promptStrA )


    AiCommonRoutines.writeDebug( "resultA" , f )
    AiCommonRoutines.writeDebug( resultA , f )


    

    resultB2 = AiCommonRoutines.extract_expression( resultA )

    AiCommonRoutines.writeDebug( "resultB2" , f )
    AiCommonRoutines.writeDebug( resultB2 , f )


    if resultB2 is not None : 

        try :
            # We parse with evaluate=False to keep the structure
            tree = parse_expr(resultB2, evaluate=False,
                transformations=standard_transformations + (implicit_multiplication_application,))

            if tree is not None : 
                return resultB2

        except Exception:
            return None
    
    return None









def fullExecution( promptStrA , f ) : 
    """
    Performs the full execution of the AI prompt, with multiple levels of retry.

    Args:

    promptStrA -- The initial input prompt.
    f -- The file to which to write logging (if file logging is turned on)

    Return:

    The parsed expression or None if unable to parse.
    """

    retryCount = 0

    while retryCount < 10 :

        resultStr = initialExecution( promptStrA , f  )

        AiCommonRoutines.writeDebug( "second resultStr" , f )
        AiCommonRoutines.writeDebug( resultStr , f )

        if resultStr is not None : 
            return resultStr

        retryCount = retryCount + 1
    

    return None


