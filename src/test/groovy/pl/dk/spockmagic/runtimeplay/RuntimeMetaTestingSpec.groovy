package pl.dk.spockmagic.runtimeplay

import groovy.time.TimeCategory
import spock.lang.Specification

class RuntimeMetaTestingSpec extends Specification {

    RuntimePet pet = new RuntimePet()

    def "should add new method"() {
        given:
            RuntimePet.metaClass.greatify << { String text ->
                return "$text the great"
            }
            RuntimePet pet = new RuntimePet() // must be created after method added

        expect:
            pet.greatify('Alexander') == 'Alexander the great'

    }

    def "should use time category"() {
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

    def "should return null not exception for any undefined method"() {
        // because of RuntimePet.invokeMethod
        when:
            def result = pet.someMethod1()

        then:
            noExceptionThrown()
            result == null
    }

}
