package com.springer.nature.watermark.service.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.Document;
import com.springer.nature.watermark.core.model.Journal;
import com.springer.nature.watermark.service.Watermarker;



/**
 * REST Service providing interface for {@link com.springer.nature.watermark.service.Watermarker}.
 */
@RestController
@RequestMapping(value = "/services")
public class WatermarkController {

    @Autowired
    private Watermarker watermarker;

    @RequestMapping(value = "/watermark/status/{ticket}", method = RequestMethod.GET)
    public @ResponseBody String getWatermarkingStatus(@PathVariable String ticket) {
	return watermarker.isProcessing(ticket) ? "RUNNING" : "NO_RUNNING";
    }

    @RequestMapping(value = "/watermark/book", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String sendBookWatermarkRequest(@RequestBody Book book) {
	return watermarker.requestWatermark(book);
    }

    @RequestMapping(value = "/watermark/journal", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody String sendJournalWatermarkRequest(@RequestBody Journal journal) {
	return watermarker.requestWatermark(journal);
    }

    @RequestMapping(value = "/watermark/{ticket}", method = RequestMethod.GET)
    public @ResponseBody Document getDocument(@PathVariable String ticket) {
	return watermarker.getDocument(ticket);
    }

}
