package pl.dk.spockmagic.neo

import org.codehaus.groovy.ast.*
import org.codehaus.groovy.ast.expr.*
import org.codehaus.groovy.ast.stmt.*
import org.codehaus.groovy.classgen.BytecodeExpression
import org.codehaus.groovy.control.SourceUnit

class GraphPersistingVisitor extends ClassCodeVisitorSupport {

    ASTNode parentNode
    private int indents = 0
    private NodeCreator nodeCreator = new NodeCreator( 'bolt://localhost:7687', 'neo4j', 'pass' )

    private GraphPersistingVisitor() {
        def properties = Collections.synchronizedMap([:])
        ASTNode.metaClass.setNodeId = { String value ->
            properties[System.identityHashCode(delegate) + "nodeId"] = value
        }
        ASTNode.metaClass.getNodeId = {->
            properties[System.identityHashCode(delegate) + "nodeId"]
        }
    }

    private void addNode(ASTNode node, Class expectedSubclass, Closure superMethod) {
        if (expectedSubclass.getName() == node.getClass().getName()) {
            if (parentNode == null) {
                parentNode = node
                onNode(null, node)
                superMethod.call(node)
            } else {
                def currentNode = parentNode
                parentNode = node
                indents ++
                onNode(currentNode, node)
                superMethod.call(node)
                indents --
                parentNode = currentNode
            }
        } else {
            superMethod.call(node)
        }
    }

    private void onNode(ASTNode parent, ASTNode node) {
        nodeCreator.addNode(parent, node)
        printNode(node)
    }

    private void printNode(ASTNode node) {
        for (int i = 0; i < indents; i++) {
            print('    ')
        }
        println(node.class.simpleName + ', ' + NodeTextRetriever.getText(node))
    }

    void visitClass(ClassNode node) {
        addNode(node, ClassNode, {
            visitAnnotations(node)
            visitPackage(node.getPackage())
            visitImports(node.getModule())
            node.visitContents(this)
            visitObjectInitializerStatements(node)
        })
    }

    void visitAnnotations(AnnotatedNode node) {
        List<AnnotationNode> annotations = node.getAnnotations()
        if (annotations.isEmpty()) return
        for (AnnotationNode an : annotations) {
            addNode(an, AnnotationNode, {
                // skip built-in properties
                if (an.isBuiltIn()) return
                for (Map.Entry<String, Expression> member : an.getMembers().entrySet()) {
                    addNode(node, Expression, {
                        member.getValue().visit(this)
                    })
                }
            })
        }
    }

    void visitPackage(PackageNode node) {
        if (node != null) {
            addNode(node, PackageNode, {
                visitAnnotations(node)
                node.visit(this)
            })
        }
    }

    void visitField(FieldNode node) {
        addNode(node, FieldNode, {
            visitAnnotations(node)
            Expression init = node.getInitialExpression()
            if (init != null) init.visit(this)
        })
    }

    void visitProperty(PropertyNode node) {
    }

/*    void visitProperty(PropertyNode node) {
        addNode(node, PropertyNode, {
            visitAnnotations(node)
            Statement statement = node.getGetterBlock()
            visitClassCodeContainer(statement)

            statement = node.getSetterBlock()
            visitClassCodeContainer(statement)

            Expression init = node.getInitialExpression()
            if (init != null) init.visit(this)
        })
    }*/


    void visitBlockStatement(BlockStatement block) {
        addNode(block, BlockStatement, {
            for (Statement statement : block.getStatements()) {
                addNode(statement, Statement, { statement.visit(this) })
            }
        })
    }

    void visitForLoop(ForStatement node) {
        addNode(node, ForStatement, { super.visitForLoop(it) })
    }

    void visitWhileLoop(WhileStatement node) {
        addNode(node, WhileStatement, { super.visitWhileLoop(it) })
    }

    void visitDoWhileLoop(DoWhileStatement node) {
        addNode(node, DoWhileStatement, { super.visitDoWhileLoop(it) })
    }

    void visitIfElse(IfStatement node) {
        addNode(node, IfStatement, { super.visitIfElse(it) })
    }

