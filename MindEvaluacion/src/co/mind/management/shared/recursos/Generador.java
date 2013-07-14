package co.mind.management.shared.recursos;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Generador {

	public static final char[] CARACTERES = { 'a', 'b', 'c', 'd', 'e', 'f',
			'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
			't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F',
			'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
			'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9' };

	/**
	 * Generates a password according to the given parameters.
	 * 
	 * @param characters
	 *            that make up the password
	 * @param length
	 *            of the password
	 * @return the generated password
	 */
	public static String GenerarCodigo(final char[] caracteres,
			final int longitud) {
		String resultado = "";
		Random aleatorio = new Random();
		for (int i = 0; i < longitud; i++) {
			int a = aleatorio.nextInt(caracteres.length);
			resultado += caracteres[a];
		}
		return resultado;
	}

	public static String convertirStringmd5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {

			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}
}