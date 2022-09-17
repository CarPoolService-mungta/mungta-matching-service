package com.carpool.partyMatch.client.dto;

import lombok.Data;

@Data
public class UserResponse {

  String userId;
  String name;
  String gender;
  String curPhoto;
  String partyRole;
  String userTeamName;

  public String getUserId() {
    return userId;
  }

}
