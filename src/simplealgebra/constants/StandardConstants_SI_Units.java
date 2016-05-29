
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

package simplealgebra.constants;

import simplealgebra.*;
import simplealgebra.meas.*;

/**
 * 
 * Standard constants in SI units as defined (for 2010) at:
 * 
 * https://physics.nist.gov/cuu/Constants/index.html
 * 
 * 
 * Plus assorted numeric constants from the JVM.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class StandardConstants_SI_Units {

	/**
	 * Planck's Constant.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> H = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(6.62606957E-34), new DoubleElem(0.00000029E-34));

	/**
	 * Planck's Constant over 2 pi.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> HBAR = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.054571726E-34), new DoubleElem(0.000000047E-34));

	/**
	 * Speed of light in vacuum.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> C = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(299792458.0), new DoubleElem(0.0));

	/**
	 * Newtonian Constant of Gravitation
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> G = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(6.67408E-11), new DoubleElem(0.00031E-11));

	/**
	 * Boltzmann Constant
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> k = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.38064852E-23), new DoubleElem(0.00000079E-23));

	/**
	 * Elementary Charge
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> e = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.6021766208E-19), new DoubleElem(0.0000000098E-19));

	/**
	 * Electron Mass
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> me = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(9.10938356E-31), new DoubleElem(0.00000011E-31));

	/**
	 * Proton Mass
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> mp = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.672621898E-27), new DoubleElem(0.000000021E-27));

	// Standard numeric constants. Uncertainty represents the accuracy of the
	// JVM-defined constants.

	/**
	 * Pi.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> PI = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(Math.PI), new DoubleElem(1E-15));

	/**
	 * E.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> E = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(Math.E), new DoubleElem(1E-15));

	/**
	 * The value of the number four, used to calculate other constants.
	 */
	private static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> FOUR = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(4.0), new DoubleElem(0.0));

	/**
	 * The value of the number 1E-7, used to calculate other constants.
	 */
	private static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> EM7 = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1E-7), new DoubleElem(0.0));

	// Constants that require the use of other constants such as PI.

	/**
	 * Calculates the Gravitational Permitivity.
	 * 
	 * @return The Gravitational Permitivity.
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcEpsilon_g() {
		try {
			return ((PI.mult(FOUR).mult(G)).invertLeft().negate());
		} catch (NotInvertibleException ex) {
			throw (new RuntimeException("Failed"));
		}
	}

	/**
	 * Gravitational Permitivity of Free Space.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> epsilon_g = calcEpsilon_g();

	/**
	 * Calculates the Magnetic Permeability.
	 * 
	 * @return The Magnetic Permeability.
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcMu_0() {
		return (EM7.mult(FOUR).mult(PI));
	}

	/**
	 * Magnetic Permeability of Free Space.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> mu_0 = calcMu_0();

	/**
	 * Calculates the Gravitomagnetic Permeability (presumed).
	 * 
	 * @return The Gravitomagnetic Permeability (presumed).
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcMu_g() {
		try {
			return ((FOUR.mult(PI).mult(G)).mult((C.mult(C)).invertLeft())
					.negate());
		} catch (NotInvertibleException ex) {
			throw (new RuntimeException("Failed"));
		}
	}

	/**
	 * Gravitomagnetic Permeability of Free Space (presumed).
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> mu_g = calcMu_g();

	/**
	 * Calculates the Electrical Permitivity.
	 * 
	 * @return The Electrical Permitivity.
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcEpsilon_0() {
		try {
			return ((C.mult(C).mult(mu_0)).invertLeft());
		} catch (NotInvertibleException ex) {
			throw (new RuntimeException("Failed"));
		}
	}

	/**
	 * Electrical Permitivity of Free Space.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> epsilon_0 = calcEpsilon_0();

	/**
	 * Calculates the Coulomb Constant.
	 * 
	 * @return The Coulomb Constant.
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcCoul() {
		try {
			return ((PI.mult(FOUR).mult(epsilon_0)).invertLeft());
		} catch (NotInvertibleException ex) {
			throw (new RuntimeException("Failed"));
		}
	}

	/**
	 * Coulomb Constant
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> Coul = calcCoul();

}
