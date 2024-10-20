package com.threeping.mudium.performance.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@XmlRootElement(name = "boxofs")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class RankResponse {

    @XmlElement(name = "basedate")
    private String baseDate;

    @XmlElement(name = "boxof")
    private List<RankItem> rankItems;

}
