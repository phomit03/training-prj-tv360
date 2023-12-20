package com.example.tv360.dto;

import com.example.tv360.utils.MapToModel;
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
	@MapToModel("username")
	private String username;

	@MapToModel("fullName")
	private String fullName;

	@MapToModel("phone")
	private String phone;

	@MapToModel("email")
	private String email;

	@MapToModel("password")
	private String password;

	@MapToModel("status")
	private Integer status;

	@MapToModel("created_at")
	private Timestamp created_at;

	@MapToModel("updated_at")
	private Timestamp updated_at;

}
