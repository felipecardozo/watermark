package com.watermark.service;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import com.watermark.model.Document;
import com.watermark.model.Status;
import com.watermark.model.Ticket;
import com.watermark.model.TopicType;
import com.watermark.model.WatermarkBook;
import com.watermark.model.WatermarkProperty;
import com.watermark.repository.WatermarkRepository;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class DefaultWatermarkService implements WatermarkService {

    private WatermarkRepository watermarkRepository;

    @Autowired
    public DefaultWatermarkService(final WatermarkRepository watermarkRepository) {
        this.watermarkRepository = watermarkRepository;
    }

    @Override
    @Async
    public Future<List<Document>> getAllDocuments() {
        try {
            return new AsyncResult<>(watermarkRepository.getAllDocuments());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        return new AsyncResult<>(Collections.emptyList());
    }

    @Override
    @Async
    public Future<Document> getDocumentByTicket(Ticket ticket) {
        return new AsyncResult<>(watermarkRepository.findById(ticket.getTicket()));
    }

    @Override
    @Async
    public Future<Ticket> createDocument(Document document) {
        String ticket = "";
        try {
            documentHasWatermarkProperty(document);
            watermarkHasValidTopic(document.getWatermarkProperty());
            ticket = watermarkRepository.saveDocument(document);
            return new AsyncResult<>(new Ticket(ticket));
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }

    @Override
    @Async
    public Future<Document> updateDocument(Ticket ticket, Document document) {
        Document doc = watermarkRepository.findById(ticket.getTicket());
        if (doc != null) {
            document.setTicket(ticket);
            documentHasWatermarkProperty(document);
            try {
                return new AsyncResult<Document>(watermarkRepository.updateDocument(document));
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }
        return new AsyncResult<Document>(null);
    }

    private void watermarkHasValidTopic(WatermarkProperty watermarkProperty) {
        if (watermarkProperty instanceof WatermarkBook) {
            WatermarkBook book = (WatermarkBook) watermarkProperty;
            if (StringUtils.isNotBlank(book.getTopic())) {
                TopicType topic = TopicType.valueOf(book.getTopic().toUpperCase());
                if (topic == null) {
                    log.warn(book.getTopic() + " Is not a valid topic");
                    throw new IllegalStateException(book.getTopic() + " Is not a valid topic");
                }
            }
        }
    }

    private void documentHasWatermarkProperty(Document document) {
        if (document.getWatermarkProperty() == null
                || (document.getWatermarkProperty() != null && !isPropertyCompleted(document.getWatermarkProperty()))) {
            document.setStatus(Status.INPROGRESS);
        } else if (isPropertyCompleted(document.getWatermarkProperty())) {
            document.setStatus(Status.FINISHED);
        }
    }

    private boolean isPropertyCompleted(WatermarkProperty property) {
        if (property instanceof WatermarkBook) {
            WatermarkBook book = (WatermarkBook) property;
            return StringUtils.isNotBlank(book.getAuthor()) && StringUtils.isNotBlank(book.getContent()) && StringUtils.isNotBlank(book.getTitle())
                    && StringUtils.isNotBlank(book.getTopic());
        } else {
            return StringUtils.isNotBlank(property.getAuthor()) && StringUtils.isNotBlank(property.getContent())
                    && StringUtils.isNotBlank(property.getTitle());
        }
    }

}
