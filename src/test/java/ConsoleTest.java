import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Console
 * 
 * @author Krish Arora
 */
class ConsoleTest {
    @Test
    void emptyInput() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("", inputRunner.run(""));
    }

    @Test
    void exit() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("Goodbye!", inputRunner.run("exit"));
    }

    @Test
    void inputVariable() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("a", inputRunner.run("a"));
    }
}
