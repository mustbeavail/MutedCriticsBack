package com.mutedcritics.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "member_token")
@NoArgsConstructor
@AllArgsConstructor
public class MemberToken {

    @Id
    @Column(name = "member_id", nullable = false)
    private String memberId;

    @Column(name = "token", length = 512, nullable = false)
    private String token;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

}
