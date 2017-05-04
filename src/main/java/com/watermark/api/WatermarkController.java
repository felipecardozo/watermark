package com.watermark.api;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.watermark.mediator.DocumentMediator;
import com.watermark.model.Document;
import com.watermark.model.Ticket;
import com.watermark.service.WatermarkService;
import com.watermark.view.DocumentView;

@RestController
public class WatermarkController {

    private WatermarkService watermarkService;
    private DocumentMediator documentMediator;

    @Autowired
    public WatermarkController(final WatermarkService watermarkService, final DocumentMediator documentMediator) {
        this.watermarkService = watermarkService;
        this.documentMediator = documentMediator;
    }

    @GetMapping("/")
    public List<Document> getAllDocuments() throws InterruptedException, ExecutionException {
        return this.watermarkService.getAllDocuments().get();
    }

    @GetMapping("/{ticket}")
    public Document getDocumentByTicket(@PathVariable String ticket) throws InterruptedException, ExecutionException {
        return this.watermarkService.getDocumentByTicket(new Ticket(ticket)).get();
    }

    @PostMapping("/")
    public Ticket createDocument(@RequestBody DocumentView documentView) throws Exception {
        return this.watermarkService.createDocument(documentMediator.convertoToDocument(documentView)).get();
    }

    @PutMapping("/{ticket}")
    public Document updateDocument(@PathVariable String ticket, @RequestBody DocumentView documentView) throws Exception {
        return this.watermarkService.updateDocument(new Ticket(ticket), documentMediator.convertoToDocument(documentView)).get();
    }


}
