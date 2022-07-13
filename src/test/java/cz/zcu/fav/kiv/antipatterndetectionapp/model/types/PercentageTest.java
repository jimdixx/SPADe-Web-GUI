package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;

class PercentageTest {
    Percentage percentage;

    @Test
    void testReturnValuesOutOfLimit() {
        // negative value
        int lessThanZero = Integer.MIN_VALUE;
        Exception exception = assertThrows(NumberFormatException.class, () -> new Percentage(lessThanZero));
        String expectedMessage = "Percentage should be between 0 and 100";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void testReturnValuesOutOfLimit2() {
        // negative value
        int greaterThanZero = Integer.MAX_VALUE;
        Exception exception = assertThrows(NumberFormatException.class, () -> new Percentage(greaterThanZero));
        String expectedMessage = "Percentage should be between 0 and 100";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.equals(expectedMessage));
    }

    @Test
    void testOtherDataTypesOnInput() {
        float f = 50f;
        percentage = new Percentage(f);
        assertEquals(0.5, percentage.getValue());

        f = 5f;
        percentage = new Percentage(f);
        assertEquals(0.05, round(percentage.getValue(), 2));
    }

    @Test
    void testReturnValuesCorrectInputs() {

        Percentage[] values = new Percentage[101];
        for (int i = 0; i < values.length; i++) {
            percentage = new Percentage(i);
            assertEquals(round(i / 100.0, 2), round(percentage.getValue(), 2));
        }

    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}