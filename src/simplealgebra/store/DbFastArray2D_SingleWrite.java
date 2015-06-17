
package simplealgebra.store;

import java.math.BigInteger;

import org.hypergraphdb.HGHandle;
import org.hypergraphdb.HyperGraph;


public class DbFastArray2D_SingleWrite<T extends Object> {
	
	
	HyperGraph graph;
	
	int tmult;
	
	int xmult;
	
	int dsz;
	
	HGHandle hndl;
	
	
	
	
	int tprev = -10000;
	
	int xprev = -10000;
	
	Object[][] oprev = null;
	
	boolean writeBack = false;
	
	
	
	
	public DbFastArray2D_SingleWrite( final HyperGraph _graph , int _tmult , int _xmult , int _dsz )
	{
		graph = _graph;
		tmult = _tmult;
		xmult = _xmult;
		dsz = _dsz;
		
		Object hnd = null;
		
		if( dsz > 1 )
		{
			hnd = new HGHandle[ tmult ][ xmult ];
		}
		else
		{
			hnd = new Object[ tmult ][ xmult ];
		}
		
		graph.getTransactionManager().beginTransaction();
		
		hndl = graph.add( hnd ).getPersistent();
		
		graph.getTransactionManager().commit();
		
		graph.getCache().getIncidenceCache().clear();
		
	}
	
	
	/**
	 * Gets the object at the 2-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @return The object at the 2-D index, or null if no object exists.
	 */
	public T get( int t , int x )
	{
		if( ( t / tmult == tprev / tmult ) && ( x / xmult == xprev / xmult ) )
		{
			if( oprev != null )
			{
				return( (T)( oprev[ t % tmult ][ x % xmult ] ) );
			}
			else
			{
				return( null );
			}
		}
		else
		{
			close();
		}
		
		writeBack = false;
		
		tprev = t;
		xprev = x;
		
		final int[] indext = new int[ dsz ];
		final int[] indexx = new int[ dsz ];
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			indexx[ ( dsz - 1 ) - cnt ] = x % xmult; 
			t = t / tmult;
			x = x / xmult;
		}
		
		
		HGHandle cur = hndl;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			HGHandle[][] obj = graph.get( cur );
			graph.getCache().getIncidenceCache().clear();
			cur = obj[ indext[ cnt ] ][ indexx[ cnt ] ];
			if( cur == null )
			{
				oprev = null;
				return( null );
			}
		}
		
		
		Object[][] obj = graph.get( cur );
		graph.getCache().getIncidenceCache().clear();
		oprev = obj;
		return( (T)( obj[ indext[ dsz - 1 ] ][ indexx[ dsz - 1 ] ] ) );
	}
	
	
	/**
	 * Sets the object at the 2-D index.
	 * 
	 * @param t The "T" index of the array.
	 * @param x The "X" index of the array.
	 * @param val The object to be set at the index.
	 */
	public void set( int t , int x , T val )
	{
		
		if( ( t / tmult == tprev / tmult ) && ( x / xmult == xprev / xmult ) )
		{
			if( oprev != null )
			{
				oprev[ t % tmult ][ x % xmult ] = val;
				
				writeBack = true;
				
				return;
			}
		}
		else
		{
			close();
		}
		
		writeBack = true;
		tprev = t;
		xprev = x;
		
		final int[] indext = new int[ dsz ];
		final int[] indexx = new int[ dsz ];
		for( int cnt = 0 ; cnt < dsz ; cnt++ )
		{
			indext[ ( dsz - 1 ) - cnt ] = t % tmult; 
			indexx[ ( dsz - 1 ) - cnt ] = x % xmult; 
			t = t / tmult;
			x = x / xmult;
		}
		
		
		HGHandle cur = hndl;
		
		
		for( int cnt = 0 ; cnt < ( dsz - 1 ) ; cnt++ )
		{
			HGHandle[][] obj = graph.get( cur );
			HGHandle acur = obj[ indext[ cnt ] ][ indexx[ cnt ] ];
			if( acur == null )
			{
				if( cnt != ( dsz - 2 ) )
				{
					HGHandle[][] hnd = new HGHandle[ tmult ][ xmult ];
				
					graph.getTransactionManager().beginTransaction();
				
					HGHandle hndd = graph.add( hnd ).getPersistent();
					obj[ indext[ cnt ] ][ indexx[ cnt ] ] = hndd;
					graph.update( obj );
				
					graph.getTransactionManager().commit();
				
					cur = hndd;
				}
				else
				{
					Object[][] hnd = new Object[ tmult ][ xmult ];
					
					graph.getTransactionManager().beginTransaction();
				
					HGHandle hndd = graph.add( hnd ).getPersistent();
					obj[ indext[ cnt ] ][ indexx[ cnt ] ] = hndd;
					graph.update( obj );
				
					graph.getTransactionManager().commit();
				
					cur = hndd;
				}
			}
			else
			{
				cur = acur;
			}
			graph.getCache().getIncidenceCache().clear();
		}
		
		
		Object[][] obj = graph.get( cur );
		obj[ indext[ dsz - 1 ] ][ indexx[ dsz - 1 ] ] = val;
		
		graph.getCache().getIncidenceCache().clear();
		
		oprev = obj;
	}
	
	
	public void close()
	{
		if( writeBack && ( oprev != null ) )
		{
			graph.getTransactionManager().beginTransaction();
		
			graph.update( oprev );
		
			graph.getTransactionManager().commit();
		
			graph.getCache().getIncidenceCache().clear();
		}
	}
	
	

}

