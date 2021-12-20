




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






package simplealgebra.culang;

import java.util.HashMap;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.Elem;
import simplealgebra.ElemFactory;
import simplealgebra.symbolic.SymbolicElem;


/**
 * Class for converting some symbolic expressions to NVIDIA Cuda native code
 * for execution-time performance optimization.
 * 
 * @author tgreen
 *
 */
public class CulangCompile<R extends Elem<R,?>, S extends ElemFactory<R,S>> {

	/**
	 * Constructor.
	 */
	public CulangCompile() {
	}
	
	
	/**
	 * Attempts to convert a SymbolicElem to culang.
	 * @param in The SymbolicElem to be converted for the value.
	 * @param in2 The SymbolicElem to be converted for the partial derivative of the value.
	 * @param templatePath The path to the template file.
	 * @param replaceMap Map of replacements to perform on #defines in the template file.
	 * @return The culang-converted SymbolicElem, or the original SymbolicElem if culang-conversion failed.
	 */
	public void attemptCulangCompile( SymbolicElem<R,S> in , SymbolicElem<R,S> in2 , final String templatePath , HashMap<String,String> replaceMap )
	{
		
		try
		{
			final S fac = in.getFac().getFac();
			
			if( fac instanceof DoubleElemFactory )
			{
				String f1 = ( new Culang() ).culang_Dbl( (SymbolicElem<DoubleElem,DoubleElemFactory>) in , templatePath , 
						"__device__ void evalValue( DblEnt* arr ," , replaceMap );
				String f2 = ( new Culang() ).culang_Dbl( (SymbolicElem<DoubleElem,DoubleElemFactory>) in2 , f1 , 
						"__device__ void evalPartialDerivative( DblEnt* arr ," , replaceMap );
				( new Culang() ).runNativeBuild();
			}
			
			if( fac instanceof ComplexElemFactory )
			{
				ComplexElemFactory<?,?> cfac = (ComplexElemFactory) fac;
				if( cfac.getFac() instanceof DoubleElemFactory )
				{	
					String f1 = ( new Culang() ).culang_Cplx_Dbl( (SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in ,  templatePath , 
							"__device__ void evalValue( CplxEnt* arr ," , replaceMap  );
					String f2 = ( new Culang() ).culang_Cplx_Dbl( (SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in2 , f1 , 
							"__device__ void evalPartialDerivative( CplxEnt* arr ," , replaceMap );
					( new Culang() ).runNativeBuild();
					
				}
			}
			
		}
		catch(  Throwable ex )
		{
			ex.printStackTrace( System.out );
		}
		
	}
	
	
	/**
	 * Evaluates the compilation.
	 */
	public void eval()
	{
		throw( new RuntimeException( "Not Implemented Yet" ) );
		// fldfksdlfkdslfksdl;f; // Implementation Here
	}
	

	
}


