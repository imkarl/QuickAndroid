package from.java.nio.charset;

import java.nio.charset.Charset;

/**
 * Contains constant definitions for the six standard {@link Charset} instances,
 * which are guaranteed to be supported by all Java platform implementations.
 * 
 * <p>
 * Assuming you're free to choose, note that <b>{@link #UTF_8} is widely
 * preferred</b>.
 * 
 * <p>
 * See the Guava User Guide article on <a href=
 * "http://code.google.com/p/guava-libraries/wiki/StringsExplained#Charsets">
 * {@code Charsets}</a>.
 * 
 * @author Mike Bostock
 * @since 1.0
 */
public class Charsets {
	private Charsets() {
	}

	/**
	 * US-ASCII: seven-bit ASCII, the Basic Latin block of the Unicode character
	 * set (ISO646-US).
	 * 
	 * <p>
	 * <b>Note for Java 7 and later:</b> this constant should be treated as
	 * deprecated; use {@link java.nio.charset.StandardCharsets#US_ASCII}
	 * instead.
	 * 
	 */
	public static final Charset US_ASCII = Charset.forName("US-ASCII");

	/**
	 * ISO-8859-1: ISO Latin Alphabet Number 1 (ISO-LATIN-1).
	 * 
	 * <p>
	 * <b>Note for Java 7 and later:</b> this constant should be treated as
	 * deprecated; use {@link java.nio.charset.StandardCharsets#ISO_8859_1}
	 * instead.
	 * 
	 */
	public static final Charset ISO_8859_1 = Charset.forName("ISO-8859-1");

	/**
	 * UTF-8: eight-bit UCS Transformation Format.
	 * 
	 * <p>
	 * <b>Note for Java 7 and later:</b> this constant should be treated as
	 * deprecated; use {@link java.nio.charset.StandardCharsets#UTF_8} instead.
	 * 
	 */
	public static final Charset UTF_8 = Charset.forName("UTF-8");

	/**
	 * UTF-16BE: sixteen-bit UCS Transformation Format, big-endian byte order.
	 * 
	 * <p>
	 * <b>Note for Java 7 and later:</b> this constant should be treated as
	 * deprecated; use {@link java.nio.charset.StandardCharsets#UTF_16BE}
	 * instead.
	 * 
	 */
	public static final Charset UTF_16BE = Charset.forName("UTF-16BE");

	/**
	 * UTF-16LE: sixteen-bit UCS Transformation Format, little-endian byte
	 * order.
	 * 
	 * <p>
	 * <b>Note for Java 7 and later:</b> this constant should be treated as
	 * deprecated; use {@link java.nio.charset.StandardCharsets#UTF_16LE}
	 * instead.
	 * 
	 */
	public static final Charset UTF_16LE = Charset.forName("UTF-16LE");

	/**
	 * UTF-16: sixteen-bit UCS Transformation Format, byte order identified by
	 * an optional byte-order mark.
	 * 
	 * <p>
	 * <b>Note for Java 7 and later:</b> this constant should be treated as
	 * deprecated; use {@link java.nio.charset.StandardCharsets#UTF_16} instead.
	 * 
	 */
	public static final Charset UTF_16 = Charset.forName("UTF-16");

    /**
     * Returns a new byte array containing the bytes corresponding to the given characters,
     * encoded in UTF-16BE. All characters are representable in UTF-16BE.
     */
    public static byte[] toBigEndianUtf16Bytes(char[] chars, int offset, int length) {
        byte[] result = new byte[length * 2];
        int end = offset + length;
        int resultIndex = 0;
        for (int i = offset; i < end; ++i) {
            char ch = chars[i];
            result[resultIndex++] = (byte) (ch >> 8);
            result[resultIndex++] = (byte) ch;
        }
        return result;
    }

}
