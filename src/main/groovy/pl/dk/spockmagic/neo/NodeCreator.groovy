package pl.dk.spockmagic.neo

import org.codehaus.groovy.ast.ASTNode
import org.neo4j.driver.v1.*

import static org.neo4j.driver.v1.Values.parameters

class NodeCreator implements AutoCloseable {

    private final Driver driver

    NodeCreator(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
    }

    @Override
    void close() {
        driver.close()
    }

    void addNode(ASTNode parent, ASTNode node) {
        Session session = driver.session()
        try {
            Integer id = session.writeTransaction({ tx ->
                StatementResult result = tx.run( 'CREATE (a:' + node.class.simpleName + ') ' +
                                'SET a.text = $text, a.id=id(a) ' +
                                'RETURN a.id',
                        parameters( 'text', NodeTextRetriever.getText(node)))
                return result.single().get(0).asInt()
            })
            node.nodeId = id

            if (parent != null) {
                session.writeTransaction({ tx ->
                    StatementResult result = tx.run('MATCH (a),(b) ' +
                            'WHERE a.id = ' + parent.nodeId + ' AND b.id = ' + node.nodeId + ' ' +
                            'CREATE (a)-[r:contains]->(b) ' +
                            'RETURN type(r)')
                    return result.single().get(0).asString()
                })
            }
        } finally {
            session.close()
        }
    }
}
