package com.threeping.mudium.performance.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dbs")
public class PerformanceResponse {

    @XmlElement(name = "db")
    PerformanceItem performanceItem;

    public PerformanceItem getPerformanceItem() {
        return performanceItem;
    }

    public void setPerformanceItem(PerformanceItem performanceItem) {
        this.performanceItem = performanceItem;
    }
}
