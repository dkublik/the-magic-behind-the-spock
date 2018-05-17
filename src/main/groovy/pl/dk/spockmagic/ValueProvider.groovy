package pl.dk.spockmagic

import static java.lang.Math.random

class ValueProvider {

    int provideValue() {
        return (int)(random() * 100)
    }
}
