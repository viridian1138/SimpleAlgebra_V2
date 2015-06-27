


package simplealgebra.store;




import org.hypergraphdb.*;




public class SegmentedTransactionManager {
	
	
	protected static boolean inSegmentedTransaction = false;
	
	
	public static void beginSegmentedTransaction( HyperGraph graph )
	{
		graph.getTransactionManager().beginTransaction();
		inSegmentedTransaction = true;
	}
	
	
	
	public static void commitSegmentedTransaction( HyperGraph graph )
	{
		graph.getTransactionManager().commit();
		inSegmentedTransaction = false;
	}
	
	
	
	public static void suspendSegmentedTransaction( HyperGraph graph )
	{
		if( inSegmentedTransaction )
		{
			graph.getTransactionManager().commit();
		}
	}
	
	
	public static void restartSegmentedTransaction( HyperGraph graph )
	{
		if( inSegmentedTransaction )
		{
			graph.getTransactionManager().beginTransaction();
		}
	}
	

	
}

