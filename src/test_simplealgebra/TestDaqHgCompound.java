



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




package test_simplealgebra;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.hypergraphdb.HGQuery.hg;
import org.hypergraphdb.HyperGraph;
import org.hypergraphdb.query.HGQueryCondition;

import simplealgebra.DoubleElem;
import simplealgebra.DoubleElemFactory;
import simplealgebra.et.EinsteinTensorElem;
import simplealgebra.ga.GeometricAlgebraMultivectorElem;
import simplealgebra.ga.GeometricAlgebraOrd;
import simplealgebra.store.DaqHg;
import simplealgebra.store.DaqHgArrayListResultHandler;
import simplealgebra.store.DaqHgContext;
import simplealgebra.store.QueryIterable;
import simplealgebra.store.SegmentedTransactionManager;
import simplealgebra.store.TypeSystemInit;



/**  
 * Tests compound query that performs a join between a query of multivectors and a query of tensors.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class TestDaqHgCompound extends TestCase {
	
	
	
	/**
	 * Compound query result containing both a tensor and a multivector.
	 * 
	 * @author thorngreen
	 *
	 */
	public static class CompoundQueryResult
	{
		
		protected GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> gaResult;
		
		protected EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> tensorResult;

		public GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory> getGaResult() {
			return gaResult;
		}

		public void setGaResult(
				GeometricAlgebraMultivectorElem<TestDimensionTwo, GeometricAlgebraOrd<TestDimensionTwo>, DoubleElem, DoubleElemFactory> gaResult) {
			this.gaResult = gaResult;
		}

		public EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> getTensorResult() {
			return tensorResult;
		}

		public void setTensorResult(
				EinsteinTensorElem<String, DoubleElem, DoubleElemFactory> tensorResult) {
			this.tensorResult = tensorResult;
		}
		
		
		
	}
	
	
	
	/**
	 * A compound query context.
	 * 
	 * @author thorngreen
	 *
	 */
	public static class CompoundQueryContext extends DaqHgContext<CompoundQueryResult>
	{
		protected QueryIterable<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>> compoundPrimitiveQuery;

		public QueryIterable<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> getCompoundPrimitiveQuery() {
			return compoundPrimitiveQuery;
		}

		public void setCompoundPrimitiveQuery(
				QueryIterable<EinsteinTensorElem<String, DoubleElem, DoubleElemFactory>> compoundPrimitiveQuery) {
			this.compoundPrimitiveQuery = compoundPrimitiveQuery;
		}
		
		
		public boolean isGeometricClose(
				GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> gaResult,
				EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> tensorResult
				)
		{
			final HashSet<BigInteger> hb = new HashSet<BigInteger>();
			hb.add( BigInteger.ONE );
			final DoubleElem gad = gaResult.get( hb );
			final ArrayList<BigInteger> tb = new ArrayList<BigInteger>();
			tb.add( BigInteger.ONE );
			final DoubleElem td = tensorResult.getVal( tb );
			final boolean match = Math.abs( gad.getVal() - td.getVal() ) < 3.0;
			return( match );
		}
		
		
		public CompoundQueryResult genResult(
				GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> gaResult,
				EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> tensorResult
				)
		{
			CompoundQueryResult qr = new CompoundQueryResult();
			qr.setGaResult( gaResult );
			qr.setTensorResult( tensorResult );
			return( qr );
		}
		
		
	}
	
	
	
	
	protected void setOrdinateValues( GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> el,
				double xVal , double yVal )
	{
		{
			final HashSet<BigInteger> hb = new HashSet<BigInteger>();
			hb.add( BigInteger.ZERO );
			el.setVal( hb , new DoubleElem( xVal ) );
		}
		
		{
			final HashSet<BigInteger> hb = new HashSet<BigInteger>();
			hb.add( BigInteger.ONE );
			el.setVal( hb , new DoubleElem( xVal ) );
		}
	}
	
	
	
	
	
	protected void setOrdinateValues( EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> el,
			double xVal , double yVal )
{
	{
		final ArrayList<BigInteger> hb = new ArrayList<BigInteger>();
		hb.add( BigInteger.ZERO );
		el.setVal( hb , new DoubleElem( xVal ) );
	}
	
	{
		final ArrayList<BigInteger> hb = new ArrayList<BigInteger>();
		hb.add( BigInteger.ONE );
		el.setVal( hb , new DoubleElem( xVal ) );
	}
}
	
	
	
	
	
	/**
	 * Tests compound query that performs a join between a query of multivectors and a query of tensors.
	 * 
	 * @throws Throwable
	 */
	public void testDaqHgCompound() throws Throwable
	{
		
		// System.out.println( "Started..." ); 
		
		String databaseLocation = "mydb";
		HyperGraph graph;
		
		graph = new HyperGraph( databaseLocation );
		
		TypeSystemInit.initType( graph );
		
		
		SegmentedTransactionManager.beginSegmentedTransaction( graph );
		
		
		final DoubleElemFactory fac = new DoubleElemFactory();
		
		
		final TestDimensionTwo td = new TestDimensionTwo();

		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> elMA
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );
	
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> elMB
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> elMC
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	
		
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> elMD
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	
	
		
		final GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory> elME
			= new GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>( fac , td , new GeometricAlgebraOrd<TestDimensionTwo>() );

	
		final ArrayList<String> arrU = new ArrayList<String>();
		arrU.add( "u" );
		
	
		
		
		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> elTA = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( fac , new ArrayList() , arrU );
		
		

		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> elTB = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( fac , new ArrayList() , arrU );
		
		

		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> elTC = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( fac , new ArrayList() , arrU );
		
		

		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> elTD = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( fac , new ArrayList() , arrU );
		
		

		final EinsteinTensorElem<String,DoubleElem,DoubleElemFactory> elTE = new EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>( fac , new ArrayList() , arrU );
		
		
		
		
		
		// Add a set of values to be queried.
		
		graph.add( elMA );
		setOrdinateValues( elMA , 5.0 , 5.0 );
		
		graph.add( elMB );
		setOrdinateValues( elMB , 50.0 , 25.0 );
		
		graph.add( elMC );
		setOrdinateValues( elMC , 23.0 , 21.0 );
		
		graph.add( elMD );
		setOrdinateValues( elMD , 100.0 , 100.0 );
		
		graph.add( elME );
		setOrdinateValues( elME , -55.0 , -55.0 );
		
		
		
		
		graph.add( elTA );
		setOrdinateValues( elTA , -55.3 , -55.2 );
		
		graph.add( elTB );
		setOrdinateValues( elTB , 100.1 , 100.2 );
		
		graph.add( elTC );
		setOrdinateValues( elTC , 22.8 , 20.8 );
		
		graph.add( elTD );
		setOrdinateValues( elTD , 50.2 , 25.1 );
		
		graph.add( elTE );
		setOrdinateValues( elTE , 5.0 , 5.0 );
		
		
		
		
		
		
		HGQueryCondition conditionM = hg.type( GeometricAlgebraMultivectorElem.class );
		
		HGQueryCondition conditionT = hg.type( EinsteinTensorElem.class );
		
		
		
		QueryIterable<GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>> 
			qiM = new QueryIterable<GeometricAlgebraMultivectorElem<TestDimensionTwo,GeometricAlgebraOrd<TestDimensionTwo>,DoubleElem,DoubleElemFactory>>( graph , conditionM );
		
		
		QueryIterable<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>> 
			qiT = new QueryIterable<EinsteinTensorElem<String,DoubleElem,DoubleElemFactory>>( graph , conditionT );
		
		
		CompoundQueryContext context = new CompoundQueryContext();
		
		
		context.setDefaultPrimitiveQuery( qiM );
		
		context.setCompoundPrimitiveQuery( qiT );
		
		
		DaqHg<CompoundQueryResult> dd = new DaqHg<CompoundQueryResult>();
		
		
		ArrayList<CompoundQueryResult> resultList = new ArrayList<CompoundQueryResult>();

		
		DaqHgArrayListResultHandler<CompoundQueryResult> resultHandler = new DaqHgArrayListResultHandler<CompoundQueryResult>();
		resultHandler.setResultList( resultList );
		
		
		dd.processDaqHg( "test_simplealgebra/testQueryCompound.drl" , context , resultHandler );
		
		
		Assert.assertTrue( resultList.size() == 5 );
		
		
		SegmentedTransactionManager.commitSegmentedTransaction( graph );
		
		
		graph.close();
		
		// System.out.println( "Done..." ); 
		
	}
	

	

	
}



