package com.mutedcritics.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mail_template")
public class MailTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tem_idx")
    private int temIdx;

    @Column(name = "tem_name", length = 50)
    private String temName;

    @Column(name = "tem_title", columnDefinition = "text")
    private String temTitle;

    @Lob
    @Column(name = "tem_body", columnDefinition = "longtext")
    private String temBody;

    @Column(name = "use_variable", columnDefinition = "text")
    private String useVariable;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
