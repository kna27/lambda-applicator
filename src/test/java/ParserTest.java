import org.junit.jupiter.api.Test;

import lambda.Parser;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class tests the Parser
 * 
 * @author Krish Arora
 */
class ParserTest {
    @Test
    void emptyInput() {
        Parser parser = new Parser(new ArrayList<String>());
        try {
            assertEquals(null, parser.parse());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void singleVariable() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("a"));
        String expected = "a";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void twoVariables() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("a", "b"));
        String expected = "(a b)";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void orderOfOperationsForApplications() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        String expected = "(((a b) c) d)";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void recursivelyDetermineTopLevelArgs() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("a", "b", "c", "(", "d", "e", ")"));
        String expected = "(((a b) c) (d e))";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void stripExtraneousMatchingParens() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("(", "(", "(", "a", ")", ")", ")"));
        String expected = "a";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void stripExtraneousMatchingParensForMultipleExpressions() {
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("(", "(", "(", "a", ")", ")", ")", "(", "(", "(", "b", ")", ")", ")"));
        String expected = "(a b)";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void function() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("\\", "a", ".", "b"));
        String expected = "(λa.b)";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void functionWithFunctionAsBody() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("\\", "f", ".", "f", "x"));
        String expected = "(λf.(f x))";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void overrideOrderWithParens() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("(", "\\", "f", ".", "f", ")", "x"));
        String expected = "((λf.f) x)";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void stackExpressions() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("\\", "a", ".", "a", "b", "c", "d", "e"));
        String expected = "(λa.((((a b) c) d) e))";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void funcWithApplicationOfVariableAndFunc() {
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("\\", "a", ".", "a", "b", "c", "d", "e", "\\", "h", ".", "f", "g", "h", "i", "j"));
        String expected = "(λa.(((((a b) c) d) e) (λh.((((f g) h) i) j))))";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void overrideFuncWithApplicationOfVariableAndFuncWithParens() {
        ArrayList<String> tokens = new ArrayList<>(
                Arrays.asList("(", "\\", "a", ".", "a", "\\", "b", ".", "b", ")", "c"));
        String expected = "((λa.(a (λb.b))) c)";
        Parser parser = new Parser(tokens);
        try {
            assertEquals(expected, parser.parse().toString());
        } catch (ParseException e) {
            assertTrue(false);
        }

    }

    @Test
    void storeExpression() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("Added (λf.(λx.x)) as 0", inputRunner.run("0 = \\f.\\x.x"));
    }

    @Test
    void runStoredExpression() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("Added (λf.(λx.x)) as 0", inputRunner.run("0 = \\f.\\x.x"));
        assertEquals("(λf.(λx.x))", inputRunner.run("0"));
    }

    @Test
    void tryStoreDefinedExpression() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("Added (λf.(λx.x)) as 0", inputRunner.run("0 = \\f.\\x.x"));
        assertEquals("0 is already defined.", inputRunner.run("0 = \\a.b"));
    }

    @Test
    void storeExpressionWithStoredExpression() {
        InputRunner inputRunner = new InputRunner();
        String exp1 = "1 = λf.λx.f x";
        String exp2 = "succ = \\n.\\f.\\x.f (n f x)";
        String store = "2 = succ 1";

        String expected1 = "Added (λf.(λx.(f x))) as 1";
        String expected2 = "Added (λn.(λf.(λx.(f ((n f) x))))) as succ";
        String expected3 = "Added ((λn.(λf.(λx.(f ((n f) x))))) (λf.(λx.(f x)))) as 2";

        assertEquals(expected1, inputRunner.run(exp1));
        assertEquals(expected2, inputRunner.run(exp2));
        assertEquals(expected3, inputRunner.run(store));
    }

    @Test
    void returnVariableNameFromReducedExpression() {
        InputRunner inputRunner = new InputRunner();
        String in1 = "false = λf.λx.x";
        String in2 = "true = λx.λy.x";
        String in3 = "and = λp.λq.p q p";
        String in4 = "run and false true";

        String expected1 = "Added (λf.(λx.x)) as false";
        String expected2 = "Added (λx.(λy.x)) as true";
        String expected3 = "Added (λp.(λq.((p q) p))) as and";
        String expected4 = "false";

        assertEquals(expected1, inputRunner.run(in1));
        assertEquals(expected2, inputRunner.run(in2));
        assertEquals(expected3, inputRunner.run(in3));
        assertEquals(expected4, inputRunner.run(in4));
    }
}
