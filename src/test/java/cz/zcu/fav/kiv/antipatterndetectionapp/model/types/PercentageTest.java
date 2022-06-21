package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PercentageTest {
    Percentage percentage;

    @Test
    void testReturnValue() {
        float inputValue = 50f;

        percentage = new Percentage(inputValue);
        assertEquals(0.6, percentage.getValue());
    }

}