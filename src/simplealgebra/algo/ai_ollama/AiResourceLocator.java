


//$$strtCprt
/**
* Simple Algebra 
* 
* Copyright (C) 2014 Thornton Green
* 
* This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
* published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
* of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
* You should have received a copy of the GNU General Public License along with this program; if not, 
* see <http://www.gnu.org/licenses>.
* Additional permission under GNU GPL version 3 section 7
*
*/
//$$endCprt





package simplealgebra.algo.ai_ollama;


import java.net.*;
import java.nio.*;
import java.nio.file.*;


/**
 * Locates local resources and returns equivalent file paths.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author tgreen
 *
 */
public class AiResourceLocator {
	
	
	/**
	 * Gets the filename for a particular local resource name.
	 * @param resourceFileName The input file name.
	 * @return The output local resource file name.
	 * @throws Throwable Throws an exception if the resource cannot be found or converted.
	 */
	public static String locateResourceAsFile( String resourceFileName ) throws Throwable
	{
		
		URL url = AiResourceLocator.class.getResource( resourceFileName );
		
		URI uri = url.toURI();
		
		if( "file".equalsIgnoreCase( uri.getScheme() ) )
		{
			final Path path = Paths.get( uri );
			return( path.toString() );
		}
		
		throw( new RuntimeException( "Failed-- Resource Does Not Point To File" ) );
		
	}
	
	

}

