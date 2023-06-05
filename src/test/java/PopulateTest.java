import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * This class tests the populate command that Populate the Church Numerals in a
 * specified rang
 * 
 * @author Krish Arora
 */
class PopulateTest {
    @Test
    void populateZeroToB() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("((populate 0) B)", inputRunner.run("populate 0 B"));
    }

    @Test
    void populateNegativeOneToThree() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("((populate -1) 3)", inputRunner.run("populate -1 3"));
    }

    @Test
    void populateZeroToThirty() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("Populated numbers 0 to 30", inputRunner.run("populate 0 30"));
        String fifteen = "(λf.(λx.(f (f (f (f (f (f (f (f (f (f (f (f (f (f (f x)))))))))))))))))";
        assertEquals(fifteen, inputRunner.run("15"));
    }
}
