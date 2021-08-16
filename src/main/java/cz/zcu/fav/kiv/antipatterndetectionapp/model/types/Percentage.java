package cz.zcu.fav.kiv.antipatterndetectionapp.model.types;

public class Percentage extends Number {

    public static final float MAX_VALUE = 100;
    public static final float MIN_VALUE = 0;

    private final float value;

    public Percentage(float value) throws NumberFormatException {
        if (value > MAX_VALUE || value < MIN_VALUE) {
            throw new NumberFormatException("Percentage should be between 0 and 100");
        }
        this.value = value;
    }

    public static Percentage parsePercentage(String value) {
        return new Percentage(Float.parseFloat(value));
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
        return (float) this.value;
    }

    @Override
    public double doubleValue() {
        return (double) this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public float getValue() {
        return value /100;
    }
}
