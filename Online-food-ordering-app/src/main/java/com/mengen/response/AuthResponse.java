package com.mengen.response;

import com.mengen.enums.USER_ROLE;

public record AuthResponse(String jwt, String message, USER_ROLE role) { }