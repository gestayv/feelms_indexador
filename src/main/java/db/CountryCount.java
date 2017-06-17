package db;

/**
 * Created by Arturo on 17-06-2017.
 */
public class CountryCount {

    private String id;
    private int count;

    private CountryCount() {

    }

    public CountryCount(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
