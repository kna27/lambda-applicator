import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the Runner
 * 
 * @author Krish Arora
 */
class RunnerTest {
    @Test
    void runVariable() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("cat", inputRunner.run("run cat"));
    }

    @Test
    void runApplicationWithoutLambda() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("(x y)", inputRunner.run("run (x y)"));
    }

    @Test
    void runSingleFunction() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("(λx.(x y))", inputRunner.run("run \\x.x y"));
    }

    @Test
    void applicationWithFunctionOnLeft() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("y", inputRunner.run("run (\\x.x) y"));
    }

    @Test
    void runRedex() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("(((y y) y) (y y))", inputRunner.run("run (\\x.(x y x)) (y y)"));
    }

    @Test
    void runRedexWithSubFunctionWithSameParameter() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("(λx.x)", inputRunner.run("run (\\x. \\x . x) y"));
    }

    @Test
    void runRedexWithTwoLambdas() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("y", inputRunner.run("run (\\x . x y) (\\v.v)"));
    }

    @Test
    void deepCopyPassedInFunctions() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("((a (λv.a)) (b (λv.b)))", inputRunner.run("run (\\x.(x a) (x b)) (\\i.i (\\v. i))"));
    }

    @Test
    void runWhileSettingVariables() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("Added bounce as ball", inputRunner.run("ball = run (\\x.x) bounce"));
    }

    @Test
    void renameBoundVariableWhenFreeVariableIsSame() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("(λx1.(x1 x))", inputRunner.run("run (λy.λx.(x y)) x"));
    }

    @Test
    void renameBoundVariableWithMultipleVariables() {
        InputRunner inputRunner = new InputRunner();
        assertEquals("(λa1.(λr1.(((a r) a1) r1)))", inputRunner.run("run (λm.λr.(λb.λc.λa.λr.b c a r) m r) a r"));
    }
}
