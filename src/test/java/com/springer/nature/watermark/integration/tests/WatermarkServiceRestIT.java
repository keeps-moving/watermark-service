package com.springer.nature.watermark.integration.tests;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.DocumentType;
import com.springer.nature.watermark.core.model.Journal;
import com.springer.nature.watermark.core.model.Topic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Tests the full flow, started in the rest interface -{@link com.springer.nature.watermark.service.rest.WatermarkController}.
 */
public class WatermarkServiceRestIT {
    
    private String baseUrl =  "http://localhost:8080/services/watermark";
    
    private RestTemplate restTemplate = new RestTemplate();
    
    private int millisecondsSleeping = 500;
    
       
    @Test
    public void watermarkingBook() throws Exception{	
	//sending a request for watermarking a book
	ResponseEntity<String> postBook = restTemplate.postForEntity(baseUrl + "/book", new Book("123","Title1","Author Author", Topic.SCIENCE), String.class);
	
	//getting the ticket used for checking the status of processing
	String ticket = postBook.getBody();
	
	String status = "RUNNING";
	while("RUNNING".equalsIgnoreCase(status)){
	    TimeUnit.MILLISECONDS.sleep(millisecondsSleeping);
	    //Checking if the generation of the watermark has ended
	    status = restTemplate.getForObject(baseUrl + "/status/" + ticket, String.class);	    
	}
	String result = restTemplate.getForObject(baseUrl + "/" + ticket, String.class);
	Book book = new Gson().fromJson(result, new TypeToken<Book>() {
	        }.getType());
	
	assertEquals("123", book.getId());
	assertEquals("Title1", book.getTitle());
	assertEquals("Author Author", book.getAuthor());
	assertEquals(Topic.SCIENCE, book.getTopic());
	
	HashMap<String, String> watermarkExpected = buildExpectedWatermark(DocumentType.BOOK.name());
	watermarkExpected.put("topic", book.getTopic().name());
	assertEquals(watermarkExpected, book.getWatermark());
    }
    
    @Test
    public void watermarkingJournal() throws Exception{	
	//sending a request for watermarking a journal
	ResponseEntity<String> postJournal = restTemplate.postForEntity(baseUrl + "/journal", new Journal("123","Title1","Author Author"), String.class);
	
	//getting the ticket used for checking the status of processing
	String ticket = postJournal.getBody();
	
	String status = "RUNNING";
	while("RUNNING".equalsIgnoreCase(status)){
	    TimeUnit.MILLISECONDS.sleep(millisecondsSleeping);
	    status = restTemplate.getForObject(baseUrl + "/status/" + ticket, String.class);	    
	}
	String result = restTemplate.getForObject(baseUrl + "/" + ticket, String.class);
	Journal journal = new Gson().fromJson(result, new TypeToken<Journal>() {
	        }.getType());
	
	assertEquals("123", journal.getId());
	assertEquals("Title1", journal.getTitle());
	assertEquals("Author Author", journal.getAuthor());
	assertEquals(buildExpectedWatermark(DocumentType.JOURNAL.name()), journal.getWatermark());
    }
       
    private HashMap<String, String> buildExpectedWatermark(String type){
  	HashMap<String, String> expectedWatermark = new HashMap<>();
  	expectedWatermark.put("content", type);
  	expectedWatermark.put("title", "Title1");
  	expectedWatermark.put("author", "Author Author");
  	  	
  	return expectedWatermark;
      } 
}
