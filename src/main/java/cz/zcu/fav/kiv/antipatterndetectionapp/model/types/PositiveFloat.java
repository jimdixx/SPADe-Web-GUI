package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

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
        return 0;
    }

    @Override
    public long longValue() {
        return 0;
    }

    @Override
    public float floatValue() {
        return 0;
    }

    @Override
    public double doubleValue() {
        return 0;
    }

    public static PositiveFloat parsePositiveFloat(String value) {
        return new PositiveFloat(Float.parseFloat(value));
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
