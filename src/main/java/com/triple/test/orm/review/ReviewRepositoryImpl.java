package com.triple.test.orm.review;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
//@DependsOn(value = "reviewRepository")
public class ReviewRepositoryImpl {

    @Qualifier("entityManagerFactory")
    private EntityManager primaryEntityManager;

//    @Getter
//    @Autowired
//    private ReviewRepository reviewRepository;
}
