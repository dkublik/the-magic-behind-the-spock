package pl.dk.spockmagic

class MagnifyingProxy {

    private final ValueProvider valueProvider
    private UsageCounter usageCounter

    MagnifyingProxy(ValueProvider valueProvider, UsageCounter usageCounter) {
        this.valueProvider = valueProvider
        this.usageCounter = usageCounter
    }

    int provideMagnifiedValue() {
        usageCounter.increase()
        return valueProvider.provideValue() * 10
    }
}
