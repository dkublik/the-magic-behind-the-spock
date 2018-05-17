package pl.dk.spockmagic.dn

import pl.dk.spockmagic.UsageCounter
import pl.dk.spockmagic.ValueProvider
import spock.lang.Specification

class SpecificationRuntimeModifyingSpec extends Specification {

    def "should modify methods"() {
        given:
            MagnifyingProxyRawSpec.metaClass.Stub = {
                println('My custom Stub implementation')
                new ValueProvider() { int provideValue() { 21 } }
            }
            MagnifyingProxyRawSpec.metaClass.Mock = {
                println('My custom Mock implementation')
                new UsageCounter() { void increase() {} }
            }
            MagnifyingProxyRawSpec magnifyingProxySpec = new MagnifyingProxyRawSpec()

        when:
            magnifyingProxySpec."should magnify value"()

        then:
            noExceptionThrown()
    }
}
