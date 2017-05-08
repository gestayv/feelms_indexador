package db;

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
        films.add(new Film(1, keywords, LocalDate.parse("2017-05-01")));

        return films;
    }

    @Override
    public void writeData(List<TweetCount> data) {

    }

}
