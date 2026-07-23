package lc.medium;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class MinimumRemoveToMakeValidParenthesesTest {

    private final MinimumRemoveToMakeValidParentheses solution =
            new MinimumRemoveToMakeValidParentheses();

    @Test
    void removesTheTrailingUnmatchedParenthesis() {
        assertEquals("lee(t(c)o)de", solution.minRemoveToMakeValid("lee(t(c)o)de)"));
    }

    @Test
    void removesTheLeadingUnmatchedParenthesis() {
        assertEquals("ab(c)d", solution.minRemoveToMakeValid("a)b(c)d"));
    }

    @Test
    void removesEveryParenthesisWhenNoneCanMatch() {
        assertEquals("", solution.minRemoveToMakeValid("))(("));
    }

    @Test
    void leavesAlreadyValidStringsUnchanged() {
        assertEquals("(a(b)c)", solution.minRemoveToMakeValid("(a(b)c)"));
    }

    @Test
    void producesValidBalancedOutput() {
        String result = solution.minRemoveToMakeValid("())()(((");
        assertTrue(isValid(result), "expected balanced result but got: " + result);
    }

    private boolean isValid(String s) {
        int open = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                open++;
            } else if (c == ')') {
                if (open == 0) {
                    return false;
                }
                open--;
            }
        }
        return open == 0;
    }
}