    void visitExpressionStatement(ExpressionStatement node) {
        addNode(node, ExpressionStatement, { super.visitExpressionStatement(it) })
    }

    void visitReturnStatement(ReturnStatement node) {
        addNode(node, ReturnStatement, { super.visitReturnStatement(it) })
    }

    void visitAssertStatement(AssertStatement node) {
        addNode(node, AssertStatement, { super.visitAssertStatement(it) })
    }

    void visitTryCatchFinally(TryCatchStatement node) {
        addNode(node, TryCatchStatement, { super.visitTryCatchFinally(it) })
    }

    protected void visitEmptyStatement(EmptyStatement node) {
        addNode(node, EmptyStatement, { super.visitEmptyStatement(it) })
    }

    void visitSwitch(SwitchStatement node) {
        addNode(node, SwitchStatement, { super.visitSwitch(it) })
    }

    void visitCaseStatement(CaseStatement node) {
        addNode(node, CaseStatement, { super.visitCaseStatement(it) })
    }

    void visitBreakStatement(BreakStatement node) {
        addNode(node, BreakStatement, { super.visitBreakStatement(it) })
    }

    void visitContinueStatement(ContinueStatement node) {
        addNode(node, ContinueStatement, { super.visitContinueStatement(it) })
    }

    void visitSynchronizedStatement(SynchronizedStatement node) {
        addNode(node, SynchronizedStatement, { super.visitSynchronizedStatement(it) })
    }

    void visitThrowStatement(ThrowStatement node) {
        addNode(node, ThrowStatement, { super.visitThrowStatement(it) })
    }

    void visitMethodCallExpression(MethodCallExpression node) {
        addNode(node, MethodCallExpression, { super.visitMethodCallExpression(it) })
    }

    void visitStaticMethodCallExpression(StaticMethodCallExpression node) {
        addNode(node, StaticMethodCallExpression, { super.visitStaticMethodCallExpression(it) })
    }

    void visitConstructorCallExpression(ConstructorCallExpression node) {
        addNode(node, ConstructorCallExpression, { super.visitConstructorCallExpression(it) })
    }

    void visitBinaryExpression(BinaryExpression node) {
        addNode(node, BinaryExpression, { super.visitBinaryExpression(it) })
    }

    void visitTernaryExpression(TernaryExpression node) {
        addNode(node, TernaryExpression, { super.visitTernaryExpression(it) })
    }

    void visitShortTernaryExpression(ElvisOperatorExpression node) {
        addNode(node, ElvisOperatorExpression, { super.visitShortTernaryExpression(it) })
    }

    void visitPostfixExpression(PostfixExpression node) {
        addNode(node, PostfixExpression, { super.visitPostfixExpression(it) })
    }

    void visitPrefixExpression(PrefixExpression node) {
        addNode(node, PrefixExpression, { super.visitPrefixExpression(it) })
    }

    void visitBooleanExpression(BooleanExpression node) {
        addNode(node, BooleanExpression, { super.visitBooleanExpression(it) })
    }

    void visitNotExpression(NotExpression node) {
        addNode(node, NotExpression, { super.visitNotExpression(it) })
    }

    void visitClosureExpression(ClosureExpression node) {
        addNode(node, ClosureExpression, {
            it.parameters?.each { parameter -> visitParameter(parameter) }
            super.visitClosureExpression(it)
        })
    }

    void visitParameter(Parameter node) {
        addNode(node, Parameter, {
            if (node.initialExpression) {
                node.initialExpression?.visit(this)
            }
        })
    }

    void visitTupleExpression(TupleExpression node) {
        addNode(node, TupleExpression, { super.visitTupleExpression(it) })
    }

    void visitListExpression(ListExpression node) {
        addNode(node, ListExpression, { super.visitListExpression(it) })
    }

    void visitArrayExpression(ArrayExpression node) {
        addNode(node, ArrayExpression, { super.visitArrayExpression(it) })
    }

    void visitMapExpression(MapExpression node) {
        addNode(node, MapExpression, { super.visitMapExpression(it) })
    }

