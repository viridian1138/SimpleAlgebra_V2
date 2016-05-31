
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
	 * Planck's Constant <math display="inline">
     * <mrow>
     * <mi>h</mi>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> H = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(6.62606957E-34), new DoubleElem(0.00000029E-34));

	/**
	 * Planck's Constant over <math display="inline">
     * <mrow>
     * <mn>2</mn>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>, <math display="inline">
     * <mrow>
     * <mi>&hbar;</mi>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> HBAR = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.054571726E-34), new DoubleElem(0.000000047E-34));

	/**
	 * Speed of light in vacuum, <math display="inline">
     * <mrow>
     * <mi>c</mi>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> C = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(299792458.0), new DoubleElem(0.0));

	/**
	 * Newtonian Constant of Gravitation, <math display="inline">
     * <mrow>
     * <mi>G</mi>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> G = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(6.67408E-11), new DoubleElem(0.00031E-11));

	/**
	 * Boltzmann Constant, <math display="inline">
     * <mrow>
     * <mi>k</mi>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> K = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.38064852E-23), new DoubleElem(0.00000079E-23));

	/**
	 * Elementary Charge (the charge of the electron <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>)
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> Q_E = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.6021766208E-19), new DoubleElem(0.0000000098E-19));

	/**
	 * Electron Mass, <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>m</mi>
     *   <mi>e</mi>
     *  </msub>
     * </mrow>
     * </math>
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> M_E = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(9.10938356E-31), new DoubleElem(0.00000011E-31));

	/**
	 * Proton Mass, <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>m</mi>
     *   <mi>p</mi>
     *  </msub>
     * </mrow>
     * </math>
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> M_P = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1.672621898E-27), new DoubleElem(0.000000021E-27));

	// Standard numeric constants. Uncertainty represents the accuracy of the
	// JVM-defined constants.

	/**
	 * <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> PI = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(Math.PI), new DoubleElem(1E-15));

	/**
	 * The natural exponent, <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> E = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(Math.E), new DoubleElem(1E-15));

	/**
	 * The value of the number four, used to calculate other constants.
	 */
	private static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> FOUR = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(4.0), new DoubleElem(0.0));

	/**
	 * The value of the number <math display="inline">
     * <mrow>
     *  <msup>
     *     <mn>10</mn>
     *   <mn>-7</mn>
     *  </msup>
     * </mrow>
     * </math>, used to calculate other constants.
	 */
	private static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> EM7 = new ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory>(
			new DoubleElem(1E-7), new DoubleElem(0.0));

	// Constants that require the use of other constants such as PI.

	/**
	 * Calculates the Gravitational Permitivity  <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&epsilon;</mi>
     *   <mrow><mn>0</mn><mo>,</mo><mi>g</mi></mrow>
     *  </msub>
     * </mrow>
     * </math>.
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
	 * Gravitational Permitivity of Free Space  <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&epsilon;</mi>
     *    <mrow><mn>0</mn><mo>,</mo><mi>g</mi></mrow>
     *  </msub>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> EPSILON_0G = calcEpsilon_g();

	/**
	 * Calculates the Magnetic Permeability  <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&mu;</mi>
     *   <mn>0</mn>
     *  </msub>
     * </mrow>
     * </math>.
	 * 
	 * @return The Magnetic Permeability.
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcMu_0() {
		return (EM7.mult(FOUR).mult(PI));
	}

	   /**
         * Magnetic Permeability of Free Space, <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&mu;</mi>
     *   <mn>0</mn>
     *  </msub>
     * </mrow>
     * </math>.
         */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> MU_0 = calcMu_0();

	/**
	 * Calculates the Gravitomagnetic Permeability  <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&mu;</mi>
     *    <mrow><mn>0</mn><mo>,</mo><mi>g</mi></mrow>
     *  </msub>
     * </mrow>
     * </math> (presumed).
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
	 * Gravitomagnetic Permeability of Free Space  <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&mu;</mi>
     *    <mrow><mn>0</mn><mo>,</mo><mi>g</mi></mrow>
     *  </msub>
     * </mrow>
     * </math> (presumed).
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> MU_0G = calcMu_g();

	/**
	 * Calculates the Electrical Permitivity  <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&epsilon;</mi>
     *   <mn>0</mn>
     *  </msub>
     * </mrow>
     * </math>.
	 * 
	 * @return The Electrical Permitivity.
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcEpsilon_0() {
		try {
			return ((C.mult(C).mult(MU_0)).invertLeft());
		} catch (NotInvertibleException ex) {
			throw (new RuntimeException("Failed"));
		}
	}

	/**
	 * Electrical Permitivity of Free Space  <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>&epsilon;</mi>
     *   <mn>0</mn>
     *  </msub>
     * </mrow>
     * </math>.
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> EPSILON_0 = calcEpsilon_0();

	/**
	 * Calculates the Coulomb Constant.
	 * 
	 * @return The Coulomb Constant.
	 */
	private static ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> calcCoul() {
		try {
			return ((PI.mult(FOUR).mult(EPSILON_0)).invertLeft());
		} catch (NotInvertibleException ex) {
			throw (new RuntimeException("Failed"));
		}
	}

	/**
	 * Coulomb Constant
	 */
	public static final ValueWithUncertaintyElem<DoubleElem, DoubleElemFactory> COUL = calcCoul();

}
