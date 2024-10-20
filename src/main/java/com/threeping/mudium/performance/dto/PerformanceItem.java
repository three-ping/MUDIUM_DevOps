package com.threeping.mudium.performance.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@XmlRootElement(name = "db")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString
public class PerformanceItem {

    @XmlElement(name = "prfpdfrom")
    private String startDate;

    @XmlElement(name = "prfpdto")
    private String endDate;

    @XmlElement(name = "fcltynm")
    private String theater;

    @XmlElement(name = "area")
    private String region;

    @XmlElement(name = "prfcast")
    private String actorList;

    @XmlElement(name = "prfruntime")
    private String runTime;

    @XmlElement(name = "prfage")
    private String age;

    @XmlElement(name = "poster")
    private String poster;

    @XmlElement(name = "entrpsnm")
    private String entrps;

}
