package com.watermark.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.watermark.model.Document;
import com.watermark.util.DocumentUtil;

import lombok.extern.log4j.Log4j;

@Repository
@Log4j
public class WatermarkRepository {

    private List<Document> data;

    public WatermarkRepository() {
        data = DocumentUtil.addData();
    }

    public String saveDocument(Document document) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        document.setTicket(DocumentUtil.generateTicket());
        data.add(document);
        Thread.sleep(internalProcessingMock());
        log.info("data " + data);
        long estimatedTime = System.currentTimeMillis() - startTime;
        log.info("TOTAL PROCESING..." + estimatedTime);
        return document.getTicket().getTicket();
    }

    public List<Document> getAllDocuments() throws InterruptedException {
        Thread.sleep(internalProcessingMock());
        return data;
    }

    private Long internalProcessingMock() {
        int i = (int) (Math.random() * 5);
        Long out = (long) i * 1000;
        log.info("waiting for ..." + i + " seconds");
        return out;
    }

    public Document findById(String ticket) {
        for (Document doc : data) {
            if (doc.getTicket().getTicket().equalsIgnoreCase(ticket)) {
                log.info("doc: " + doc);
                return doc;
            }
        }
        return null;
    }

    public Document updateDocument(Document document) throws InterruptedException {
        for (Document doc : data) {
            if (doc.getTicket().getTicket().equalsIgnoreCase(document.getTicket().getTicket())) {
                doc = document;
                Thread.sleep(internalProcessingMock());
                return doc;
            }
        }
        return null;
    }

}
