package jp.gr.java_conf.ussiy.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class StringUtilTest {

	@Nested
	class RemoveCarriageReturn {

		@Test
		void removesWindowsLineEndings() {
			assertEquals("line1\nline2\n", StringUtil.removeCarriageReturn("line1\r\nline2\r\n"));
		}

		@Test
		void removesStandaloneCR() {
			assertEquals("ab", StringUtil.removeCarriageReturn("a\rb"));
		}

		@Test
		void noCarriageReturn() {
			assertEquals("hello\nworld", StringUtil.removeCarriageReturn("hello\nworld"));
		}

		@Test
		void emptyString() {
			assertEquals("", StringUtil.removeCarriageReturn(""));
		}
	}

	@Nested
	class EscapeHtml {

		@Test
		void escapesAmpersand() {
			assertEquals("a&amp;b", StringUtil.escapeHtml("a&b"));
		}

		@Test
		void escapesLessThan() {
			assertEquals("a&lt;b", StringUtil.escapeHtml("a<b"));
		}

		@Test
		void escapesGreaterThan() {
			assertEquals("a&gt;b", StringUtil.escapeHtml("a>b"));
		}

		@Test
		void escapesDoubleQuote() {
			assertEquals("a&quot;b", StringUtil.escapeHtml("a\"b"));
		}

		@Test
		void nullInput() {
			assertNull(StringUtil.escapeHtml(null));
		}

		@Test
		void combinedSpecialChars() {
			assertEquals("&lt;a href=&quot;x&quot;&gt;A &amp; B&lt;/a&gt;",
					StringUtil.escapeHtml("<a href=\"x\">A & B</a>"));
		}
	}
}
