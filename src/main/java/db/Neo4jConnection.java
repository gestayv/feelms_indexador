package db;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.neo4j.driver.v1.Values.parameters;

/**
 * Created by Arturo on 24-05-2017.
 */
public class Neo4jConnection implements AutoCloseable {

    public final Driver driver;

    public Neo4jConnection(String uri, String user, String password) {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }


    public int buildFilmUserGraph(List<User> users, Film film) {
        int result = 1;

        List list = new ArrayList<>();

        for (User user : users) {
            ArrayList aux = new ArrayList();
            aux.add(user.getName());
            //Old, conteos
            //aux.add(Integer.toString(user.getCount()));
            aux.add(Integer.toString(user.getDate()));
            list.add(aux);
        }

        try (Session session = driver.session()) {

            Map<String, Object> params = new HashMap<>();
            params.put("id", film.getId());
            params.put("title", film.getTitle());
            params.put("userList", list);

            //cambiar el name por title despues
            //OLD, con conteos
            /*
            session.run("MERGE (m:Film {film_id: $id})\n" +
                    "ON CREATE SET m.name = $title \n" +
                    "FOREACH (n in $userList | \n" +
                    "\tMERGE (u:User {name: n[0]})\n" +
                    "    MERGE (u)-[r:Tweeting]->(m)\n" +
                    "    ON CREATE SET r.count = toInteger(n[1])\n" +
                    "    ON MATCH SET r.count = r.count + toInteger(n[1])\n" +
                    ")", params
            ); */

            //cambiar el name por title despues
            session.run("MERGE (m:Film {film_id: $id})\n" +
                    "ON CREATE SET m.name = $title \n" +
                    "FOREACH (n in $userList | \n" +
                    "\tMERGE (u:User {name: n[0]})\n" +
                    "    MERGE (u)-[r:Tweeting]->(m)\n" +
                    "    ON CREATE SET r.last_tweet = toInteger(n[1])\n" +
                    "    ON MATCH SET r.last_tweet = toInteger(n[1])\n" +
                    ")", params
            );

        }

        return result;
    }


}
