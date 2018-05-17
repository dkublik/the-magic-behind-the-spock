import spock.lang.Specification

class SpecificationCompilingSpec extends Specification {

    def "should execute magnifying spec"() {
        given:
            def file = new File('src/test/groovy/pl.dk.spockmagic.dn.MagnifyingProxySpec.groovy')

        when:
            GroovyClassLoader invoker = new GroovyClassLoader()
            Class clazz = invoker.parseClass(file)

        then:
            clazz != null
    }
}
