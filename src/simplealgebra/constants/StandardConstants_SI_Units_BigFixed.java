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

import java.math.BigInteger;

import simplealgebra.*;
import simplealgebra.bigfixedpoint.*;
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
 * Format is BigFixedPoint.
 * 
 * This documentation should be viewed using Firefox version 33.1.1 or above.
 * 
 * @author thorngreen
 *
 */
public class StandardConstants_SI_Units_BigFixed<T extends Precision<T>> {

	/**
	 * The precision of the elems.
	 */
	protected T prec;

	/**
	 * Constructs the constants.
	 * @param _prec The precision of the elems.
	 */
	public StandardConstants_SI_Units_BigFixed(T _prec) {
		prec = _prec;
		H = calcH();
		HBAR = calcHbar();
		C = calcC();
		G = calcG();
		K = calcK();
		Q_E = calcQ_E();
		M_E = calcM_E();
		M_P = calcM_P();
		PI = calcPi();
		E = calcE();
		FOUR = calcFour();
		EM7 = calcEM7();
		EPSILON_0G = calcEpsilon_g();
		MU_0 = calcMu_0();
		MU_0G = calcMu_g();
		EPSILON_0 = calcEpsilon_0();
		COUL = calcCoul();
	}

	/**
	 * Planck's Constant <math display="inline">
     * <mrow>
     * <mi>h</mi>
     * </mrow>
     * </math>.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> H;

	/**
	 * Calculates Planck's Constant <math display="inline">
     * <mrow>
     * <mi>h</mi>
     * </mrow>
     * </math>.
	 * @return Planck's constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcH() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(6.62606957E-34, prec),
				new BigFixedPointElem<T>(0.00000029E-34, prec)));
	}

	/**
	 * Gets Planck's Constant <math display="inline">
     * <mrow>
     * <mi>h</mi>
     * </mrow>
     * </math>.
	 * @return Planck's constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getH() {
		return (H);
	}

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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> HBAR;

	/**
	 * Calculates Planck's Constant over <math display="inline">
     * <mrow>
     * <mn>2</mn>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>, <math display="inline">
     * <mrow>
     * <mi>&hbar;</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcHbar() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(1.054571726E-34, prec),
				new BigFixedPointElem<T>(0.000000047E-34, prec)));
	}

	/**
	 * Gets Planck's Constant over <math display="inline">
     * <mrow>
     * <mn>2</mn>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>, <math display="inline">
     * <mrow>
     * <mi>&hbar;</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getHbar() {
		return (HBAR);
	}

	
	/**
	 * Speed of light in vacuum, <math display="inline">
     * <mrow>
     * <mi>c</mi>
     * </mrow>
     * </math>.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> C;

	/**
	 * Calculates the Speed of light in vacuum, <math display="inline">
     * <mrow>
     * <mi>c</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcC() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(299792458.0, prec),
				new BigFixedPointElem<T>(BigInteger.ZERO, prec)));
	}

	/**
	 * Gets the Speed of light in vacuum, <math display="inline">
     * <mrow>
     * <mi>c</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getC() {
		return (C);
	}

	
	
	/**
	 * Newtonian Constant of Gravitation, <math display="inline">
     * <mrow>
     * <mi>G</mi>
     * </mrow>
     * </math>.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> G;

	/**
	 * Calculates the Newtonian Constant of Gravitation, <math display="inline">
     * <mrow>
     * <mi>G</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcG() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(6.67408E-11, prec),
				new BigFixedPointElem<T>(0.00031E-11, prec)));
	}

	/**
	 * Gets the Newtonian Constant of Gravitation, <math display="inline">
     * <mrow>
     * <mi>G</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getG() {
		return (G);
	}

	
	/**
	 * Boltzmann Constant, <math display="inline">
     * <mrow>
     * <mi>k</mi>
     * </mrow>
     * </math>.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> K;

	/**
	 * Calculates the Boltzmann Constant, <math display="inline">
     * <mrow>
     * <mi>k</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcK() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(1.38064852E-23, prec),
				new BigFixedPointElem<T>(0.00000079E-23, prec)));
	}

	/**
	 * Gets the Boltzmann Constant, <math display="inline">
     * <mrow>
     * <mi>k</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getK() {
		return (K);
	}

	
	/**
	 * Elementary Charge (the charge of the electron <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>)
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> Q_E;

	/**
	 * Calculates the Elementary Charge (the charge of the electron <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>)
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcQ_E() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(1.6021766208E-19, prec),
				new BigFixedPointElem<T>(0.0000000098E-19, prec)));
	}

	/**
	 * Gets the Elementary Charge (the charge of the electron <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>)
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getQ_E() {
		return (Q_E);
	}

	
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> M_E;

	/**
	 * Calculates the Electron Mass, <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>m</mi>
     *   <mi>e</mi>
     *  </msub>
     * </mrow>
     * </math>
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcM_E() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(9.10938356E-31, prec),
				new BigFixedPointElem<T>(0.00000011E-31, prec)));
	}

	/**
	 * Gets the Electron Mass, <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>m</mi>
     *   <mi>e</mi>
     *  </msub>
     * </mrow>
     * </math>
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getM_E() {
		return (M_E);
	}

	
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> M_P;

	/**
	 * Calculates the Proton Mass, <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>m</mi>
     *   <mi>p</mi>
     *  </msub>
     * </mrow>
     * </math>
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcM_P() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(1.672621898E-27, prec),
				new BigFixedPointElem<T>(0.000000021E-27, prec)));
	}

	/**
	 * Gets the Proton Mass, <math display="inline">
     * <mrow>
     *  <msub>
     *     <mi>m</mi>
     *   <mi>p</mi>
     *  </msub>
     * </mrow>
     * </math>
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getM_P() {
		return (M_P);
	}

	// Standard numeric constants. Uncertainty represents the accuracy of the
	// JVM-defined constants.

	
	/**
	 * <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> PI;

	/**
	 * Calculates <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcPi() {
		
		final BigInteger TWO = BigInteger.valueOf( 2 );
		
		int numBits = 1;
		BigInteger bitChk = TWO;
		
		while( bitChk.compareTo( prec.getVal() ) <= 0 )
		{
			bitChk = bitChk.multiply( TWO );
			numBits ++;
		}
		
		numBits = numBits * 2 + 8;
		
		final PiDigits pid = new PiDigits();
		
		pid.piDigits( numBits );
		
		final BigInteger initVal = pid.getPiNumer().multiply( prec.getVal() ).divide( pid.getPiDenom() );
		
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>( initVal , prec ),
				new BigFixedPointElem<T>( BigInteger.ONE , prec )));
	}

	/**
	 * Gets <math display="inline">
     * <mrow>
     * <mi>&pi;</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getPi() {
		return (PI);
	}

	
	/**
	 * The natural exponent, <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> E;

	/**
	 * Calculates the natural exponent, <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcE() {
		
		final BigInteger TEN = BigInteger.valueOf( 10 );
		
		int numDigits = 1;
		BigInteger digChk = TEN;
		
		while( digChk.compareTo( prec.getVal() ) <= 0 )
		{
			digChk = digChk.multiply( TEN );
			numDigits++;
		}
		
		numDigits = numDigits * 2 + 8;
		
		final EDigits ed = new EDigits();
		
		ed.eDigits( numDigits );
		
		final BigInteger initVal = ed.getENumer().multiply( prec.getVal() ).divide( ed.getEDenom() );
		
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>( initVal , prec ),
				new BigFixedPointElem<T>( BigInteger.ONE , prec )));
	}

	/**
	 * Gets the natural exponent, <math display="inline">
     * <mrow>
     * <mi>e</mi>
     * </mrow>
     * </math>.
	 * @return The Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getE() {
		return (E);
	}

	
	/**
	 * The value of the number four, used to calculate other constants.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> FOUR;

	/**
	 * Calculates the value of the number four, used to calculate other constants.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcFour() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(prec.getVal().multiply(
						BigInteger.valueOf(4)), prec),
				new BigFixedPointElem<T>(BigInteger.ZERO, prec)));
	}

	
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> EM7;

	/**
	 * Calculates the value of the number <math display="inline">
     * <mrow>
     *  <msup>
     *     <mn>10</mn>
     *   <mn>-7</mn>
     *  </msup>
     * </mrow>
     * </math>, used to calculate other constants.
	 * @return The Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcEM7() {
		return (new ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>>(
				new BigFixedPointElem<T>(prec.getVal().divide(
						BigInteger.valueOf(10000000L)), prec),
				new BigFixedPointElem<T>(BigInteger.ZERO, prec)));
	}

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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcEpsilon_g() {
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> EPSILON_0G;

	/**
	 * Gets the Gravitational Permitivity  <math display="inline">
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
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getEpsilon_g() {
		return (EPSILON_0G);
	}

	
	
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcMu_0() {
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> MU_0;

	/**
	 * Gets the Magnetic Permeability  <math display="inline">
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
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getMu_0() {
		return (MU_0);
	}

	
	
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcMu_g() {
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> MU_0G;

	/**
	 * Gets the Gravitomagnetic Permeability  <math display="inline">
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
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getMu_g() {
		return (MU_0G);
	}

	
	
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcEpsilon_0() {
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
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> EPSILON_0;

	/**
	 * Gets the Electrical Permitivity  <math display="inline">
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
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getEpsilon_0() {
		return (EPSILON_0);
	}

	/**
	 * Calculates the Coulomb Constant.
	 * 
	 * @return The Coulomb Constant.
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> calcCoul() {
		try {
			return ((PI.mult(FOUR).mult(EPSILON_0)).invertLeft());
		} catch (NotInvertibleException ex) {
			throw (new RuntimeException("Failed"));
		}
	}

	/**
	 * Coulomb Constant
	 */
	protected ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> COUL;

	/**
	 * Gets the Coulomb Constant.
	 * 
	 * @return The Coulomb Constant.
	 */
	public ValueWithUncertaintyElem<BigFixedPointElem<T>, BigFixedPointElemFactory<T>> getCoul() {
		return (COUL);
	}

	
}
