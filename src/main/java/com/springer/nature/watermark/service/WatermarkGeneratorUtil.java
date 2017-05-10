package com.springer.nature.watermark.service;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.Document;
import com.springer.nature.watermark.core.model.DocumentType;
import com.springer.nature.watermark.core.model.Journal;


/**
 * Component responsible for generate and set a watermark in a document
 */
@Component
public class WatermarkGeneratorUtil {

    @Value("${milliseconds.delay:500}")
    private long delay;

    public void setDelay(long delay) {
	this.delay = delay;
    }

    @Async
    public Future<Document> generate(Document document) {

	try {
	    TimeUnit.MILLISECONDS.sleep(delay);
	} catch (InterruptedException ex) {
	    // nothing to do
	}
	
	//Could be an interface being called, so I'd have 2 implementations for build a watermark. But I prefer don't implement it.
	HashMap<String, String> watermark = null;
	if (document instanceof Book) {
	    Book b = (Book) document;
	    watermark = buildBookWatermark(b);
	}
	if (document instanceof Journal) {
	    Journal j = (Journal) document;
	    watermark = buildJournalWatermark(j);
	}
	document.setWatermark(watermark);

	return new AsyncResult<>(document);

    }

    private HashMap<String, String> buildBookWatermark(Book book) {
	HashMap<String, String> watermark = new HashMap<>();
	watermark.put("content", DocumentType.BOOK.name());
	watermark.put("title", book.getTitle());
	watermark.put("author", book.getAuthor());
	watermark.put("topic", book.getTopic().name());

	return watermark;
    }

    private HashMap<String, String> buildJournalWatermark(Journal journal) {
	HashMap<String, String> watermark = new HashMap<>();
	watermark.put("content", DocumentType.JOURNAL.name());
	watermark.put("title", journal.getTitle());
	watermark.put("author", journal.getAuthor());

	return watermark;
    }

}
