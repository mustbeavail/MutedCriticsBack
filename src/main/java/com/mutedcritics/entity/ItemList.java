package com.mutedcritics.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

// 단일 아이템 리스트
@Entity
@Data
@Table(name = "item_list")
public class ItemList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_idx")
    private int itemIdx;

    @Column(name = "item_cate", length = 100)
    private String itemCate;

    @Column(name = "item_name", length = 50)
    private String itemName;

    @Column(name = "item_price", length = 6)
    private int itemPrice;

    @Column(name = "sell_type", length = 50)
    private String sellType;

    @Column(name = "sell_start_date")
    private LocalDate sellStartDate;

    @Column(name = "sell_end_date")
    private LocalDate sellEndDate;

    // 이 아이템을 보유한 영웅들 (역방향 관계)
    // Heroes 엔티티의 items 필드와 연결됨 (heroes_item 테이블 사용)
    @ManyToMany(mappedBy = "items")
    private List<Heroes> heroes;

    // 이 아이템이 포함된 묶음상품들 (역방향 관계)
    // BundleItem 엔티티의 items 필드와 연결됨 (bundle_content 테이블 사용)
    @ManyToMany(mappedBy = "items")
    private List<BundleItem> bundles;
}
