package pl.dk.spockmagic.spockoff

import groovy.transform.CompileStatic
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ModuleNode
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.spockframework.runtime.model.SpecMetadata
import spock.lang.Specification

@CompileStatic
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
class AddSpecificationSuperClass implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        ModuleNode module = (ModuleNode) nodes[0]
        @SuppressWarnings("unchecked")
        List<ClassNode> classes = module.getClasses()

        for (ClassNode clazz: classes) {
            if (isDisabled(clazz)) {
                clazz.superClass = ClassHelper.make(Specification.class)
                if (!specMetaDataAnnotationExists(clazz)) {
                    AnnotationNode annotationNode = new AnnotationNode(ClassHelper.make(SpecMetadata))
                    annotationNode.addMember('line', new ConstantExpression(6))
                    annotationNode.addMember('filename', new ConstantExpression(''))
                    clazz.annotations.add(annotationNode)
                }
            }
        }
    }

    private boolean specMetaDataAnnotationExists(ClassNode clazz) {
        return clazz.annotations.size() > 0 && clazz.annotations.any { annotation ->
            annotation.classNode.name.endsWith('SpecMetadata')
        }
    }

    private boolean isDisabled(ClassNode clazz) {
        return clazz.getAnnotations().size() > 0 && clazz.getAnnotations().any { annotation ->
            annotation.classNode.name.endsWith('DisableSpockMagic')
        }
    }

}
