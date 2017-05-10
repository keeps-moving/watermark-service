package com.springer.nature.watermark.core.model;

import java.util.HashMap;

import org.junit.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Book extends Document {    
    
    private Topic topic;    
      
    public Book(String id, String title, String author, Topic topic, HashMap<String, String> watermark) {
        super(id, title, author, watermark);
        Assert.assertNotNull("Null not allowed", topic);
        this.topic = topic;
    }
    
    @JsonCreator
    public Book(@JsonProperty("id") String id,
                @JsonProperty("title") String title,
                @JsonProperty("author") String author,
                @JsonProperty("topic") Topic topic) {
        this(id, title, author, topic, null);
    }


    public void setTopic(Topic topic) {	
	this.topic = topic;	
    }
    
    public Topic getTopic() {
	return topic;	
    }
        
}
