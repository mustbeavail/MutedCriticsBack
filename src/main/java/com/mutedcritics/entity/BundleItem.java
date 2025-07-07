package com.mutedcritics.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "bundle_item")
public class BundleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bundle_idx")
    private int BundleIdx;

    @Column(name = "bundle_price", length = 6)
    private int bundlePrice;

    @Column(name = "bundle_name", length = 100)
    private String bundleName;

    @Column(name = "sell_type", length = 50)
    private String sellType;

    @Column(name = "sell_start_date")
    private LocalDate sellStartDate;

    @Column(name = "sell_end_date")
    private LocalDate sellEndDate;

    // 묶음상품에 포함된 아이템들 (다대다 관계)
    // bundle_content 테이블을 통해 ItemList와 연결
    // 한 묶음상품은 여러 아이템을 포함할 수 있고, 한 아이템은 여러 묶음상품에 포함될 수 있음
    @ManyToMany
    @JoinTable(name = "bundle_content", joinColumns = @JoinColumn(name = "bundle_idx"), inverseJoinColumns = @JoinColumn(name = "item_idx"))
    private List<ItemList> items;

}
