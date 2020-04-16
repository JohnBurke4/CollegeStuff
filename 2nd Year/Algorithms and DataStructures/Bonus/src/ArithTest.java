import org.junit.Test;

import static org.junit.Assert.*;

public class ArithTest {

    @Test
    public void validatePrefixOrderTest() {
        String[] empty = {};
        String[] oneInt = {"1"};
        String[] oneOp = {"/"};
        String[] trueExp1 = {"+", "1", "2"};
        String[] trueExp2 = {"-", "*", "/", "3", "100", "10", "5"};
        String[] falseExp1 = {"-", "*", "/", "3", "100", "10", "5", "-"};
        String[] falseExp2 = {"+", "2", "3", "-", "4"};

        assertFalse(Arith.validatePrefixOrder(empty));
        assertTrue(Arith.validatePrefixOrder(oneInt));
        assertFalse(Arith.validatePrefixOrder(oneOp));

        assertTrue(Arith.validatePrefixOrder(trueExp1));
        assertTrue(Arith.validatePrefixOrder(trueExp2));

        assertFalse(Arith.validatePrefixOrder(falseExp1));
        assertFalse(Arith.validatePrefixOrder(falseExp2));
    }

    @Test
    public void validatePostfixOrderTest() {
        String[] empty = {};
        String[] oneInt = {"1"};
        String[] oneOp = {"/"};
        String[] trueExp1 = {"1", "2", "+"};
        String[] trueExp2 = {"1", "2", "/", "10", "*", "100", "-"};
        String[] falseExp1 = {"-", "*", "/", "3", "100", "10", "5", "-"};
        String[] falseExp2 = {"+", "2", "3", "-", "4"};

        assertFalse(Arith.validatePostfixOrder(empty));
        assertTrue(Arith.validatePostfixOrder(oneInt));
        assertFalse(Arith.validatePostfixOrder(oneOp));

        assertTrue(Arith.validatePostfixOrder(trueExp1));
        assertTrue(Arith.validatePostfixOrder(trueExp2));

        assertFalse(Arith.validatePostfixOrder(falseExp1));
        assertFalse(Arith.validatePostfixOrder(falseExp2));
    }

    @Test
    public void evaluatePrefixOrderTest() {
        String[] oneInt = {"1"};
        String[] trueExp1 = {"+", "1", "2"};
        String[] trueExp2 = {"-", "*", "/", "3", "100", "10", "5"};


        assertTrue(Arith.validatePrefixOrder(oneInt));
        assertTrue(Arith.validatePrefixOrder(trueExp1));
        assertTrue(Arith.validatePrefixOrder(trueExp2));

        assertEquals(1, Arith.evaluatePrefixOrder(oneInt));
        assertEquals(3, Arith.evaluatePrefixOrder(trueExp1));
        assertEquals(-5, Arith.evaluatePrefixOrder(trueExp2));
    }

    @Test
    public void evaluatePostfixOrderTest() {
        String[] oneInt = {"1"};
        String[] trueExp1 = {"1", "2", "+"};
        String[] trueExp2 = {"1", "2", "/", "10", "*", "100", "-"};
        assertTrue(Arith.validatePostfixOrder(oneInt));
        assertTrue(Arith.validatePostfixOrder(trueExp1));
        assertTrue(Arith.validatePostfixOrder(trueExp2));

        assertEquals(1, Arith.evaluatePostfixOrder(oneInt));
        assertEquals(3, Arith.evaluatePostfixOrder(trueExp1));
        assertEquals(-100, Arith.evaluatePostfixOrder(trueExp2));

    }

    @Test
    public void convertPrefixToInfixTest() {
        String[] oneInt = {"1"};
        String[] exp1 = {"1"};
        String[] trueExp1 = {"+", "1", "2"};
        String[] exp2 = {"(", "1", "+", "2", ")"};
        String[] trueExp2 = {"-", "*", "/", "3", "100", "10", "5"};
        String[] exp3 = {"(", "(", "(", "3", "/", "100", ")", "*", "10", ")", "-", "5", ")"};

        assertArrayEquals(exp1, Arith.convertPrefixToInfix(oneInt));
        assertArrayEquals(exp2, Arith.convertPrefixToInfix(trueExp1));
        assertArrayEquals(exp3, Arith.convertPrefixToInfix(trueExp2));
    }

    @Test
    public void convertPostfixToInfixTest() {
        String[] oneInt = {"1"};
        String[] exp1 = {"1"};
        String[] trueExp1 = {"1", "2", "+"};
        String[] exp2 = {"(", "1", "+", "2", ")"};
        String[] trueExp2 = {"1", "2", "/", "10", "*", "100", "-"};
        String[] exp3 = {"(", "(", "(", "1", "/", "2", ")", "*", "10", ")", "-", "100", ")"};

        assertArrayEquals(exp1, Arith.convertPostfixToInfix(oneInt));
        assertArrayEquals(exp2, Arith.convertPostfixToInfix(trueExp1));
        assertArrayEquals(exp3, Arith.convertPostfixToInfix(trueExp2));
    }

    @Test
    public void convertInfixToPostfixTest(){
        String[] test1 = {"1"};
        String[] test2 = {"1", "+", "2"};
        String[] test3 = {"(", "(", "(", "1", "/", "2", ")", "*", "10", ")", "-", "100", ")"};

        String[] exp1 = {"1"};
        String[] exp2 = {"1", "2", "+"};
        String[] exp3 = {"1", "2", "/", "10", "*", "100", "-"};

        assertArrayEquals(exp1, Arith.convertInfixToPostfix(test1));
        assertArrayEquals(exp2, Arith.convertInfixToPostfix(test2));
        assertArrayEquals(exp3, Arith.convertInfixToPostfix(test3));
    }

    @Test
    public void convertPostfixToPrefix(){
        String[] test1 = {"1"};
        String[] test2 = {"1", "2", "+"};
        String[] test3 = {"1", "2", "/", "10", "*", "100", "-"};

        String[] exp1 = {"1"};
        String[] exp2 = {"+", "1", "2"};
        String[] exp3 = {"-", "*", "/", "1", "2", "10", "100"};


        assertArrayEquals(exp1, Arith.convertPostfixToPrefix(test1));
        assertArrayEquals(exp2, Arith.convertPostfixToPrefix(test2));
        assertArrayEquals(exp3, Arith.convertPostfixToPrefix(test3));
    }

    @Test
    public void convertPrefixToPostfix(){
        String[] exp1 = {"1"};
        String[] exp2 = {"1", "2", "+"};
        String[] exp3 = {"1", "2", "/", "10", "*", "100", "-"};

        String[] test1 = {"1"};
        String[] test2 = {"+", "1", "2"};
        String[] test3 = {"-", "*", "/", "1", "2", "10", "100"};


        assertArrayEquals(exp1, Arith.convertPrefixToPostfix(test1));
        assertArrayEquals(exp2, Arith.convertPrefixToPostfix(test2));
        assertArrayEquals(exp3, Arith.convertPrefixToPostfix(test3));
    }
}