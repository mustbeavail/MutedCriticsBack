package com.mutedcritics.config;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

/**
 * QueryDSL 설정 클래스입니다.
 * JPAQueryFactory를 스프링 빈으로 등록하여, 어디서든 의존성 주입 받아 사용할 수 있도록 합니다.
 */
@Configuration
public class QueryDslConfig {

    /**
     * JPA의 핵심 인터페이스인 EntityManager를 주입받습니다.
     * PersistenceContext는 스프링이 JPA EntityManager를 관리하도록 하는 어노테이션입니다.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * JPAQueryFactory를 Bean으로 등록합니다.
     * QueryDSL을 사용할 때 이 팩토리를 통해 쿼리 객체를 생성할 수 있습니다.
     *
     * @return JPAQueryFactory 인스턴스
     */
    @Bean
    public JPAQueryFactory factory() {
        return new JPAQueryFactory(entityManager);
    }

}
