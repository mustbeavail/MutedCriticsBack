package com.mutedcritics.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "order_list")
public class OrderList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_idx")
    private int orderIdx;

    // 외래키(user 테이블의 user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "item_idx")
    private int itemIdx;

    @Column(name = "item_cate", length = 100)
    private String itemCate;

    @Column(name = "order_date")
    private LocalDate orderDate;

}
