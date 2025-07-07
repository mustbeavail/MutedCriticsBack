package com.mutedcritics.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@EqualsAndHashCode
public class MailFileId implements Serializable {

    @Column(name = "mail_idx")
    private int mailIdx;

    @Column(name = "file_idx")
    private int fileIdx;
}