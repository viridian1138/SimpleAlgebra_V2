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

AI Common Verification

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












def initialVerification( promptStrA , f ) : 
    """
    Performs the initial verification of the AI prompt result, with no retry.

    Args:

    promptStrA -- The initial input verification prompt.
    f -- The file to which to write logging (if file logging is turned on)

    Return:

    True/False verification or None if unable to verify.
    """

    AiCommonRoutines.writeDebug( "second verif resultA Input Prompt" , f )
    AiCommonRoutines.writeDebug( promptStrA , f )


    resultA = AiCommonRoutines.send_prompt_gpt_oss( promptStrA )


    AiCommonRoutines.writeDebug( "second verif resultA" , f )
    AiCommonRoutines.writeDebug( resultA , f )

    if ( "No" in resultA ) and not ( "Yes" in resultA ) :
        return False

    if ( "Yes" in resultA ) and not ( "No" in resultA ) :
        return True

    return None









def secondVerification( promptStrA , f ) : 
    """
    Performs the second verification of the AI prompt result, with retry.

    Args:

    promptStrA -- The initial input verification prompt.
    f -- The file to which to write logging (if file logging is turned on)

    Return:

    True/False verification or False if unable to verify.
    """

    retryCount = 0

    while retryCount < 10 :

        result = initialVerification( promptStrA , f )

        AiCommonRoutines.writeDebug( "second verif result" , f )
        AiCommonRoutines.writeDebug( result , f )

        if result is not None : 
            return result

        retryCount = retryCount + 1
    

    return False









def fullVerification( promptStrA , f ) : 
    """
    Performs the full verification of the AI prompt result, with retry.

    Args:

    promptStrA -- The initial input verification prompt.
    f -- The file to which to write logging (if file logging is turned on)

    Return:

    True/False verification or False if unable to verify.
    """

    result2A = secondVerification( promptStrA , f )

    AiCommonRoutines.writeDebug( "third result2A" , f )
    AiCommonRoutines.writeDebug( result2A , f )

    if result2A :

        result2B = secondVerification( promptStrA , f )

        AiCommonRoutines.writeDebug( "third result2B" , f )
        AiCommonRoutines.writeDebug( result2B , f )

        if result2B :

            result2C = secondVerification( promptStrA , f )

            AiCommonRoutines.writeDebug( "third result2C" , f )
            AiCommonRoutines.writeDebug( result2C , f )

            if result2C : 
                return True

    return False


