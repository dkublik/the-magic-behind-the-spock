package pl.dk.spockmagic;

import static java.lang.Math.random;

public class ValueProvider {

    public int provideValue() {
        return (int)(random() * 100);
    }

}
