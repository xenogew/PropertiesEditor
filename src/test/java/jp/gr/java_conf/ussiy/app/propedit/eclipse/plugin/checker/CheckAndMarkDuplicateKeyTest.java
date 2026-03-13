package jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.checker;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class CheckAndMarkDuplicateKeyTest {

	@Nested
	class ExtractKeys {

		@Test
		void simpleKeyEqualsValue() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key=value\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
			assertEquals(List.of(1), result.get("key"));
		}

		@Test
		void keyColonValue() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key:value\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
		}

		@Test
		void keySpaceValue() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key value\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
		}

		@Test
		void keyTabValue() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key\tvalue\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
		}

		@Test
		void multipleDistinctKeys() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("a=1\nb=2\nc=3\n");
			assertEquals(3, result.size());
			assertTrue(result.containsKey("a"));
			assertTrue(result.containsKey("b"));
			assertTrue(result.containsKey("c"));
		}

		@Test
		void duplicateKeys() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key=val1\nkey=val2\n");
			assertEquals(1, result.size());
			assertEquals(List.of(1, 2), result.get("key"));
		}

		@Test
		void triplicateKeys() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key=a\nkey=b\nkey=c\n");
			assertEquals(List.of(1, 2, 3), result.get("key"));
		}

		@Test
		void commentLinesSkipped() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("# comment\nkey=val\n! another comment\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
			assertEquals(List.of(2), result.get("key"));
		}

		@Test
		void blankLinesSkipped() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("\nkey=val\n\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
		}

		@Test
		void escapedSeparatorInKey() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key\\=name=value\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key=name"));
		}

		@Test
		void escapedColonInKey() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key\\:name=value\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key:name"));
		}

		@Test
		void multiLineValue() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("key=long\\\nvalue\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
		}

		@Test
		void emptyInput() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("");
			assertTrue(result.isEmpty());
		}

		@Test
		void onlyComments() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("# comment1\n! comment2\n");
			assertTrue(result.isEmpty());
		}

		@Test
		void keyWithNoValue() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("keyonly\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("keyonly"));
		}

		@Test
		void leadingWhitespace() throws IOException {
			var result = CheckAndMarkDuplicateKey.extractKeys("  key=value\n");
			assertEquals(1, result.size());
			assertTrue(result.containsKey("key"));
		}
	}

	@Nested
	class Replace {

		@Test
		void basicReplacement() {
			assertEquals("Key 'hello' is duplicated",
					CheckAndMarkDuplicateKey.replace("Key '$key$' is duplicated", "hello", "$key$"));
		}

		@Test
		void noMatch() {
			assertEquals("no match here",
					CheckAndMarkDuplicateKey.replace("no match here", "replacement", "missing"));
		}
	}
}
