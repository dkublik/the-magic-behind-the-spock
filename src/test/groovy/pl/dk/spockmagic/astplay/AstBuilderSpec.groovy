package pl.dk.spockmagic.astplay

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.builder.AstBuilder
import pl.dk.spockmagic.neo.GraphPersistingVisitor
import spock.lang.Specification

import static org.codehaus.groovy.control.CompilePhase.SEMANTIC_ANALYSIS

class AstBuilderSpec extends Specification {

    def "should build class from code - arithmetical"() {
        when:
            List<ASTNode> nodes = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, true, """
                    2 + 3 * 4
                """)

        then:
            noExceptionThrown()
    }

    def "should build class from code - constant expression"() {
        when:
            List<ASTNode> nodes = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, true, """
                'København'
            """)

        then:
            noExceptionThrown()

    }

    def "should build class from code - declaration"() {
        when:
            List<ASTNode> nodes = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, true, """
                    String city = 'København'
                """)

        then:
            noExceptionThrown()
    }

    def "should build class from code - for loop"() {
        when:
            List<ASTNode> nodes = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, true, """
                    for (def a: 'København')
                        println(a)    
                """)

        then:
            noExceptionThrown()
    }

















    def "should build class from code"() {
        when:
            def nodes = new AstBuilder().buildFromString(SEMANTIC_ANALYSIS, true, """
                package pl.dk.spockmagic
                
                import pl.dk.spockmagic.spockoff.DisableSpockMagic
                import spock.lang.Specification
                import spock.lang.Subject
                
                @DisableSpockMagic
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
            """)
            ClassNode specNode = nodes[1]
            GraphPersistingVisitor graphPersistingVisitor = new GraphPersistingVisitor()
            graphPersistingVisitor.visitClass(specNode)

        then:
            nodes != null
    }

}
