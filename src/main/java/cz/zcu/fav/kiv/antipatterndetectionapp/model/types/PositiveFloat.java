package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

/**
 * This class represents percentage as a positive float number between 0 and MAX_FLOAT.
 */
public class PositiveFloat extends Number {
    public static final float MAX_VALUE = Float.MAX_VALUE;
    public static final int MIN_VALUE = 0;

    private final float value;

    public PositiveFloat(float value) throws NumberFormatException {
        if (value > MAX_VALUE || value < MIN_VALUE) {
            throw new NumberFormatException("Positive integer should be between 0 and infinity");
        }
        this.value = value;
    }


    @Override
    public int intValue() {
        return (int) this.value;
    }

    @Override
    public long longValue() {
        return (long) this.value;
    }

    @Override
    public float floatValue() {
        return this.value;
    }

    @Override
    public double doubleValue() {
        return this.value;
    }

    public static PositiveFloat parsePositiveFloat(String value) {
        return new PositiveFloat(Float.parseFloat(value));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
