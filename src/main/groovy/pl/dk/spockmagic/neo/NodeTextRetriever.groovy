package pl.dk.spockmagic.neo

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.AnnotationNode
import org.codehaus.groovy.ast.FieldNode

class NodeTextRetriever {

    static String getText(ASTNode node) {
        if (node.text.startsWith('<not implemented yet for class:')) {
            if (node instanceof FieldNode) {
                FieldNode fieldNode = (FieldNode) node
                return fieldNode.type.name + ': ' + fieldNode.name
            }
            if (node instanceof AnnotationNode) {
                AnnotationNode annotationNode = (AnnotationNode) node
                return annotationNode.members.keySet()
            }
        }
        return node.text
    }

}
