package com.example.tv360.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
	private String username;
	private String fullName;
	private String phone;
	private String email;
	private String password;
	private Integer status;
	private Timestamp created_at;
	private Timestamp updated_at;

}
