package com.springer.nature.watermark.core.model;

import java.util.HashMap;

import org.junit.Assert;

public abstract class Document {

    private String id;

    private String title;

    private String author;

    private HashMap<String, String> watermark;
    
    public Document(String id, String title, String author) {
	Assert.assertNotNull("Null not allowed", id);
	Assert.assertNotNull("Null not allowed", title);
	Assert.assertNotNull("Null not allowed", author);
	this.id = id;
	this.title = title;
	this.author = author;	
    }

    public String getId() {
	return id;
    }

    public String getTitle() {
	return title;
    }

    public String getAuthor() {
	return author;
    }

    public HashMap<String, String> getWatermark() {
	return watermark;
    }

    public void setId(String id) {
	this.id = id;
    }

    

    public void setTitle(String title) {
	this.title = title;
    }

    public void setAuthor(String author) {
	this.author = author;
    }

    public void setWatermark(HashMap<String, String> watermark) {
	this.watermark = watermark;
    }

    // sobrescrita padrão do hashCode pra objeto
    @Override
    public final int hashCode() {
	return id.hashCode();
    }

    // sobrescrita padrao do equals pra objeto
    @Override
    public final boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}	
	if (obj == null || !(obj instanceof Document)) {
	    return false;
	}
	Document document = (Document) obj;

	return id.equals(document.id);
    }
}
