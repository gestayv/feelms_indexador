package db;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public class TestSqlConnection implements SqlConnection {


    @Override
    public List<Film> getFilms() {
        List<Film> films = new ArrayList<Film>();

        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("star wars");
        keywords.add("los ultimos jedi");
        keywords.add("the last jedi");
        films.add(new Film(1, keywords, LocalDate.parse("2017-05-01"), "Star Wars"));

        return films;
    }

    @Override
    public int writeData(List<TweetCount> data) {

        return 0;
    }

    @Override
    public int writeSentiment(List<TweetsSentiments> data) throws SQLException {
        return 0;
    }

    @Override
    public ArrayList<String> getCountryCodes() throws SQLException {
        return null;
    }


}
