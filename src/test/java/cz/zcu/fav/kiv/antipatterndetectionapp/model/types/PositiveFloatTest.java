package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositiveFloatTest {


    @Test
    void testReturnValuesOutOfLimit() {
        // negative value
        float lessThanZero = -1f;
        Exception exception = assertThrows(NumberFormatException.class, () -> new PositiveFloat(lessThanZero));
        String expectedMessage = "Positive integer should be between 0 and infinity";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void testParserWithWrongInput() {
        String badValue = "a5f";
        assertThrows(NumberFormatException.class, (() -> PositiveFloat.parsePositiveFloat(badValue)));
    }

}