package com.watermark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WatermarkProperty {

    @Getter
    @Setter
    private String content;
    @Getter
    @Setter
    private String title;
    @Getter
    @Setter
    private String author;

}
