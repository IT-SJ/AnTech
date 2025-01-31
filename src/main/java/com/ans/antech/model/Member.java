package com.ans.antech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Member {
  private String id;          // 매핑: ID
  private String pw;          // 매핑: PW
  private String email;       // 매핑: EMAIL
  private String profile_Img;  // 매핑: PROFILE_IMG
}
