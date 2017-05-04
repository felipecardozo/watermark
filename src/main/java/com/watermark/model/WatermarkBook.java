package com.watermark.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class WatermarkBook extends WatermarkProperty {

    @Getter
    @Setter
    private String topic;

    @Override
    public String toString() {
        return super.toString() + "topic=" + topic;
    }

}
