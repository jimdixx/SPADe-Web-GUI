package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositiveIntegerTest {

    @Test
    void testReturnValuesOutOfLimit() {
        // negative value
        int lessThanZero = -1;
        Exception exception = assertThrows(NumberFormatException.class, () -> new PositiveInteger(lessThanZero));
        String expectedMessage = "Positive integer should be between 0 and infinity";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void testParserWithWrongInput() {
        String badValue = "a5";
        assertThrows(NumberFormatException.class, (() -> PositiveInteger.parsePositiveInteger(badValue)));
    }

}