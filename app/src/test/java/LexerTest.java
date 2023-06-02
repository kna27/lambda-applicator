import org.junit.jupiter.api.Test;

import lambda.Lexer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class tests the Lexer
 * 
 * @author Krish Arora
 */
class LexerTest {
    @Test
    void commentOnly() {
        Lexer lexer = new Lexer();
        assertEquals(new ArrayList<String>(), lexer.tokenize("; comment"));
    }

    @Test
    void commentAfter() {
        Lexer lexer = new Lexer();
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("a!"));
        assertEquals(expected, lexer.tokenize("a!; comment"));
    }

    @Test
    void lambda() {
        Lexer lexer = new Lexer();
        ArrayList<String> expected = new ArrayList<>(Arrays.asList("\\", "a", ".", "a"));
        assertEquals(expected, lexer.tokenize("λa.a"));
    }

    @Test
    void complexLambda() {
        Lexer lexer = new Lexer();
        ArrayList<String> expected = new ArrayList<>(
                Arrays.asList("(", "(", "cu", "(", "a", "\\", "boo", ".", "a", "boo", ")", ")", ")", "day"));
        assertEquals(expected, lexer.tokenize("((cu (a λboo.a boo) )) day"));
    }

    @Test
    void lambdaWithCommentAndSpacing() {
        Lexer lexer = new Lexer();
        ArrayList<String> expected = new ArrayList<>(
                Arrays.asList("(", "(", "\\", "bat", ".", "bat", "flies", ")", "cat", ")"));
        assertEquals(expected, lexer.tokenize("(    (\\bat .bat flies)cat );    comment"));
    }
}
