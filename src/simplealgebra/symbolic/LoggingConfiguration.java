package simplealgebra.symbolic;


public class LoggingConfiguration {
	
	public static boolean LOGGING_ON = false;
	
	public void performLogging( Object obj )
	{
		System.out.println( "Logging:" );
		System.out.println( obj );
		
		if( obj instanceof SymbolicElem )
		{
			System.out.println( ((SymbolicElem) obj).writeString() );
		}
		
		if( obj instanceof Reng )
		{
			System.out.println( ((Reng) obj).getStrt().writeString() );
			System.out.println( "-->" );
			System.out.println( ((Reng) obj).getEnd().writeString() );
		}
		
		System.out.println( "------------" );
	}

}

