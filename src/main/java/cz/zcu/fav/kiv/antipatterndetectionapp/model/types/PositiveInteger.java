package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents percentage as a positive int number between 0 and MAX_INT.
 */
public class PositiveInteger extends Number {

    public static final int MAX_VALUE = Integer.MAX_VALUE;
    public static final int MIN_VALUE = 0;

    private final int value;

    private final Logger LOGGER = LoggerFactory.getLogger(PositiveInteger.class);

    public PositiveInteger(int value) throws NumberFormatException {
        if (value > MAX_VALUE || value < MIN_VALUE) {
            LOGGER.error("Invalid number entered!");
            throw new NumberFormatException("Positive integer should be between 0 and infinity");
        }
        this.value = value;
    }


    @Override
    public int intValue() {
        return this.value;
    }

    @Override
    public long longValue() {
        return this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    public static PositiveInteger parsePositiveInteger(String value) {
        return new PositiveInteger(Integer.parseInt(value));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
