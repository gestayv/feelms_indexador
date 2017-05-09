package tweets;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public class TestLoader implements TweetLoader {


    @Override
    public List<Document> getTweets() {

        List<Document> docs = new ArrayList<Document>();

        //Documento A

        Document docA = new Document();
        docA.add(new StoredField("doc_id", "1"));
        docA.add(new StoredField("tweet_id", "10"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "La nueva star wars me vale madre", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-05").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", " ", Store.NO));

        docs.add(docA);

        docA = new Document();
        docA.add(new StoredField("doc_id", "2"));
        docA.add(new StoredField("tweet_id", "20"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "StarWars es horrible", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-04").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", "HorribleTodo MAGA", Store.NO));

        docs.add(docA);

        docA = new Document();
        docA.add(new StoredField("doc_id", "3"));
        docA.add(new StoredField("tweet_id", "30"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "Que cresta esta pelicula de los ultimos jedi", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-03").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", "NoBachelet2024", Store.NO));

        docs.add(docA);

        docA = new Document();
        docA.add(new StoredField("doc_id", "4"));
        docA.add(new StoredField("tweet_id", "40"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "The last jedi sucks my deck", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-05").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", "NoBachelet2024", Store.NO));

        docs.add(docA);

        docA = new Document();
        docA.add(new StoredField("doc_id", "5"));
        docA.add(new StoredField("tweet_id", "50"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "Hola mundo me quiero morir", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-04").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", "Starwars", Store.NO));

        docs.add(docA);

        docA = new Document();
        docA.add(new StoredField("doc_id", "5"));
        docA.add(new StoredField("tweet_id", "50"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "Hola mundo me quiero morir Thor: RAGNAROK", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-07").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", "BladeRunner2049", Store.NO));

        docs.add(docA);

        docA = new Document();
        docA.add(new StoredField("doc_id", "5"));
        docA.add(new StoredField("tweet_id", "50"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "Star Wars episode VIII thor ragnarok! Blade Runner.2049", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-07").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", "holi", Store.NO));

        docs.add(docA);

        return docs;
    }

    @Override
    public List<Document> getTweets(Date date) {
        return null;
    }

    @Override
    public List<Document> getTweets(Date start, Date end) {
        return null;
    }
}
