package com.watermark.mediator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.watermark.model.ContentType;
import com.watermark.model.Document;
import com.watermark.model.WatermarkBook;
import com.watermark.model.WatermarkJournal;
import com.watermark.view.DocumentView;
import com.watermark.view.WatermarView;

@Component
public class DocumentMediator {

    public Document convertoToDocument(DocumentView documentView) {
        Document document = new Document();
        if (StringUtils.isNotBlank(documentView.getAuthor())) {
            document.setAuthor(documentView.getAuthor());
        }
        if (StringUtils.isNotBlank(documentView.getTitle())) {
            document.setTitle(documentView.getTitle());
        }
        if (documentView.getWatermarkProperty() != null) {
            if (StringUtils.isNotBlank(documentView.getWatermarkProperty().getContent())) {
                String content = documentView.getWatermarkProperty().getContent();
                if (content.equalsIgnoreCase(ContentType.BOOK.toString())) {
                    document.setWatermarkProperty(createWatermarkBook(documentView.getWatermarkProperty()));
                } else if (content.equalsIgnoreCase(ContentType.JOURNAL.toString())) {
                    document.setWatermarkProperty(createWatermarkJournal(documentView.getWatermarkProperty()));
                }
            }
        }
        return document;
    }

    private WatermarkBook createWatermarkBook(WatermarView watermarkView) {
        WatermarkBook watermarkBook = new WatermarkBook();
        if (StringUtils.isNoneBlank(watermarkView.getAuthor())) {
            watermarkBook.setAuthor(watermarkView.getAuthor());
        }
        if (StringUtils.isNotBlank(watermarkView.getTitle())) {
            watermarkBook.setTitle(watermarkView.getTitle());
        }
        if (StringUtils.isNotBlank(watermarkView.getTopic())) {
            watermarkBook.setTopic(watermarkView.getTopic());
        }
        watermarkBook.setContent(ContentType.BOOK.toString().toLowerCase());
        return watermarkBook;
    }

    private WatermarkJournal createWatermarkJournal(WatermarView watermarkView) {
        WatermarkJournal watermarkJournal = new WatermarkJournal();
        if (StringUtils.isNoneBlank(watermarkView.getAuthor())) {
            watermarkJournal.setAuthor(watermarkView.getAuthor());
        }
        if (StringUtils.isNotBlank(watermarkView.getTitle())) {
            watermarkJournal.setTitle(watermarkView.getTitle());
        }
        watermarkJournal.setContent(ContentType.JOURNAL.toString().toLowerCase());
        return watermarkJournal;
    }

}
