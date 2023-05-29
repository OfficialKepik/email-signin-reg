package com.example.email;

import lombok.Data;

@Data
public class User {
    private String name;
    private String email;
    private String password;
    private String password2;
    private boolean enabled = false;
}
