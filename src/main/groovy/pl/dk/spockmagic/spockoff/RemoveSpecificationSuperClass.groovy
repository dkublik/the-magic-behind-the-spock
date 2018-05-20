package pl.dk.spockmagic.spockoff

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import spock.mock.MockingApi

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CONVERSION)
class RemoveSpecificationSuperClass implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        ModuleNode module = (ModuleNode) nodes[0]
        @SuppressWarnings("unchecked")
        List<ClassNode> classes = module.getClasses()

        for (ClassNode clazz: classes) {
            if (isDisabled(clazz)) {
                clazz.superClass = ClassHelper.make(MockingApi.class)
            }
        }
    }

    private boolean isDisabled(ClassNode clazz) {
        return clazz.getAnnotations().size() > 0 && clazz.getAnnotations().any { annotation ->
            annotation.classNode.name.endsWith('DisableSpockMagic')
        }
    }

}
