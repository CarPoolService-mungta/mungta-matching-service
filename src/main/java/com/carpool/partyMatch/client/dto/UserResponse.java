package com.carpool.partyMatch.client.dto;

import lombok.Data;

@Data
public class UserResponse {

  private String userId;
  private String userName;
  private String gender;
  private String userTeamName;
  private byte[] userPhoto;
  private String fileExtension;


}
