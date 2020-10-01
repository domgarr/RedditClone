package com.domgarr.RedditClone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {

    @NotBlank()
    @Size(min=8, max=20)
    @Pattern(regexp = "^[a-zA-Z1-9]+(_?)([a-zA-Z1-9]+)$")
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank()
    @Size(min=8, max=20)
    private String password;
}
