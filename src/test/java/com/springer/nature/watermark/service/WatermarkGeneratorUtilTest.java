package com.springer.nature.watermark.service;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.DocumentType;
import com.springer.nature.watermark.core.model.Journal;
import com.springer.nature.watermark.core.model.Topic;

public class WatermarkGeneratorUtilTest {    
    
    WatermarkGeneratorUtil wg;
    
    @Before
    public void setUp(){
	wg = new WatermarkGeneratorUtil();
	wg.setDelay(300); //setting a default delay
    }
    
    @Test
    public void generateBookWatermarkTest() throws Exception{	
	Book book = new Book("12#345_999", "Title Title", "Author Name", Topic.MEDIA );
	
	Book doc = (Book) wg.generate(book).get();
	
	assertEquals("12#345_999", doc.getId());
	assertEquals("Title Title", doc.getTitle());
	assertEquals("Author Name", doc.getAuthor());
	assertEquals(Topic.MEDIA, doc.getTopic());
	assertNotNull(doc.getWatermark());
	
	HashMap<String, String> watermarkExpected = buildExpectedWatermark(DocumentType.BOOK.name());
	watermarkExpected.put("topic", book.getTopic().name());
	
	assertEquals(watermarkExpected, doc.getWatermark());	
    }
    
    @Test
    public void generateJournalWatermarkTest() throws Exception{
	Journal journal = new Journal("12#345_999", "Title Title", "Author Name");
	
	Journal doc = (Journal) wg.generate(journal).get();
	
	assertEquals("12#345_999", doc.getId());
	assertEquals("Title Title", doc.getTitle());
	assertEquals("Author Name", doc.getAuthor());	
	assertNotNull(doc.getWatermark());
	
	assertEquals(buildExpectedWatermark(DocumentType.JOURNAL.name()), doc.getWatermark());	
    }
    
    @Test(expected=AssertionError.class)
    public void generateBookWatermarkErrorTest() throws Exception{
	 wg.generate(new Book("1", "some title","some author", null));	
    }
    
    @Test(expected=AssertionError.class)
    public void generateJournalWatermarkErrorTest() throws Exception{
	 wg.generate(new Journal("1", "some title", null));
    }
    
    private HashMap<String, String> buildExpectedWatermark(String type){
	HashMap<String, String> expectedWatermark = new HashMap<>();
	expectedWatermark.put("content", type);
	expectedWatermark.put("title", "Title Title");
	expectedWatermark.put("author", "Author Name");
	
	return expectedWatermark;
    } 


}
