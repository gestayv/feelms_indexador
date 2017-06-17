package db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public interface SqlConnection {

    public List<Film> getFilms() throws SQLException;

    public int writeData(List<TweetCount> data) throws SQLException;

    public int writeSentiment(List<TweetsSentiments> data) throws SQLException;

    public ArrayList<String> getCountryCodes() throws SQLException;

}
