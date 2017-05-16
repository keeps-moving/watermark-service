package com.springer.nature.watermark.integration.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.Topic;
import com.springer.nature.watermark.service.WatermarkGeneratorUtil;
import com.springer.nature.watermark.service.Watermarker;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/WatermarkServiceIT-context.xml")
public class WatermarkServiceIT {
    
    @Autowired
    Watermarker service;
    
    @Autowired
    WatermarkGeneratorUtil g;
    
    @Autowired
    ThreadPoolTaskExecutor taskExecutor;
    
    long milisseconds = 2000;
    
    @Before
    public void setup(){
	g.setDelay(milisseconds);
	service.setGenerator(g);
    }

    @Test
    public void asyncTest() throws InterruptedException, ExecutionException {	
		
	Callable<String> ticket1 = () -> {
	    	System.out.println(Thread.currentThread().getName() + " is running callable 1");
		return service.requestWatermark(new Book("1","The Title","Ana Silva",Topic.BUSINESS));		
	};
	
	Callable<String> ticket2 = () -> {
	    	System.out.println(Thread.currentThread().getName() + " is running callable 2");
		return service.requestWatermark(new Book("2","The Title","Ana Silva",Topic.BUSINESS));		
	};
	
	Callable<String> ticket3 = () -> {
	    	System.out.println(Thread.currentThread().getName() + " is running callable 3");
		return service.requestWatermark(new Book("3","The Title","Ana Silva",Topic.BUSINESS));		
	};
		
	Future<String> result1 = taskExecutor.submit(ticket1);
	Future<String> result2 = taskExecutor.submit(ticket2);
	Future<String> result3 = taskExecutor.submit(ticket3);
	
	String ticket_1 = result1.get().toString();
	String ticket_2 = result2.get().toString();
	String ticket_3 = result3.get().toString();
	
	assertTrue(ticket_1, service.isProcessing(ticket_1));
	assertTrue(ticket_2, service.isProcessing(ticket_2));
	assertTrue(ticket_3, service.isProcessing(ticket_3));
	
	TimeUnit.MILLISECONDS.sleep(milisseconds * 3);
	
	assertFalse(ticket_1, service.isProcessing(ticket_1));
	assertFalse(ticket_2, service.isProcessing(ticket_2));
	assertFalse(ticket_3, service.isProcessing(ticket_3));
	
	assertEquals("1", service.getDocument(ticket_1).getId());
	assertEquals("2", service.getDocument(ticket_2).getId());
	assertEquals("3", service.getDocument(ticket_3).getId());
    }
}
