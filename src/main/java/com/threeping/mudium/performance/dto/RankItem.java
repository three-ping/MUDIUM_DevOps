package com.threeping.mudium.performance.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class RankItem {

    @XmlElement(name = "rnum")
    private Integer Rank;

    @XmlElement(name = "prfnm")
    private String title;

    @XmlElement(name = "area")
    private String area;

}
