package com.carpool.partyMatch.domain;

import java.io.Serializable;

import lombok.*;

import javax.persistence.*;
import java.util.List;
//import javax.persistence.OneToMany;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "match_info")
@Builder
public class MatchInfo extends BaseEntity {

  @Id @GeneratedValue
  private Long id;

  @Column(name = "party_info_id")
  private Long partyInfoId;

  @Column(name = "user_id")
  private String userId;

  @Column(name = "match_status")
  private MatchStatus matchStatus;

  public boolean isUser(String userId) {
    return this.getUserId().equals(userId);
}

  public static MatchInfo of(Long partyInfoId, String userId, MatchStatus matchStatus){
    return MatchInfo.builder()
            .partyInfoId(partyInfoId)
            .userId(userId)
            .matchStatus(matchStatus)
            .build();
  }


}
