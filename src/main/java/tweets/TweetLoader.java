package tweets;

import db.DocumentList;
import org.apache.lucene.document.Document;

import java.util.Date;
import java.util.List;

/**
 * Created by Arturo on 07-05-2017.
 */
public interface TweetLoader {

    public DocumentList getTweets();

    public List<Document> getTweets(Date date);

    public List<Document> getTweets(Date start, Date end);

}
