package com.mengen.request;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class LoginRequestDTO {
    String email;
    String password;
}