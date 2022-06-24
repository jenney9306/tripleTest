package com.triple.test.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "TR_PLACE")
public class TrPlace {
    @Id
    @Column(name = "PLACE_ID", length = 36, nullable = false)
    private String placeId;

}
