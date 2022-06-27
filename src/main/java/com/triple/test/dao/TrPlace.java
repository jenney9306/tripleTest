package com.triple.test.dao;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "TR_PLACE")
public class TrPlace {
    @Id
    @Column(name = "PLACE_ID", length = 36, nullable = false)
    private String placeId;

}
