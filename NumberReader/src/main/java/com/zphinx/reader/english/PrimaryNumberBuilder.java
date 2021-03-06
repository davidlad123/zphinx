/**
 * PrimaryNumberBuilder.java
 * @author David Ladapo (davidl@zphinx.com)
 * @version  1.0
 * 
 * Copyright &copy;Zphinx Software Solutions
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  License for more details.
 *
 * THERE IS NO WARRANTY FOR THIS SOFTWARE, TO THE EXTENT PERMITTED BY
 * APPLICABLE LAW.  EXCEPT WHEN OTHERWISE STATED IN WRITING BY ZPHINX SOFTWARE SOLUTIONS 
 * AND/OR OTHER PARTIES WHO PROVIDE THIS SOFTWARE "AS IS" WITHOUT WARRANTY
 * OF ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.  THE ENTIRE RISK AS TO THE QUALITY AND PERFORMANCE OF THE PROGRAM
 * IS WITH YOU.  SHOULD THE PROGRAM PROVE DEFECTIVE, YOU ASSUME THE COST OF
 * ALL NECESSARY SERVICING, REPAIR OR CORRECTION.
 *
 * IN NO EVENT UNLESS REQUIRED BY APPLICABLE LAW OR AGREED TO IN WRITING
 * WILL ANY COPYRIGHT HOLDER, OR ANY OTHER PARTY WHO MODIFIES AND/OR CONVEYS
 * THE PROGRAM AS PERMITTED ABOVE, BE LIABLE TO YOU FOR DAMAGES, INCLUDING ANY
 * GENERAL, SPECIAL, INCIDENTAL OR CONSEQUENTIAL DAMAGES ARISING OUT OF THE
 * USE OR INABILITY TO USE THE PROGRAM (INCLUDING BUT NOT LIMITED TO LOSS OF
 * DATA OR DATA BEING RENDERED INACCURATE OR LOSSES SUSTAINED BY YOU OR THIRD
 * PARTIES OR A FAILURE OF THE PROGRAM TO OPERATE WITH ANY OTHER PROGRAMS),
 * EVEN IF SUCH HOLDER OR OTHER PARTY HAS BEEN ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGES.
 *
 * For further information, please go to http://www.zphinx.co.uk/
 *
 **/

package com.zphinx.reader.english;

import com.zphinx.reader.core.AbstractNumberBuilder;
import com.zphinx.reader.english.enums.DistinctNumbers;
import com.zphinx.reader.english.translator.TranslatorConstants;
import com.zphinx.reader.exception.NumberReaderException;

/**
 * The primary number builder serves as an adaptee which is used to build the
 * vocal representations of the primary numbers i.e numbers from 1-1000
 * 
 * @author David Ladapo
 * @version $Id: PrimaryNumberBuilder.java 219 2012-07-13 22:03:40Z rogue $
 * 
 *          <p>
 *          Created: Jul 10, 2012 06:53:12 PM<br>
 *          Copyright &copy;Zphinx Software Solutions
 *          </p>
 * 
 */
public class PrimaryNumberBuilder extends AbstractNumberBuilder {

	/**
	 * Takes a Number from 100-999 and translates it into english words.
	 * 
	 * @param num
	 *            The String representation of the number to translate.
	 * @return The translated number expressed as english words.
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 */
	protected final String translateHundreds(final String num) throws NumberReaderException {
		final int numLen = num.length();
		validateNumberLength(numLen, TranslatorConstants.CONSTANT_3, TranslatorConstants.NUMBER_100);
		final StringBuilder sBuilder = processHundreds(num);
		// parse the remainder
		final String tens = num.substring(TranslatorConstants.CONSTANT_1, numLen);
		final String tensTranslated = translateTens(tens);
		sBuilder.append(tensTranslated);
		return sBuilder.toString();
	}

	/**
	 * Processes numbers in the hundreds
	 * 
	 * @param num
	 *            The String representation of the number to translate.
	 * @return A StringBuilder which contains the results of this process
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 */
	private StringBuilder processHundreds(final String num) throws NumberReaderException {
		final StringBuilder sBuilder = new StringBuilder();
		try {
			parseHundreds(num, sBuilder);
			checkLastNumbers(num, sBuilder);

		} catch (NumberFormatException e) {
			throw new NumberReaderException(e);
		}
		return sBuilder;
	}

	/**
	 * Parses a number in the hundreds
	 * 
	 * @param num
	 *            The additional string to append.
	 * @param sBuilder
	 *            The StringBuilder which contains extra information.
	 */
	private void parseHundreds(final String num, final StringBuilder sBuilder) {
		final int numVal = getNumberInt(num);
		if (numVal > TranslatorConstants.CONSTANT_0) {
			sBuilder.append(DistinctNumbers.findWord(numVal));
			sBuilder.append(trailingWord(TranslatorConstants.CONSTANT_0));

		}
	}

