import groovy.time.TimeCategory
import spock.lang.Specification

class TestClassSpec extends Specification {

    TestClass testClass = new TestClass()


    def "should add method"() {
        given:
            TestClass.metaClass.addedMethod << { String a ->
                return a + 'great'
            }
            TestClass testClass = new TestClass() // must be created after method added

        expect:
            testClass.addedMethod('alexander the ') == 'alexander the great'

    }

    def "should use time cattegory"() {
        when:
            use(TimeCategory)  {
                println 1.minute.from.now
                println 10.hours.ago

                def someDate = new Date()
                println someDate - 3.months
            }

        then:
            noExceptionThrown()


    }

    def "should test Class"() {
        when:
            testClass.someMethod1()

        then:
            noExceptionThrown()
    }

}
