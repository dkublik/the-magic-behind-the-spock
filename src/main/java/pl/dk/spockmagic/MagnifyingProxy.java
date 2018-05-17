package pl.dk.spockmagic;

public class MagnifyingProxy {

    private final ValueProvider valueProvider;
    private UsageCounter usageCounter;

    public MagnifyingProxy(ValueProvider valueProvider, UsageCounter usageCounter) {
        this.valueProvider = valueProvider;
        this.usageCounter = usageCounter;
    }

    public int provideMagnifiedValue() {
        usageCounter.increase();
        return valueProvider.provideValue() * 10;
    }

}
