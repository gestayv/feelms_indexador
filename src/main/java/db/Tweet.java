/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.util.List;

/**
 *
 * @author ichigo
 */
public class Tweet {


    private String id_doc;
    private String id_tweet;
    private String user;
    private String name;
    private String text;
    private String rt_count;
    private String fecha;
    private String hashtag;
    private String pais;
    
    public Tweet(String id_doc, String id_tweet, String usr, String name, String text, String rt_count, String fecha, String ht, String pais)
    {
        this.id_doc = id_doc;
        this.id_tweet = id_tweet;
        this.user = usr;
        this.name = name;
        this.text = text;
        this.rt_count = rt_count;
        this.fecha = fecha;
        this.hashtag = ht;
        this.pais = pais;
    }
    
    /**
     * @return the pais
     */
    public String getPais() {
        return pais;
    }

    /**
     * @param pais the pais to set
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * @return the id_doc
     */
    public String getId_doc() {
        return id_doc;
    }

    /**
     * @param id_doc the id_doc to set
     */
    public void setId_doc(String id_doc) {
        this.id_doc = id_doc;
    }

    /**
     * @return the id_tweet
     */
    public String getId_tweet() {
        return id_tweet;
    }

    /**
     * @param id_tweet the id_tweet to set
     */
    public void setId_tweet(String id_tweet) {
        this.id_tweet = id_tweet;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text the text to set
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return the rt_count
     */
    public String getRt_count() {
        return rt_count;
    }

    /**
     * @param rt_count the rt_count to set
     */
    public void setRt_count(String rt_count) {
        this.rt_count = rt_count;
    }

    /**
     * @return the fecha
     */
    public String getFecha() {
        return fecha;
    }

    /**
     * @param fecha the fecha to set
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    /**
     * @return the hashtag
     */
    public String getHashtag() {
        return hashtag;
    }

    /**
     * @param hashtag the hashtag to set
     */
    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }
}
