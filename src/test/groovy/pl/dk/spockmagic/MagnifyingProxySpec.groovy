package pl.dk.spockmagic

import spock.lang.Specification
import spock.lang.Subject

class MagnifyingProxySpec extends Specification {

    ValueProvider valueProvider = Stub()
    UsageCounter usageCounter = Mock()

    @Subject
    MagnifyingProxy magnifyingProxy = new MagnifyingProxy(valueProvider, usageCounter)

    def "should magnify value"() {
        given:
            valueProvider.provideValue() >> 21

        when:
            int result = magnifyingProxy.provideMagnifiedValue()

        then:
            result == 210
            1 * usageCounter.increase()
    }
}

