





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





package simplealgebra.symbolic;


/**
 * Configuration for logging in a Drools ( http://drools.org ) session.  This configuration
 * and its related rule can be added to a Drools in a fashion similar to adding a cross-cutting
 * concern to an aspect-oriented system.
 * 
 * @author thorngreen
 *
 */
public class LoggingConfiguration {
	
	/**
	 * Configuration setting for turning logging on/off.
	 */
	public static boolean LOGGING_ON = false;
	
	/**
	 * Called by the Drools session to log an object.
	 * 
	 * @param obj The object to be logged.
	 */
	public void performLogging( Object obj )
	{
		System.out.println( "Logging:" );
		System.out.println( obj );
		
		if( obj instanceof SymbolicElem )
		{
			((SymbolicElem) obj).writeString( System.out );
		}
		
		if( obj instanceof Reng )
		{
			((Reng) obj).getStrt().writeString( System.out );
			System.out.println( "-->" );
			((Reng) obj).getEnd().writeString( System.out );
		}
		
		System.out.println( "------------" );
	}

}

