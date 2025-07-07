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
@Table(name = "refund_list")
public class RefundList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refund_idx")
    private int refundIdx;

    // 외래키(order_list 테이블의 order_idx)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_idx")
    private OrderList orderList;

    // 외래키(item_list 테이블의 item_idx)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_idx")
    private ItemList itemList;

    // 외래키(user 테이블의 user_id)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "refund_amt")
    private int refundAmt;

    @Column(name = "refund_date")
    private LocalDate refundDate;

    @Column(name = "why", length = 100)
    private String why;

}
