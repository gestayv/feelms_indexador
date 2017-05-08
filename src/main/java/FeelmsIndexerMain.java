import db.TestSqlConnection;
import indexer.TweetIndexer;
import tweets.TestLoader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Properties;

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


        TweetIndexer indexer = new TweetIndexer(new TestLoader(), new TestSqlConnection());
        indexer.run();
    }

}
