



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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import simplealgebra.ComplexElem;
import simplealgebra.ComplexElemFactory;
import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.symbolic.SymbolicAbsoluteValue;
import simplealgebra.symbolic.SymbolicAdd;
import simplealgebra.symbolic.SymbolicDivideBy;
import simplealgebra.symbolic.SymbolicElem;
import simplealgebra.symbolic.SymbolicIdentity;
import simplealgebra.symbolic.SymbolicInvertLeft;
import simplealgebra.symbolic.SymbolicInvertRight;
import simplealgebra.symbolic.SymbolicMult;
import simplealgebra.symbolic.SymbolicNegate;
import simplealgebra.symbolic.SymbolicReduction;
import simplealgebra.symbolic.SymbolicZero;



/**
 * Class for converting some symbolic expressions to NVIDIA Cuda native code
 * for execution-time performance optimization.
 * 
 * @author tgreen
 *
 */
public class Culang {
	
		
	
	/**
	 * Class for storing individual variable names contributing to a complex number.
	 * 
	 * @author tgreen
	 *
	 */
	protected static class CplxRec
	{
		
		/**
		 * Constructor.
		 * @param _re Variable name for real value.
		 * @param _im Variable name for imaginary value.
		 */
		public CplxRec( String _re , String _im )
		{
			re = _re;
			im = _im;
		}
		
		/**
		 * Variable name for real value.
		 */
		String re;
		
		/**
		 * Variable name for imaginary value.
		 */
		String im;
	}
	
	
	/**
	 * The temporary directory in which to write and build culang code.
	 */
	static File tempDir = null;
	
	/**
	 * Allocation number used to assign unique IDs variables, classes, etc.
	 */
	static long allocNum = 0;
	
	
	
	/**
	 * Stream for adding the actual expression evaluation logic to the generated culang code.
	 */
	static PrintStream psInst = null;
	
	/**
	 * Stream for adding local declarations to the generated culang code.
	 */
	static PrintStream jlocmemInst = null;
	
	/**
	 * Stream for adding initialization of local declarations to the generated culang code.
	 */
	static PrintStream jfldInst = null;
	
	/**
	 * Map used to prevent redundant calculations by tracking re-use of SymbolicElems.
	 */
	static HashMap<SymbolicElem<?,?>,Object> reuseMap = null;
	
	
	
	
	/**
	 * The script file that invokes gcc compilation.
	 */
	static File fgcc = null;

	
	
	
	/**
	 * Performs post-processing on the template line and then outputs it as generated Cuda code.
	 * @param iline The line to be processed.
	 * @param ps The stream to which to write the result.
	 * @param replaceMap Set of replacements to make for #defines in the line.
	 */
	protected static void outputParsedLine( final String iline , PrintStream ps , HashMap<String,String> replaceMap )
	{
		String line = iline;
		boolean endN = iline.endsWith( "\\n\\" );
		for( Entry<String,String> e : replaceMap.entrySet() )
		{
			if( iline.startsWith( "#define " + ( e.getKey() ) ) )
			{
				line = "#define " + ( e.getKey() ) + " " + ( e.getValue() ) + ( endN ? "\\n\\" : "" );
			}
		}
		ps.println( line );
	}
	
	
	
	
	
