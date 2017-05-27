
import db.MySqlConnection;
import db.Neo4jConnection;
import db.TestSqlConnection;
import db.MongodbConnection;
import indexer.TweetIndexer;
import tweets.TestLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;
import tweets.MongoLoader;

/**
 * Created by Arturo on 07-05-2017.
 */
public class FeelmsIndexerMain {

    public static void main(String[] args) {
        
        
        InputStream inputStream = null;

        Properties prop = new Properties();
        String propFileName = "app.properties";

        try {

            //Lectura de parametros de configuracion

            File f = new File(FeelmsIndexerMain.class.getProtectionDomain().getCodeSource().getLocation().getFile());
            String path = f.getParent() + File.separator + propFileName;

            System.out.print("\n" + path + "\n");

            //inputStream = FeelmsIndexerMain.class.getResourceAsStream("/" + propFileName);
            //inputStream = new FileInputStream(propFileName);
            inputStream = new FileInputStream(path);

            prop.load(inputStream);

            String mysql_host = prop.getProperty("mysql_host");
            String mysql_port = prop.getProperty("mysql_port");
            String mysql_username = prop.getProperty("mysql_username");
            String mysql_password = prop.getProperty("mysql_password");
            String mysql_db_name = prop.getProperty("mysql_db_name");

            System.out.print("MySQL Host: " + mysql_host + "\n");
            System.out.print("MySQL Port: " + mysql_port + "\n");
            System.out.print("MySQL Username: " + mysql_username + "\n");
            System.out.print("MySQL DB Name: " + mysql_db_name + "\n");

            System.out.print("\n");

            String neo4j_uri = prop.getProperty("neo4j_uri");
            String neo4j_user = prop.getProperty("neo4j_user");
            String neo4j_pass = prop.getProperty("neo4j_pass");

            //Comienza a crear conexiones


            //Para cerrar automaticamente neo4j al terminar el programa
            try (Neo4jConnection neo4jConnection = new Neo4jConnection(neo4j_uri, neo4j_user, neo4j_pass)) {


                //Conexion MySQL
                MySqlConnection sqlconn = new MySqlConnection(mysql_username, mysql_password, mysql_host, mysql_port, mysql_db_name);
                boolean SqlTest = sqlconn.test();

                //Conexion MongoDB
                MongoLoader mongoLoader = new MongoLoader();
                boolean MongoTest = mongoLoader.connectionStatus(); //Poner acá prueba de conexion de mongo

                if (SqlTest && MongoTest) {


                    //Realizar tareas
                    //ACA PONER TWEET INDEXER Y CADA COSA DENTRO DE UN TRY-CATCH
                    TweetIndexer indexer = new TweetIndexer(mongoLoader, sqlconn, neo4jConnection);
                    indexer.run();

                } else {
                    if(!SqlTest) {
                        System.out.print("Error de conexión de la base de datos MySQL\n");
                    }

                    if(!MongoTest) {
                        System.out.print("Error de conexión de la base de datos MongoDB\n");
                    }

                    System.out.print("Revisar excepciones correspondientes");
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if(inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }



    }

}
