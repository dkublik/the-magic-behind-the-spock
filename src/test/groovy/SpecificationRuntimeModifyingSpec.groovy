import pl.dk.spockmagic.UsageCounter
import pl.dk.spockmagic.ValueProvider
import pl.dk.spockmagic.dn.MagnifyingProxyRawSpec
import spock.lang.Specification

class SpecificationRuntimeModifyingSpec extends Specification {

    def "should modify methods"() {
        given:
            Class spec = MagnifyingProxyRawSpec.class
            spec.metaClass.Stub = {
                println('My custom Stub implementation')
                new ValueProvider() { int provideValue() { 21 } }
            }
            spec.metaClass.Mock = {
                println('My custom Mock implementation')
                new UsageCounter() { void increase() {} }
            }
            MagnifyingProxyRawSpec  magnifyingProxySpec = spec.newInstance()

        when:
            magnifyingProxySpec."should magnify value"()

        then:
            spec != null
    }
}
