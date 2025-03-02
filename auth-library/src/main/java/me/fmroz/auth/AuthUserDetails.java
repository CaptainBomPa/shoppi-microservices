package me.fmroz.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthUserDetails {
    private String username;
    private AccountType role;
}
