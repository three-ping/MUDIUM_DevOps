package com.threeping.mudium.musical.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Collections;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dbs")
public class MusicalListResponse {

    @XmlElement(name = "db")
    private List<MusicalItem> musicalItems;

    public List<MusicalItem> getMusicalItems() {
        return musicalItems != null ? musicalItems : Collections.emptyList();
    }

    public void setMusicalItems(List<MusicalItem> musicalItems) {
        this.musicalItems = musicalItems;
    }
}
