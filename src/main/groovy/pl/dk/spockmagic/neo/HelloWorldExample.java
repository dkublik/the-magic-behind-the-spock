package pl.dk.spockmagic.neo;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;

import static org.neo4j.driver.v1.Values.parameters;

public class HelloWorldExample implements AutoCloseable {

    private final Driver driver;

    public HelloWorldExample(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password) );
    }

    @Override
    public void close() {
        driver.close();
    }

    public void printGreeting( final String message ) {
        try (Session session = driver.session()) {
            String greeting = session.writeTransaction(tx -> {
                StatementResult result = tx.run( "CREATE (a:Greeting) " +
                                "SET a.message = $message " +
                                "RETURN a.message + ', from node ' + id(a)",
                        parameters( "message", message ) );
                return result.single().get( 0 ).asString();
            });
            System.out.println(greeting);
        }
    }

    public static void main(String... args) {
        try (HelloWorldExample greeter = new HelloWorldExample( "bolt://localhost:7687", "neo4j", "pass" )) {
            greeter.printGreeting( "hello, world" );
        }
    }
}
