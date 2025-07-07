package com.mutedcritics.entity;

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
@Table(name = "heroes")
public class Heroes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heroes_idx")
    private int heroesIdx;

    @Column(name = "heroes_name", length = 50)
    private String heroesName;

    @Column(name = "role", length = 10)
    private String role;

    // 영웅별 전용 아이템 관리 (다대다 관계)
    // heroes_item 테이블을 통해 ItemList와 연결
    // 각 영웅은 전용 아이템들을 보유하며, 향후 획득시간/강화레벨 등 추가 정보 확장 가능
    @ManyToMany
    @JoinTable(name = "heroes_item", joinColumns = @JoinColumn(name = "heroes_idx"), inverseJoinColumns = @JoinColumn(name = "item_idx"))
    private List<ItemList> items;

}
