package com.watermark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Document {

    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String author;
    @Getter
    @Setter
    private WatermarkProperty watermarkProperty;
    @Getter
    @Setter
    private Ticket ticket;
    @Getter
    @Setter
    private Status status;

}
