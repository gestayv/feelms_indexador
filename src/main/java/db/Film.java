package db;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public class Film {

    private int id;
    private List<String> keyterms;

    private LocalDate lastUpdate;

    private String title;

    private Film() {

    }

    public Film(int id, List<String> kw, LocalDate lastUpdate, String title) {

        this.id = id;
        this.keyterms = kw;
        this.lastUpdate = lastUpdate;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public List<String> getKeyterms() {
        return keyterms;
    }

    public void addKeyterm(String term) { this.keyterms.add(term); }

    public LocalDate getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDate lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
