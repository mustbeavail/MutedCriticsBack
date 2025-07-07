package com.mutedcritics.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mail_file")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MailFile {

    @EmbeddedId
    private MailFileId id;
}