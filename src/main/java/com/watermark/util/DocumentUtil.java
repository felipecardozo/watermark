package com.watermark.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.watermark.model.ContentType;
import com.watermark.model.Document;
import com.watermark.model.Status;
import com.watermark.model.Ticket;
import com.watermark.model.TopicType;
import com.watermark.model.WatermarkBook;

public final class DocumentUtil {

    public final static List<Document> addData() {
        List<Document> data = new ArrayList<>();
        Document doc1 = new Document();
        doc1.setAuthor("Bruce Wayne");
        doc1.setTicket(generateTicket());
        doc1.setTitle("The Dark Code");
        doc1.setStatus(Status.FINISHED);
        WatermarkBook watermarkProperty1 = new WatermarkBook();
        watermarkProperty1.setAuthor("Bruce Wayne");
        watermarkProperty1.setContent(ContentType.BOOK.toString().toLowerCase());
        watermarkProperty1.setTitle("The Dark Code");
        watermarkProperty1.setTopic(StringUtils.capitalize(TopicType.SCIENCE.toString().toLowerCase()));
        doc1.setWatermarkProperty(watermarkProperty1);

        Document doc2 = new Document();
        doc2.setAuthor("Dr. Evil");
        doc2.setTicket(generateTicket());
        doc2.setTitle("How to make money");
        doc2.setStatus(Status.FINISHED);
        WatermarkBook watermarkProperty2 = new WatermarkBook();
        watermarkProperty2.setAuthor("Dr. Evil");
        watermarkProperty2.setContent(ContentType.BOOK.toString().toLowerCase());
        watermarkProperty2.setTitle("How to make money");
        watermarkProperty2.setTopic(StringUtils.capitalize(TopicType.BUSINESS.toString().toLowerCase()));
        doc2.setWatermarkProperty(watermarkProperty2);

        Document doc3 = new Document();
        doc3.setAuthor("Clark Kent");
        doc3.setTicket(generateTicket());
        doc3.setTitle("Journal of human flight routes");
        doc3.setStatus(Status.FINISHED);
        WatermarkBook watermarkProperty3 = new WatermarkBook();
        watermarkProperty3.setAuthor("Clark Kent");
        watermarkProperty3.setContent(ContentType.JOURNAL.toString().toLowerCase());
        watermarkProperty3.setTitle("Journal of human flight routes");
        watermarkProperty3.setTopic(StringUtils.capitalize(TopicType.SCIENCE.toString().toLowerCase()));
        doc3.setWatermarkProperty(watermarkProperty3);

        data.add(doc1);
        data.add(doc2);
        data.add(doc3);
        return data;
    }

    public static Ticket generateTicket() {
        return new Ticket(UUID.randomUUID().toString().replace("-", ""));
    }

}