    void visitMapEntryExpression(MapEntryExpression node) {
        addNode(node, MapEntryExpression, { super.visitMapEntryExpression(it) })
    }

    void visitRangeExpression(RangeExpression node) {
        addNode(node, RangeExpression, { super.visitRangeExpression(it) })
    }

    void visitSpreadExpression(SpreadExpression node) {
        addNode(node, SpreadExpression, { super.visitSpreadExpression(it) })
    }

    void visitSpreadMapExpression(SpreadMapExpression node) {
        addNode(node, SpreadMapExpression, { super.visitSpreadMapExpression(it) })
    }

    void visitMethodPointerExpression(MethodPointerExpression node) {
        addNode(node, MethodPointerExpression, { super.visitMethodPointerExpression(it) })
    }

    void visitUnaryMinusExpression(UnaryMinusExpression node) {
        addNode(node, UnaryMinusExpression, { super.visitUnaryMinusExpression(it) })
    }

    void visitUnaryPlusExpression(UnaryPlusExpression node) {
        addNode(node, UnaryPlusExpression, { super.visitUnaryPlusExpression(it) })
    }

    void visitBitwiseNegationExpression(BitwiseNegationExpression node) {
        addNode(node, BitwiseNegationExpression, { super.visitBitwiseNegationExpression(it) })
    }

    void visitCastExpression(CastExpression node) {
        addNode(node, CastExpression, { super.visitCastExpression(it) })
    }

    void visitConstantExpression(ConstantExpression node) {
        addNode(node, ConstantExpression, { super.visitConstantExpression(it) })
    }

    void visitClassExpression(ClassExpression node) {
        addNode(node, ClassExpression, { super.visitClassExpression(it) })
    }

    void visitVariableExpression(VariableExpression node) {
        addNode(node, VariableExpression, { VariableExpression it ->
            if (it.accessedVariable) {
                if (it.accessedVariable instanceof Parameter) {
                    visitParameter((Parameter)it.accessedVariable)
                } else if (it.accessedVariable instanceof DynamicVariable) {
                    addNode(it.accessedVariable, DynamicVariable,{ it.initialExpression?.visit(this)})
                }
            }
        })
    }

    void visitDeclarationExpression(DeclarationExpression node) {
        addNode(node, DeclarationExpression, { super.visitDeclarationExpression(it) })
    }

    @Override
    protected SourceUnit getSourceUnit() {
        return null
    }

    void visitPropertyExpression(PropertyExpression node) {
        addNode(node, PropertyExpression, { super.visitPropertyExpression(it) })
    }

    void visitAttributeExpression(AttributeExpression node) {
        addNode(node, AttributeExpression, { super.visitAttributeExpression(it) })
    }

    void visitFieldExpression(FieldExpression node) {
        addNode(node, FieldExpression, { super.visitFieldExpression(it) })
    }

    void visitGStringExpression(GStringExpression node) {
        addNode(node, GStringExpression, { super.visitGStringExpression(it) })
    }

    void visitCatchStatement(CatchStatement node) {
        addNode(node, CatchStatement, {
            if (it.variable) visitParameter(it.variable)
            super.visitCatchStatement(it)
        })
    }

    void visitArgumentlistExpression(ArgumentListExpression node) {
        addNode(node, ArgumentListExpression, { super.visitArgumentlistExpression(it) })
    }

    void visitClosureListExpression(ClosureListExpression node) {
        addNode(node, ClosureListExpression, { super.visitClosureListExpression(it) })
    }

    void visitBytecodeExpression(BytecodeExpression node) {
        addNode(node, BytecodeExpression, { super.visitBytecodeExpression(it) })
    }

    protected void visitListOfExpressions(List<? extends Expression> list) {
        list.each { Expression node ->
            if (node instanceof NamedArgumentListExpression ) {
                addNode(node, NamedArgumentListExpression, { it.visit(this) })
            } else {
                node.visit(this)
            }
        }
    }

    protected void visitConstructorOrMethod(MethodNode node, boolean isConstructor) {
        addNode(node, MethodNode, { super.visitConstructorOrMethod(it, isConstructor) })
    }
}
