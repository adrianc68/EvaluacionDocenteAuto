package util;

public enum SystemProcessEnum {
    EXIT_VALUE(0);

    private int value;

    SystemProcessEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
