
import db.MySqlConnection;
import db.TestSqlConnection;
import db.MongodbConnection;
import indexer.TweetIndexer;
import tweets.TestLoader;

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

        try {
            Properties prop = new Properties();
            String propFileName = "app.properties";

            inputStream = new FileInputStream(propFileName);

            prop.load(inputStream);

            String mysql_host = prop.getProperty("mysql_host");
            String mysql_port = prop.getProperty("mysql_port");
            String mysql_username = prop.getProperty("mysql_username");
            String mysql_password = prop.getProperty("mysql_password");
            String mysql_db_name = prop.getProperty("mysql_db_name");

            System.out.print("MySQL Host: " + mysql_host + "\n");
            System.out.print("MySQL Port: " + mysql_port + "\n");
            System.out.print("MySQL Username: " + mysql_username + "\n");
            System.out.print("MySQL Password: " + mysql_password + "\n");
            System.out.print("MySQL DB Name: " + mysql_db_name + "\n");

            System.out.print("\n");


            //Conexion MySQL
            MySqlConnection sqlconn = new MySqlConnection(mysql_username, mysql_password, mysql_host, mysql_port, mysql_db_name);
            boolean SqlTest = sqlconn.test();

            //Conexion MongoDB

            boolean MongoTest = true; //Poner acá prueba de conexion de mongo

            if (SqlTest && MongoTest) {

                //Prueba SQL
                try {
                    sqlconn.getFilms();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                //Realizar tareas
                //ACA PONER TWEET INDEXER Y CADA COSA DENTRO DE UN TRY-CATCH
                TweetIndexer indexer = new TweetIndexer(new TestLoader(), sqlconn);
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



        } catch (IOException e) {
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

        /*
        //Borrar esto despues, solo para pruebas
        TweetIndexer indexer = new TweetIndexer(new TestLoader(), new TestSqlConnection());
        indexer.run();
        */

    }

}
