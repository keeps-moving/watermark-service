package com.springer.nature.watermark.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.Document;
import com.springer.nature.watermark.core.model.Journal;
import com.springer.nature.watermark.core.model.Topic;

public class DocumentTest {
    
    private Document book1;
    
    private Document book2;
    
    private Document book3;
    
    private Document journal1;
    
    private Document journal2;
    
    private Document journal3;
    
    @Before
    public void setUp(){
	book1 = new Book("1","The Title","Ana Silva",Topic.BUSINESS);
	book2 = new Book("1","The Title","Ana Silva",Topic.BUSINESS);
	book3 = new Book("2","The Title Bla","Maria Silva",Topic.MEDIA);
	
	journal1 = new Journal("3","Journal 1","Ana Silva");
	journal2 = new Journal("3","Journal 1","Ana Silva");
	journal3 = new Journal("4","Journal Bla","Ana Silva");
    }
    
    @Test
    public void equalsTest(){
	assertTrue(book1.equals(book2));
	assertFalse(book1.equals(book3));
	assertTrue(journal1.equals(journal2));
	assertFalse(journal1.equals(journal3));
    }
    
    @Test
    public void hashCodeTest(){
	assertEquals(book1.hashCode(), book2.hashCode());
	assertEquals(journal1.hashCode(), journal2.hashCode());
	assertNotEquals(book1.hashCode(), book3.hashCode());
	assertNotEquals(journal1.hashCode(), journal3.hashCode());
    }

}
