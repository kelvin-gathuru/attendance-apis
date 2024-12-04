package com.attendance.attendance.settings.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JwtUser {
    private Long userID;
    private String email;
    private String name;
}
