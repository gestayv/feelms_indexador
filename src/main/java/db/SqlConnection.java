package db;

import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public interface SqlConnection {

    public List<Film> getFilms();

    public void writeData(List<TweetCount> data);


}
