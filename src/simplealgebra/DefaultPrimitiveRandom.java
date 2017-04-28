
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

package simplealgebra;

import java.io.PrintStream;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Random;

/**
 * A derfault primitive random number generator.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class DefaultPrimitiveRandom extends PrimitiveRandom {
	
	/**
	 * Java random number generator.
	 */
	protected Random rand;
	
	/**
	 * Constructs the class.
	 * @param _rand Java random number generator.
	 */
	public DefaultPrimitiveRandom( final Random _rand )
	{
		rand = _rand;
	}
	
	/**
	 * Gets the Java random number generator.
	 * @return The Java random number generator.
	 */
	public Random getRand()
	{
		return( rand );
	}

	@Override
	public double nextRandom(double max) {
		return ((rand.nextDouble()) * max);
	}

	@Override
	public void writeTypeString(PrintStream ps) {
		ps.print(PrimitiveRandom.class.getSimpleName());
	}

	@Override
	public String writeDesc(WritePrimitiveRandomCache cache, PrintStream ps) {
		String st = cache.getFac(this);
		if (st == null) {
			st = cache.getIncrementVal();
			cache.putFac(this, st);
			writeTypeString(ps);
			ps.print(" ");
			ps.print(st);
			ps.print(" = PrimitiveRandom.genRand( new java.util.Random( ");
			try {
				Field field = Random.class.getDeclaredField("seed");
				field.setAccessible(true);
				ps.print( field.getLong(rand) + "L" );
			} catch (Throwable ex) {
				ex.printStackTrace(System.out);
			}
			ps.println(" ) );");
		}
		return (st);
	}

	@Override
	public BigInteger nextRandom(BigInteger max) {

		final BigInteger imax = max.abs();

		final int MVAL = 10;
		final BigInteger BMVAL = BigInteger.valueOf(MVAL);

		BigInteger st = BigInteger.valueOf(rand.nextInt(MVAL));
		BigInteger stm = BMVAL;

		while (stm.compareTo(imax) < 0) {
			stm = stm.multiply(BMVAL);
			st = (st.multiply(BMVAL))
					.add(BigInteger.valueOf(rand.nextInt(MVAL)));
		}

		BigInteger iret = st.mod(imax);
		if (max.compareTo(BigInteger.ZERO) < 0) {
			iret = iret.negate();
		}

		return (iret);
	}

}
