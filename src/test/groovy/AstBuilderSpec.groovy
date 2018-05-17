import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.control.CompilePhase
import pl.dk.spockmagic.dn.MagnifyingProxySpec
import pl.dk.spockmagic.neo.GraphPersistingVisitor
import pl.dk.spockmagic.neo.GraphPersistingVisitor2
import spock.lang.Specification

class AstBuilderSpec extends Specification {


    def "should build block statement from code"() {
        when:
        def code = new AstBuilder().buildFromCode {
            String city = 'København'
        }

        then:
            code != null

    }


    def "should build class from code0"() {
        when:
        List<ASTNode> nodes = new AstBuilder().buildFromString(CompilePhase.SEMANTIC_ANALYSIS, true, """
                2 + 3 * 4
            """)

        then:
        noExceptionThrown()

    }

    def "should build class from code1"() {
        when:
            List<ASTNode> nodes = new AstBuilder().buildFromString(CompilePhase.SEMANTIC_ANALYSIS, true, """
                'København'
            """)

        then:
        noExceptionThrown()

    }



    def "should build class from code2"() {
        when:
        List<ASTNode> nodes = new AstBuilder().buildFromString(CompilePhase.SEMANTIC_ANALYSIS, true, """
                String city = 'København'
            """)

        then:
        noExceptionThrown()

    }

    def "should build class from code3"() {
        when:
        List<ASTNode> nodes = new AstBuilder().buildFromString(CompilePhase.SEMANTIC_ANALYSIS, true, """
                for (def a: 'København')
                    println(a)    
            """)

        then:
        noExceptionThrown()

    }


    def "should build class from code"() {
        when:
        def code = new AstBuilder().buildFromString(CompilePhase.CONVERSION, true, """
import spock.lang.Specification
import spock.lang.Subject
class pl.dk.spockmagic.dn.MagnifyingProxySpec extends Specification {

    pl.dk.spockmagic.ValueProvider valueProvider = Stub()

    @Subject
    pl.dk.spockmagic.MagnifyingProxy magnifyingProxy = new pl.dk.spockmagic.MagnifyingProxy(valueProvider)

    def "should magnify value"() {
        given:
        valueProvider.provideValue() >> 21

        when:
        int result = magnifyingProxy.provideMagnifiedValue()

        then:
        result == 210
    }
}
        """)
        ClassNode specNode = code[1]
       // GraphPersistingVisitor graphPersistingVisitor = new GraphPersistingVisitor()
     //   graphPersistingVisitor.visitClass(specNode)
        GraphPersistingVisitor2 graphPersistingVisitor = new GraphPersistingVisitor2(null)
        specNode.visitContents(graphPersistingVisitor)


        then:
        code != null

    }

    // not working
    def "should build class by class helper"() {
        when:
            ClassNode specNode = ClassHelper.make(MagnifyingProxySpec.class)
            GraphPersistingVisitor graphPersistingVisitor = new GraphPersistingVisitor()
            graphPersistingVisitor.visitClass(specNode)

        then:
            specNode != null

    }
}