	/**
	 * Takes a Number from 10-99 and translates it into english words.
	 * 
	 * @param num
	 *            The String representation of the number to translate.
	 * @return The translated number expressed as english words.
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 */
	protected final String translateTens(final String num) throws NumberReaderException {
		validateNumberLength(num.length(), TranslatorConstants.CONSTANT_2, TranslatorConstants.NUMBER_MUST_BE_10);
		final StringBuilder sBuilder = processTens(num);
		return sBuilder.toString();
	}

	/**
	 * Processes numbers in the tens
	 * 
	 * @param num
	 *            The String representation of the number to translate.
	 * @return A StringBuilder which contains the results of this process
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 * */
	private StringBuilder processTens(final String num) throws NumberReaderException {
		final StringBuilder sBuilder = new StringBuilder();
		// parse the string
		try {
			final int numVal = getNumberInt(num);
			final String last = num.substring(TranslatorConstants.CONSTANT_1);
			sBuilder.append(parseTens(num, numVal, last));
		} catch (NumberFormatException e) {
			throw new NumberReaderException(e);
		}
		return sBuilder;
	}

	/**
	 * Parses translation data relating to numbers in the tens
	 * 
	 * @param num
	 *            The string representation of the number to parse
	 * @param numVal
	 *            A constant used to indicate the manner in which the number
	 *            should be parsed
	 * @param last
	 *            The last number in a given set of numbers
	 * @return A String which is the output of this process
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 */
	private String parseTens(final String num, final int numVal, final String last) throws NumberReaderException {
		final StringBuilder sBuilder = new StringBuilder();

		if (numVal == TranslatorConstants.CONSTANT_0 && !last.equals(TranslatorConstants.STRING_0)) {

			sBuilder.append(translateUnits(last));
		} else if (numVal == TranslatorConstants.CONSTANT_1) {

			int intNum = Integer.parseInt(num);
			sBuilder.append(DistinctNumbers.findWord(intNum));
		} else if (numVal == TranslatorConstants.CONSTANT_0 && last.equals(TranslatorConstants.STRING_0)) {

			sBuilder.append("");
		} else {

			parseTensDefault(numVal, last, sBuilder);
		}
		return sBuilder.toString();
	}

	/**
	 * Parses the default value for the tens translation parsing The string
	 * representation of the number to parse
	 * 
	 * @param numVal
	 *            A constant used to indicate the manner in which the number
	 *            should be parsed
	 * @param last
	 *            The last number in a given set of numbers
	 * @param sBuilder
	 *            The StringBuilder which saves the string generated
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 */
	private void parseTensDefault(final int numVal, final String last, final StringBuilder sBuilder) throws NumberReaderException {
		sBuilder.append(DistinctNumbers.findWord(numVal * TranslatorConstants.CONSTANT_10));
		if (!last.equals(TranslatorConstants.STRING_0)) {
			sBuilder.append("-");
			sBuilder.append(translateUnits(last));
		}
	}

	/**
	 * Takes a Number from 0-9 and translates it into english words
	 * 
	 * @param num
	 *            The String representation of the number to translate
	 * @return The translated number expressed as english words
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 * 
	 */
	protected final String translateUnits(final String num) throws NumberReaderException {
		validateNumberLength(num.length(), TranslatorConstants.CONSTANT_1, TranslatorConstants.NUMBER_LESS_10);
		final StringBuilder sBuilder = buildTens(num);
		return sBuilder.toString();
	}

	/**
	 * Builds the string representation for numbers in the tens i.e 10-90
	 * 
	 * @param num
	 *            The number whose string representation is to be stated
	 * @return A stringBuilder with the string representation as its contents
	 * @throws NumberReaderException
	 *             Throws a NumberReaderException if an error occurs
	 */
	private StringBuilder buildTens(final String num) throws NumberReaderException {
		final StringBuilder sBuilder = new StringBuilder();
		try {
			final int numVal = Integer.parseInt(num);
			sBuilder.append(DistinctNumbers.findWord(numVal));

		} catch (NumberFormatException e) {
			throw new NumberReaderException(e);
		}
		return sBuilder;
	}

	/**
	 * Parses the given number(0-999) based on the given index.Calls individual
	 * methods using the index given as a flag.
	 * 
	 * @see com.zphinx.reader.core.NumberBuilder#translateNumberGroup(int,
	 *      java.lang.String)
	 */

	public final String translateNumberGroup(final int index, final String number) throws NumberReaderException {
		String out = "";
		switch (index) {
		case TranslatorConstants.CONSTANT_1:
			out = translateUnits(number);
			break;
		case TranslatorConstants.CONSTANT_2:
			out = translateTens(number);
			break;
		case TranslatorConstants.CONSTANT_3:
			out = translateHundreds(number);
			break;
		default:
			break;
		}
		return out;
	}

}
