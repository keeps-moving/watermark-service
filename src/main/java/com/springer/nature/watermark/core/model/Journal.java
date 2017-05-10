package com.springer.nature.watermark.core.model;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Journal extends Document {


    public Journal(String id, String title, String author, HashMap<String, String> watermark) {
        super(id, title, author, watermark);       
    }
    
    @JsonCreator
    public Journal(@JsonProperty("id") String id,
                   @JsonProperty("title") String title,
                   @JsonProperty("author") String author) {
        this(id, title, author, null);
    }
    
}