	/**
	 * Handles a particular Elem of type SymbolicElem<DoubleElem,DoubleElemFactory>
	 * @param in The Elem to be handled.
	 * @return The generated culang variable name for the Elem.
	 * @throws Throwable
	 */
	protected static String hndl_Dbl( SymbolicElem<DoubleElem,DoubleElemFactory> in ) throws Throwable
	{
		if( reuseMap.get( in ) != null )
		{
			return( (String)( reuseMap.get( in ) ) );
		}
		
		if( in instanceof SymbolicZero )
		{
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 0.0;" );
			return( vName );
		}
		
		if( in instanceof SymbolicIdentity )
		{
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 1.0;" );
			return( vName );
		}
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<DoubleElem,DoubleElemFactory> neg
				= (SymbolicNegate<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = - " + arg + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicAdd )
		{
			SymbolicAdd<DoubleElem,DoubleElemFactory> neg
				= (SymbolicAdd<DoubleElem,DoubleElemFactory>) in;
			String argA = hndl_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			String argB = hndl_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + argA + " + " + argB + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicMult )
		{
			SymbolicMult<DoubleElem,DoubleElemFactory> neg
				= (SymbolicMult<DoubleElem,DoubleElemFactory>) in;
			String argA = hndl_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			String argB = hndl_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + argA + " * " + argB + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicDivideBy )
		{
			SymbolicDivideBy<DoubleElem,DoubleElemFactory> neg
				= (SymbolicDivideBy<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + arg + " / " + ( neg.getIval() ) + "L;" );
			return( vName );
		}
		
		if( in instanceof SymbolicInvertLeft )
		{
			SymbolicInvertLeft<DoubleElem,DoubleElemFactory> neg
				= (SymbolicInvertLeft<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 1.0 / " + arg + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicInvertRight )
		{
			SymbolicInvertRight<DoubleElem,DoubleElemFactory> neg
				= (SymbolicInvertRight<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = 1.0 / " + arg + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicReduction )
		{
			final DoubleElem eval = in.eval( null );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = " + ( eval.getVal() ) + ";" );
			return( vName );
		}
		
		if( in instanceof SymbolicAbsoluteValue )
		{
			SymbolicAbsoluteValue<DoubleElem,DoubleElemFactory> neg
				= (SymbolicAbsoluteValue<DoubleElem,DoubleElemFactory>) in;
			String arg = hndl_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vName = "t_" + allocNum;
			psInst.println( "const double " + vName + " = fabs( " + arg + " );" );
			return( vName );
		}
		
		if( in instanceof CuGen_Dbl )
		{
			allocNum++;
			final String vName = "t_" + allocNum;
			( (CuGen_Dbl) in ).genCuda(vName, psInst);
			return( vName );
		}
		
		
		
		throw( new RuntimeException( "Not Recigized / Supported " + in ) );
	}
	
	
	

	
	
	/**
	 * Handles a particular Elem of type SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>
	 * @param in The Elem to be handled.
	 * @return The generated culang variable names for the Elem.
	 * @throws Throwable
	 */
	protected static CplxRec hndl_Cplx_Dbl( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in ) throws Throwable
	{

		if( reuseMap.get( in ) != null )
		{
			System.out.println( "Simplified !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! " + in );
			return( (CplxRec)( reuseMap.get( in ) ) );
		}
		
		if( in instanceof SymbolicZero )
		{
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = 0.0;" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = 0.0;" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicIdentity )
		{
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = 1.0;" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = 0.0;" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicNegate )
		{
			SymbolicNegate<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicNegate<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = - " + arg.re + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = - " + arg.im + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicAdd )
		{
			SymbolicAdd<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicAdd<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec argA = hndl_Cplx_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			CplxRec argB = hndl_Cplx_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + argA.re + " + " + argB.re + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + argA.im + " + " + argB.im + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicMult )
		{
			SymbolicMult<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicMult<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec argA = hndl_Cplx_Dbl( neg.getElemA() );
			reuseMap.put( neg.getElemA() , argA );
			CplxRec argB = hndl_Cplx_Dbl( neg.getElemB() );
			reuseMap.put( neg.getElemB() , argB );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + argA.re + " * " + argB.re 
					+ " - " + argA.im + " * " + argB.im + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + argA.re + " * " + argB.im 
					+ " + " + argA.im + " * " + argB.re + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicDivideBy )
		{
			SymbolicDivideBy<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicDivideBy<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + arg.re + " / " + ( neg.getIval() ) + "L;" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + arg.im + " / " + ( neg.getIval() ) + "L;" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicInvertLeft )
		{
			SymbolicInvertLeft<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicInvertLeft<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameConj = "t_" + allocNum;
			psInst.println( "const double " + vNameConj + " = " + arg.re + " * " + arg.re + " + " + arg.im + " * " + arg.im + ";" );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + arg.re + " / " + vNameConj + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = - " + arg.im + " / " + vNameConj + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicInvertRight )
		{
			SymbolicInvertRight<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> neg
				= (SymbolicInvertRight<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>) in;
			CplxRec arg = hndl_Cplx_Dbl( neg.getElem() );
			reuseMap.put( neg.getElem() , arg );
			allocNum++;
			final String vNameConj = "t_" + allocNum;
			psInst.println( "const double " + vNameConj + " = " + arg.re + " * " + arg.re + " + " + arg.im + " * " + arg.im + ";" );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + arg.re + " / " + vNameConj + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = - " + arg.im + " / " + vNameConj + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof SymbolicReduction )
		{
			final ComplexElem<DoubleElem,DoubleElemFactory> eval = in.eval( null );
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			psInst.println( "const double " + vNameRe + " = " + ( eval.getRe().getVal() ) + ";" );
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			psInst.println( "const double " + vNameIm + " = " + ( eval.getIm().getVal() ) + ";" );
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		if( in instanceof CuGen_Cplx_Dbl )
		{
			allocNum++;
			final String vNameRe = "t_" + allocNum;
			allocNum++;
			final String vNameIm = "t_" + allocNum;
			( (CuGen_Cplx_Dbl) in ).genCuda(vNameRe, vNameIm, psInst);
			return( new CplxRec( vNameRe, vNameIm ) );
		}
		
		
		
		throw( new RuntimeException( "Not Recigized / Supported " + in ) );
	}

	
	/**
	 * Attempts to convert a SymbolicElem<DoubleElem,DoubleElemFactory> to culang.
	 * @param in The SymbolicElem<DoubleElem,DoubleElemFactory> to be converted.
	 * @param templatePath The path to the template file to use for the conversion.
	 * @param templateTag The tag on the file to acquire for the conversion.
	 * @param replaceMap Set of replacements to make for #defines in the file.
	 * @return The culang-converted SymbolicElem<DoubleElem,DoubleElemFactory>.
	 * @throws Throwable
	 */
	public static String culang_Dbl( SymbolicElem<DoubleElem,DoubleElemFactory> in , String templatePath , String templateTag , 
			HashMap<String,String> replaceMap ) throws Throwable
	{
		if( reuseMap != null ) reuseMap.clear();
		
		if( tempDir == null )
		{
			allocNum++;
			File fi = File.createTempFile( "tempDir" , "" + allocNum );
			
			if( !( fi.delete() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			if( !( fi.mkdir() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			tempDir = fi;
		}
		
		allocNum++;
		final String className = "Culang_" + allocNum;
	
		final File fculang = new File( tempDir , className + ".cpp" );
		
		fgcc = new File( tempDir , "runGCC.sh" );
		
		final File fPsInst = new File( tempDir , className + "A.txt" );
		final File fJmemInst = new File( tempDir , className + "B.txt" );
		final File fJcnstInst = new File( tempDir , className + "C.txt" );
		final File fJcimpInst = new File( tempDir , className + "D.txt" );
		final File fJlocmemInst = new File( tempDir , className + "E.txt" );
		final File fJfldInst = new File( tempDir , className + "F.txt" );
		
		
		psInst = new PrintStream( new FileOutputStream( fPsInst ) );
		jlocmemInst = new PrintStream( new FileOutputStream( fJlocmemInst ) );
		jfldInst = new PrintStream( new FileOutputStream( fJfldInst ) );
		reuseMap = new HashMap<SymbolicElem<?,?>,Object>();
		final String vName = hndl_Dbl( in );
		
		psInst.println( "out = " + vName + ";" );
		
		psInst.close();
		jlocmemInst.close();
		jfldInst.close();
		psInst = null;
		jlocmemInst = null;
		jfldInst = null;
		reuseMap = null;
		
		
		PrintStream ps = new PrintStream( new FileOutputStream( fculang ) );
		
		
		
		LineNumberReader lip = new LineNumberReader( new FileReader( templatePath ) );
		String line = lip.readLine();
		while( ( line != null ) && ( !( line.startsWith( templateTag ) ) ) )
		{
			outputParsedLine( line , ps , replaceMap );
			line = lip.readLine();
		}
		outputParsedLine( line , ps , replaceMap );
		line = lip.readLine(); // Opening brace.
		outputParsedLine( line , ps , replaceMap );
		
		
		LineNumberReader li = new LineNumberReader( new FileReader( fJfldInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line + " \\n\\" );
			line = li.readLine();
		}
		li.close();
		ps.println( "\\n\\" );
		
		li = new LineNumberReader( new FileReader( fPsInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line + " \\n\\" );
			line = li.readLine();
		}
		li.close();
		
		line = lip.readLine();
		while( ( line != null ) && ( !( line.startsWith( "}" ) ) ) )
		{
			line = lip.readLine();
		}
		outputParsedLine( line , ps , replaceMap );
		line = lip.readLine();
		while( line != null )
		{
			outputParsedLine( line , ps , replaceMap );
			line = lip.readLine();
		}
		
		ps.close();
		return( fculang.getAbsolutePath() );
	}
	


	
	/**
	 * Attempts to convert a SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> to culang.
	 * @param in The SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> to be converted.
	 * @param templatePath The path to the template file to use for the conversion.
	 * @param templateTag The tag on the file to acquire for the conversion.
	 * @param replaceMap Set of replacements to make for #defines in the file.
	 * @return The culang-converted SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>>.
	 * @throws Throwable
	 */
	public static String culang_Cplx_Dbl( SymbolicElem<ComplexElem<DoubleElem,DoubleElemFactory>,ComplexElemFactory<DoubleElem,DoubleElemFactory>> in , String templatePath , String templateTag , 
			HashMap<String,String> replaceMap ) throws Throwable
	{
		if( reuseMap != null ) reuseMap.clear();
		
		if( tempDir == null )
		{
			allocNum++;
			File fi = File.createTempFile( "tempDir" , "" + allocNum );
			
			if( !( fi.delete() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			if( !( fi.mkdir() ) )
			{
				throw( new RuntimeException( "Fail" ) );
			}
			
			tempDir = fi;
		}
		
		allocNum++;
		final String className = "Culang_" + allocNum;
		
		final File fculang = new File( tempDir , className + ".cpp" );
		
		fgcc = new File( tempDir , "runGCC.sh" );
		
		final File fPsInst= new File( tempDir , className + "A.txt" );
		final File fJmemInst= new File( tempDir , className + "B.txt" );
		final File fJcnstInst= new File( tempDir , className + "C.txt" );
		final File fJcimpInst= new File( tempDir , className + "D.txt" );
		final File fJlocmemInst= new File( tempDir , className + "E.txt" );
		final File fJfldInst= new File( tempDir , className + "F.txt" );
		
		
		psInst = new PrintStream( new FileOutputStream( fPsInst ) );
		jlocmemInst = new PrintStream( new FileOutputStream( fJlocmemInst ) );
		jfldInst = new PrintStream( new FileOutputStream( fJfldInst ) );
		reuseMap = new HashMap<SymbolicElem<?,?>,Object>();
		final CplxRec vNames = hndl_Cplx_Dbl( in );
		
		psInst.println( "out.getRe().setVal( " + vNames.re + " );" );
		psInst.println( "out.getIm().setVal( " + vNames.im + " );" );
		
		psInst.close();
		jlocmemInst.close();
		jfldInst.close();
		psInst = null;
		jlocmemInst = null;
		jfldInst = null;
		reuseMap = null;
		
		
		
		System.out.println( "Writing Translated File..." + fculang );
		PrintStream ps = new PrintStream( new FileOutputStream( fculang ) );
		
		
		System.out.println( "Reading Template " + templatePath );
		LineNumberReader lip = new LineNumberReader( new FileReader( templatePath ) );
		String line = lip.readLine();
		while( ( line != null ) && ( !( line.startsWith( templateTag ) ) ) )
		{
			outputParsedLine( line , ps , replaceMap );
			line = lip.readLine();
		}
		outputParsedLine( line , ps , replaceMap );
		line = lip.readLine(); // Opening brace.
		outputParsedLine( line , ps , replaceMap );
		
		
		LineNumberReader li = new LineNumberReader( new FileReader( fJfldInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line + " \\n\\" );
			line = li.readLine();
		}
		li.close();
		ps.println( "\\n\\" );
		
		li = new LineNumberReader( new FileReader( fPsInst ) );
		line = li.readLine();
		while( line != null )
		{
			ps.println( line + " \\n\\" );
			line = li.readLine();
		}
		li.close();
		
		line = lip.readLine();
		while( ( line != null ) && ( !( line.startsWith( "}" ) ) ) )
		{
			line = lip.readLine();
		}
		outputParsedLine( line , ps , replaceMap );
		line = lip.readLine();
		while( line != null )
		{
			outputParsedLine( line , ps , replaceMap );
			line = lip.readLine();
		}
		
		ps.close();
		return( fculang.getAbsolutePath() );
	}
	
	
	
	
	
	public File runNativeBuild( final String fculang ) throws Throwable
	{
		
		PrintStream ps = new PrintStream( new FileOutputStream( fgcc ) );
		
		
		ps.println( "cd " + tempDir.getAbsolutePath() );
		ps.println( CulangConstants.CULANG_NATIVE_COMPILATION_COMMAND + fculang + CulangConstants.CULANG_NATIVE_LINK_OUTPUT + " dpar " + CulangConstants.CULANG_NATIVE_LINK_OUTPUT2 );
		
		
		ps.close();
		
		
		
		Process p = Runtime.getRuntime().exec( "sh " + fgcc.getAbsolutePath() );
		p.waitFor();
		
		
		File fo = new File( tempDir , "dpar" );
		
		if( !( fo.exists() ) )
		{
			throw( new RuntimeException( "Compilation Failed" ) );
		}
		
		
		System.out.println( "Finished Native Culang Compile." );
		return( fo );
	}

	
	
	
	
}



