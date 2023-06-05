import org.junit.jupiter.api.Test;

import lambda.Parser;

import static org.junit.jupiter.api.Assertions.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class tests the preParse method in the Parser
 * 
 * @author Krish Arora
 */
class PreParserTest {
    @Test
    void noLambdas() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("a"));
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("a"));

        Parser parser = new Parser(tokens);
        try {
            parser.preParse();
            assertEquals(expected, parser.tokens);
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void lambda() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("a", "\\", "b", ".", "c", "d"));
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("a", "(", "\\", "b", ".", "c", "d", ")"));

        Parser parser = new Parser(tokens);
        try {
            parser.preParse();
            assertEquals(expected, parser.tokens);
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void alreadySurroundedLambda() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("a", "(", "\\", "b", ".", "c", "d", ")", "e"));
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("a", "(", "\\", "b", ".", "c", "d", ")", "e"));

        Parser parser = new Parser(tokens);
        try {
            parser.preParse();
            assertEquals(expected, parser.tokens);
        } catch (ParseException e) {
            assertTrue(false);
        }
    }

    @Test
    void lambdaBlockedByParenOutsideScope() {
        ArrayList<String> tokens = new ArrayList<>(Arrays.asList("(", "a", "\\", "b", ".", "c", "d", ")", "e", ")"));
        ArrayList<String> expected = new ArrayList<>(
                Arrays.asList("(", "a", "(", "\\", "b", ".", "c", "d", ")", ")", "e", ")"));

        Parser parser = new Parser(tokens);
        try {
            parser.preParse();
            assertEquals(expected, parser.tokens);
        } catch (ParseException e) {
            assertTrue(false);
        }
    }
}
