package com.eljabali;

import java.util.*;

public class View {

    private final ViewListener listener;
    private Scanner inputReader = new Scanner(System.in);

    public View(ViewListener listener) {
        this.listener = listener;
    }

    public void setNewViewState(ViewState newViewState) {
        if (newViewState.displayOutput) {
            System.out.println(newViewState.output);
        }
        if (newViewState.askForInput) {
            listener.enteredInput(inputReader.next());
        }
    }
}