package pl.dk.spockmagic.dn

import pl.dk.spockmagic.MagnifyingProxy
import pl.dk.spockmagic.UsageCounter
import pl.dk.spockmagic.ValueProvider
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

