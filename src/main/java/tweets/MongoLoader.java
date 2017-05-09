/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweets;

import com.mongodb.*;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;
import com.mongodb.client.model.Sorts;
import java.util.Arrays;
import org.bson.*;

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
 *
 * @author ichigo
 */
public class MongoLoader implements TweetLoader{
    
    @Override   
    public List<Document> getTweets() {
        List<Document> docs = new ArrayList<Document>();
        
        Document tweet = new Document();
        
        Document docA = new Document();
        docA.add(new StoredField("doc_id", "1"));
        docA.add(new StoredField("tweet_id", "10"));
        docA.add(new StoredField("user", "@cog"));
        docA.add(new StoredField("name", "fugg"));
        docA.add(new TextField("text", "La nueva star wars me vale madre", Store.NO));
        docA.add(new StoredField("rt_count", "2"));
        docA.add(new TextField("fecha", LocalDate.parse("2017-05-05").format(DateTimeFormatter.BASIC_ISO_DATE), Store.YES));
        docA.add(new TextField("hashtags", " ", Store.NO));
        
        docs.add(tweet);
        
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
