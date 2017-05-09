package db;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public interface SqlConnection {

    public List<Film> getFilms() throws SQLException;

    public void writeData(List<TweetCount> data) throws SQLException;


}
