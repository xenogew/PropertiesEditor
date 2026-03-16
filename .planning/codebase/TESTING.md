# Testing Patterns

**Analysis Date:** 2025-03-15

## Test Framework

**Runner:**
- JUnit Jupiter (JUnit 5) version 5.11.4.
- Config: Managed in root `pom.xml` via `dependencyManagement`.

**Assertion Library:**
- JUnit 5 standard assertions: `org.junit.jupiter.api.Assertions`.

**Run Commands:**
```bash
./mvnw test              # Run all tests
./mvnw surefire:test     # Run unit tests specifically
```

## Test File Organization

**Location:**
- Separate test source folder: `bundles/io.github.xenogew.propedit/src/test/java/`.
- Mirrors the package structure of `src/main/java` (though main source is actually in `src/`).

**Naming:**
- Files: `[ClassName]Test.java` (e.g., `StringUtilTest.java`).
- Methods: Descriptive snake_case or camelCase: `simpleKeyEqualsValue`, `escapedColonInKey`.

**Structure:**
```
bundles/io.github.xenogew.propedit/src/test/java/
└── io/github/xenogew/propedit/
    ├── util/
    │   └── StringUtilTest.java
    └── eclipse/plugin/checker/
        └── CheckAndMarkDuplicateKeyTest.java
```

## Test Structure

**Suite Organization:**
```java
class ExampleTest {
  @Nested
  class MethodUnderTest {
    @Test
    void expectedBehavior() {
      // test logic
    }
  }
}
```
- Extensive use of `@Nested` to group tests by method or functionality.

**Patterns:**
- **Setup:** Limited use of `@BeforeEach` in observed files; many tests are self-contained.
- **Assertion:** Standard `assertEquals`, `assertTrue`, `assertNull`.

## Mocking

**Framework:** Not explicitly detected in `pom.xml` dependencies (Mockito/Easymock absent). Tests appear to use real objects or static utility calls.

## Fixtures and Factories

**Test Data:**
- Strings and basic objects are instantiated directly in test methods.
- Example: `CheckAndMarkDuplicateKey.extractKeys("key=value\n")`.

**Location:**
- Inline within test methods.

## Coverage

**Requirements:** None enforced in `pom.xml`.

**View Coverage:**
- Not configured in Maven; likely handled via Eclipse IDE integration.

## Test Types

**Unit Tests:**
- Focus on utility classes (`StringUtil`) and logic components (`CheckAndMarkDuplicateKey`).

**Integration Tests:**
- Limited; Tycho is used for building, but `tycho-surefire-plugin` (for OSGi integration tests) is configured to run unit tests in the current setup.

## Common Patterns

**Async Testing:**
- Not observed in current test suite.

**Error Testing:**
- `assertThrows` or `throws IOException` in method signatures.
```java
@Test
void simpleKeyEqualsValue() throws IOException { ... }
```

---

*Testing analysis: 2025-03-15*
