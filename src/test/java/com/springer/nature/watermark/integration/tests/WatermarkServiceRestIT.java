package com.springer.nature.watermark.integration.tests;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import static org.junit.Assert.assertEquals;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.DocumentType;
import com.springer.nature.watermark.core.model.Journal;
import com.springer.nature.watermark.core.model.Topic;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


/**
 * Tests the full flow, started in the rest interface -{@link com.springer.nature.watermark.service.rest.WatermarkController}.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class WatermarkServiceRestIT {
     
    private String baseUrl =  "/services/watermark";
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    private int millisecondsSleeping = 500;  
  
    @Test
    public void watermarkingBook() throws Exception{	
	
	 // difere no metodo sobrescrito
	
	System.out.println(new Gson().toJson(new Book("123","Title1","Author Author", Topic.SCIENCE)));
	//sending a request for watermarking a book
	ResponseEntity<String> postBook = restTemplate.postForEntity(baseUrl + "/book", new Book("123","Title1","Author Author", Topic.SCIENCE), String.class);
	
	//getting the ticket used for checking the status of processing
	String ticket = postBook.getBody();
	
	String status = "PROCESSING";
	while("PROCESSING".equalsIgnoreCase(status)){
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
	
	String status = "PROCESSING";
	while("PROCESSING".equalsIgnoreCase(status)){
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
