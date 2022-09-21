package com.carpool.partyMatch.domain;

import java.io.Serializable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Embedded;
import javax.persistence.AttributeOverride;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import java.util.List;
//import javax.persistence.OneToMany;

@Entity
@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchInfo extends BaseEntity {

  @Id @GeneratedValue
  private Long id;

  private Long partyInfoId;

  private String userId;

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
