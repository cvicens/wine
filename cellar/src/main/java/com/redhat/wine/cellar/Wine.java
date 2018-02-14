package com.redhat.wine.cellar;

import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;

@EqualsAndHashCode(exclude={"id"})
public class Wine {

    @Id
    public String id;

    public WineType type;
    public Integer vintage;
    public String producer;
    public String region;
    public String name;
    public String country;
    public String grape;
    public String colour;
    public String aroma;
    public String taste;
    public String alcohol;

    public Wine() {}

    public Wine(WineType type, Integer vintage, String producer, String region, String name, String country, String grape, String colour, String aroma, String taste, String alcohol) {
        this.type = type;
        this.vintage = vintage;
        this.producer = producer;
        this.region = region;
        this.name = name;
        this.country = country;
        this.grape = grape;
        this.colour = colour;
        this.aroma = aroma;
        this.taste = taste;
        this.alcohol = alcohol;
    }

    @Override
    public String toString() {
        return String.format(
                "Wine[id=%s, type='%s', producer='%s', region='%s', name='%s', vintage='%s' country='%s']",
                id, type, producer, region, name, vintage, country);
    }

}

