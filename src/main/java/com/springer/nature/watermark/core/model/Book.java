package com.springer.nature.watermark.core.model;

import org.junit.Assert;

public class Book extends Document {    
    
    private Topic topic;    
      
    public Book(String id, String title, String author, Topic topic) {
        super(id, title, author);
        Assert.assertNotNull("Null not allowed", topic);
        this.topic = topic;
    }

    public void setTopic(Topic topic) {	
	this.topic = topic;	
    }
    
    public Topic getTopic() {
	return topic;	
    }
        
}
