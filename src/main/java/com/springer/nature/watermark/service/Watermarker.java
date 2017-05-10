package com.springer.nature.watermark.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.springer.nature.watermark.core.model.Document;


/**
 * Service responsible for watermarking documents asynchronously.
 * For generating watermarks it depends on  * {@link com.springer.nature.watermark.service.WatermarkGeneratorUtil}
 */
@Service
public class Watermarker {

    private final static Logger log = LoggerFactory.getLogger(Watermarker.class);
    
    private WatermarkGeneratorUtil generator;

    @Autowired
    public void setGenerator(WatermarkGeneratorUtil generator) {
        this.generator = generator;
    }

    private ConcurrentMap<String, Future<Document>> watermarkedsMap = new ConcurrentHashMap<>();

    private ConcurrentMap<String, Boolean> waitingsMap = new ConcurrentHashMap<>();

    public Document getDocument(String ticket) {
	try {
	    Future<Document> future = watermarkedsMap.get(ticket);
	    return future != null && future.isDone() ? future.get(0, TimeUnit.MILLISECONDS) : null;
	} catch (Exception e) {
	    log.error("Error getting document. Ticket {} ", ticket, e);
	}
	log.debug("Ticket {} not found.", ticket);
	return null;
    }

    public boolean isProcessing(String ticket) {
	Future<Document> future = watermarkedsMap.get(ticket);
	return future != null && !future.isDone() && !future.isCancelled();
    }

    /**
     * Responsible for putting requests for watermark, represented by a generated ticket, in a thread-safe hashmap first, for control. After this,
     *  the ticket is inserted as a key in another thread-safe hashmap where the value is the processed watermark.
     * @param document - document from which a ticket is being requested
     *
     * @return the ticket
     */
    public String requestWatermark(Document document) {
	String ticket = new StringBuilder().append("TKT_").append(System.currentTimeMillis()).append(document.getId().hashCode()).toString();
	try {
	    if (waitingsMap.putIfAbsent(ticket, Boolean.TRUE) == null && !watermarkedsMap.containsKey(ticket)) {
		watermarkedsMap.put(ticket, generator.generate(document));
	    }

	} catch (Exception ex) {
	    log.warn("Error requesting watermark - Doc. {}", document.getId());
	} finally {
	    waitingsMap.remove(ticket);
	}
	return ticket;
    }

}
