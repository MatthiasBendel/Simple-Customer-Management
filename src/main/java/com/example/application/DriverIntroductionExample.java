package com.example.application;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.Neo4jException;

import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DriverIntroductionExample implements AutoCloseable {
    private static final Logger LOGGER = Logger.getLogger(DriverIntroductionExample.class.getName());
    private final Driver driver;

    public DriverIntroductionExample() {
        // Aura queries use an encrypted connection using the "neo4j+s" protocol
        var uri = "neo4j+s://da4d5b39.databases.neo4j.io";

        var user = "neo4j";
        var password = "paRW4iFN-knhb4nWd6UE1Di7u3ddj141gmrJLfq1Z9I";
        // The driver is a long living object and should be opened during the start of your application
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password), Config.defaultConfig());
    }

    @Override
    public void close() {
        // The driver object should be closed before the application ends.
        driver.close();
    }

    public void createFriendship(final String person1Name, final String person2Name) {
        // To learn more about the Cypher syntax, see https://neo4j.com/docs/cypher-manual/current/
        // The Reference Card is also a good resource for keywords https://neo4j.com/docs/cypher-refcard/current/
        var query = new Query(
                """
                CREATE (p1:Person { name: $person1_name })
                CREATE (p2:Person { name: $person2_name })
                CREATE (p1)-[:KNOWS]->(p2)
                RETURN p1, p2
                """,
                Map.of("person1_name", person1Name, "person2_name", person2Name));

        try (var session = driver.session(SessionConfig.forDatabase("neo4j"))) {
            // Write transactions allow the driver to handle retries and transient errors
            var record = session.executeWrite(tx -> tx.run(query).single());
            System.out.printf(
                    "Created friendship between: %s, %s%n",
                    record.get("p1").get("name").asString(),
                    record.get("p2").get("name").asString());
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, query + " raised an exception", ex);
            throw ex;
        }
    }

    public void createPerson(Person person) {
        final String name = person.name;
        final String address = person.address;
        final String mail = person.mail;
        final String birth = person.birth.toString();

        // To learn more about the Cypher syntax, see https://neo4j.com/docs/cypher-manual/current/
        // The Reference Card is also a good resource for keywords https://neo4j.com/docs/cypher-refcard/current/
        var query = new Query(
                """
                CREATE (p1:Person { name: $name, address: $address, mail: $mail, birth: $birth })
                RETURN p1
                """,
                Map.of("name", name, "address", address, "mail", mail, "birth", birth));

        try (var session = driver.session(SessionConfig.forDatabase("neo4j"))) {
            // Write transactions allow the driver to handle retries and transient errors
            var record = session.executeWrite(tx -> tx.run(query).single());
//            System.out.printf(
//                    "Created person between: %s, %s%n",
//                    record.get("p1").get("name").asString(),
//                    record.get("p2").get("name").asString());
            System.out.println("Created person: " + record.get("p1").get("name").asString());
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, query + " raised an exception", ex);
            throw ex;
        }
    }

    public Person findPerson(final String personName) {
        var query = new Query(
                """
                MATCH (p:Person)
                WHERE p.name = $person_name
                RETURN p.name AS name
                """,
                Map.of("person_name", personName));

        try (var session = driver.session(SessionConfig.forDatabase("neo4j"))) {
            var record = session.executeRead(tx -> tx.run(query).single());
            String name = record.get("name").asString();
            System.out.printf("Found person: %s%n", name);
            // You should capture any errors along with the query and data for traceability
            return new Person(name, "", "", "01.01.1981");

        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, query + " raised an exception", ex);
            throw ex;
        }
    }

    public ArrayList<Person> findPersons() {
        ArrayList<Person> result = new ArrayList<>();
        var query = new Query(
                """
                MATCH (p:Person)
                RETURN p.name AS name
                """);

        try (var session = driver.session(SessionConfig.forDatabase("neo4j"))) {
            var records = session.executeRead(tx -> tx.run(query).list());
            for(Record record : records) {
                System.out.printf("Found person: %s%n", record.get("name").asString());
                result.add(new Person(record.get("name").asString(), "", "", "01.01.1981"));
            }
            // You should capture any errors along with the query and data for traceability
        } catch (Neo4jException ex) {
            LOGGER.log(Level.SEVERE, query + " raised an exception", ex);
            throw ex;
        }
        return result;
    }

    public static void main(String... args) {
        // Aura queries use an encrypted connection using the "neo4j+s" protocol
        var uri = "neo4j+s://da4d5b39.databases.neo4j.io";

        var user = "neo4j";
        var password = "paRW4iFN-knhb4nWd6UE1Di7u3ddj141gmrJLfq1Z9I";

        try (var app = new DriverIntroductionExample()) {
            //app.createFriendship("Alice", "David");
            //app.findPerson("Alice");
            app.createPerson(new Person("Steven Hawking", "test", "none", "08.01.1942"));
        }
    }
}