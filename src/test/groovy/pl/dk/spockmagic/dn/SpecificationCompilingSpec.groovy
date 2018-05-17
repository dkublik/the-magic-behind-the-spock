package pl.dk.spockmagic.dn

import spock.lang.Specification

class SpecificationCompilingSpec extends Specification {

    def "should compile MagnifyingProxySpec.groovy"() {
        given:
            def file = new File('src/test/groovy/pl/dk/spockmagic/dn/MagnifyingProxySpec.groovy')

        when:
            GroovyClassLoader invoker = new GroovyClassLoader()
            Class clazz = invoker.parseClass(file)


        then:
            clazz != null
    }
}
