package db;

import org.apache.lucene.document.Document;

import java.util.List;

/**
 * Created by Arturo on 02-06-2017.
 */
public class DocumentList {

    private List<Tweet> tweets;
    private List<Document> documents;

    public DocumentList(List<Tweet> tweets, List<Document> documents) {
        this.tweets = tweets;
        this.documents = documents;
    }

    public List<Tweet> getTweets() {
        return tweets;
    }

    public void setTweets(List<Tweet> tweets) {
        this.tweets = tweets;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
