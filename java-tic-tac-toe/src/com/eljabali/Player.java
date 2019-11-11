package com.eljabali;

public enum Player {
    X("X"),
    O("O"),
    NA(" ");

    public final String displayValue;

    Player(String displayValue) {
        this.displayValue = displayValue;
    }
}
