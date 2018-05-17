package pl.dk.spockmagic.dn

import pl.dk.spockmagic.MagnifyingProxySpec
import spock.lang.Specification

class SpecificationRunningSpec extends Specification {

    def "should run specification method"() {
        when:
            new MagnifyingProxySpec()."should magnify value"()

        then:
            noExceptionThrown()
    }
}
