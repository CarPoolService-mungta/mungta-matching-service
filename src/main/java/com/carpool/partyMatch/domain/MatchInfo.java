package com.carpool.partyMatch.domain;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@Data
@NoArgsConstructor
public class MatchInfo extends BaseEntity {

  @Id @GeneratedValue
  Long id;

  Long partyInfoId;

  String userId;

  MatchStatus matchStatus;


}
