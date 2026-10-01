package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LeftPadUtilsTest {

    @Test
    void testLeftPad_NullString_ReturnsNull() {
        assertNull(LeftPadUtils.leftPad(null, 5, " "));
    }

    @Test
    void testLeftPad_SizeLessThanOrEqualToStringLength_ReturnsOriginalString() {
        assertEquals("hello", LeftPadUtils.leftPad("hello", 3, " "));
        assertEquals("hello", LeftPadUtils.leftPad("hello", 5, " "));
    }

    @Test
    void testLeftPad_NullPadStr_UsesSpace() {
        assertEquals("  hello", LeftPadUtils.leftPad("hello", 7, null));
    }

    @Test
    void testLeftPad_EmptyPadStr_UsesSpace() {
        assertEquals("  hello", LeftPadUtils.leftPad("hello", 7, ""));
    }

    @Test
    void testLeftPad_PadLenEqualsPads_ReturnsPadStrConcatStr() {
        assertEquals("abchello", LeftPadUtils.leftPad("hello", 8, "abc"));
    }

    @Test
    void testLeftPad_PadLenGreaterThanPads_ReturnsSubstringConcatStr() {
        assertEquals("ahello", LeftPadUtils.leftPad("hello", 6, "abc"));
    }

    @Test
    void testLeftPad_PadLenLessThanPads_UsesLoop() {
        assertEquals("ababhello", LeftPadUtils.leftPad("hello", 9, "ab"));
    }

    @Test
    void testLeftPad_SingleCharPad() {
        assertEquals("   hello", LeftPadUtils.leftPad("hello", 8, " "));
    }

    @Test
    void testLeftPad_EmptyString() {
        assertEquals("   ", LeftPadUtils.leftPad("", 3, " "));
    }

    @Test
    void testLeftPad_ZeroSize() {
        assertEquals("", LeftPadUtils.leftPad("", 0, " "));
    }

    @Test
    void testLeftPad_NegativeSize() {
        assertEquals("hello", LeftPadUtils.leftPad("hello", -1, " "));
    }
}
