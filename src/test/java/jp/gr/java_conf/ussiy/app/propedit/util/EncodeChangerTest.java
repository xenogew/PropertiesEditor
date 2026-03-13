package jp.gr.java_conf.ussiy.app.propedit.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import jp.gr.java_conf.ussiy.app.propedit.util.EncodeChanger;

class EncodeChangerTest {

	@Nested
	class Unicode2UnicodeEsc {

		@Test
		void asciiOnly() {
			assertEquals("Hello world", EncodeChanger.unicode2UnicodeEsc("Hello world"));
		}

		@Test
		void japaneseChars() {
			assertEquals("\\u3053\\u3093\\u306b\\u3061\\u306f", EncodeChanger.unicode2UnicodeEsc("\u3053\u3093\u306b\u3061\u306f"));
		}

		@Test
		void mixedAsciiAndUnicode() {
			assertEquals("key=\\u5024", EncodeChanger.unicode2UnicodeEsc("key=\u5024"));
		}

		@Test
		void emptyString() {
			assertEquals("", EncodeChanger.unicode2UnicodeEsc(""));
		}

		@Test
		void nullInput() {
			assertNull(EncodeChanger.unicode2UnicodeEsc(null));
		}

		@Test
		void uppercaseMode() {
			assertEquals("\\u3053", EncodeChanger.unicode2UnicodeEsc("\u3053", EncodeChanger.UPPERCASE));
		}

		@Test
		void lowercaseMode() {
			assertEquals("\\u3053", EncodeChanger.unicode2UnicodeEsc("\u3053", EncodeChanger.LOWERCASE));
		}

		@Test
		void hexPaddingWithLeadingZeros() {
			// Character \u00E9 (é) — hex E9, needs 2 leading zeros
			assertEquals("\\u00e9", EncodeChanger.unicode2UnicodeEsc("\u00E9"));
		}
	}

	@Nested
	class UnicodeEsc2Unicode {

		@Test
		void validLowercaseEscape() {
			assertEquals("\u3053\u3093\u306b\u3061\u306f", EncodeChanger.unicodeEsc2Unicode("\\u3053\\u3093\\u306b\\u3061\\u306f"));
		}

		@Test
		void validUppercaseU() {
			assertEquals("\u3053", EncodeChanger.unicodeEsc2Unicode("\\U3053"));
		}

		@Test
		void invalidHexAfterU() {
			// backslash-u followed by invalid hex — should keep the backslash literally
			assertEquals("\\uZZZZ", EncodeChanger.unicodeEsc2Unicode("\\uZZZZ"));
		}

		@Test
		void partialEscape() {
			// backslash-u followed by only 2 hex digits — not enough for a full escape
			assertEquals("\\u30", EncodeChanger.unicodeEsc2Unicode("\\u30"));
		}

		@Test
		void emptyString() {
			assertEquals("", EncodeChanger.unicodeEsc2Unicode(""));
		}

		@Test
		void nullInput() {
			assertNull(EncodeChanger.unicodeEsc2Unicode(null));
		}

		@Test
		void mixedAsciiAndEscapes() {
			assertEquals("key=\u5024", EncodeChanger.unicodeEsc2Unicode("key=\\u5024"));
		}

		@Test
		void backslashNotFollowedByU() {
			assertEquals("\\n", EncodeChanger.unicodeEsc2Unicode("\\n"));
		}
	}

	@Nested
	class RoundTrip {

		@Test
		void japaneseRoundTrip() {
			var original = "\u3053\u3093\u306b\u3061\u306f\u4e16\u754c";
			assertEquals(original, EncodeChanger.unicodeEsc2Unicode(EncodeChanger.unicode2UnicodeEsc(original)));
		}

		@Test
		void asciiRoundTrip() {
			var original = "Hello world 123 !@#";
			assertEquals(original, EncodeChanger.unicodeEsc2Unicode(EncodeChanger.unicode2UnicodeEsc(original)));
		}

		@Test
		void mixedRoundTrip() {
			var original = "greeting=\u3053\u3093\u306b\u3061\u306f";
			assertEquals(original, EncodeChanger.unicodeEsc2Unicode(EncodeChanger.unicode2UnicodeEsc(original)));
		}
	}

	@Nested
	class Unicode2UnicodeEscWithoutComment {

		@Test
		void commentLinesPreserved() throws IOException {
			var input = "# This is a comment with \u3053\u3093\u306b\u3061\u306f\nkey=\u5024\n";
			var result = EncodeChanger.unicode2UnicodeEscWithoutComment(input);
			assertTrue(result.startsWith("# This is a comment with \u3053\u3093\u306b\u3061\u306f\n"));
			assertTrue(result.contains("key=\\u5024\n"));
		}

		@Test
		void exclamationCommentPreserved() throws IOException {
			var input = "! comment \u3053\nkey=\u5024\n";
			var result = EncodeChanger.unicode2UnicodeEscWithoutComment(input);
			assertTrue(result.startsWith("! comment \u3053\n"));
			assertTrue(result.contains("key=\\u5024\n"));
		}

		@Test
		void continuationLineConverted() throws IOException {
			var input = "key=\u5024\\\ncontinued\u5024\n";
			var result = EncodeChanger.unicode2UnicodeEscWithoutComment(input);
			assertTrue(result.contains("key=\\u5024\\\n"));
			assertTrue(result.contains("continued\\u5024\n"));
		}

		@Test
		void noTrailingNewline() throws IOException {
			var input = "key=\u5024";
			var result = EncodeChanger.unicode2UnicodeEscWithoutComment(input);
			assertEquals("key=\\u5024", result);
		}
	}
}
