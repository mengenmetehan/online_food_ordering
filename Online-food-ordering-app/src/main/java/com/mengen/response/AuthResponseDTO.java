package com.mengen.response;

import com.mengen.enums.USER_ROLE;

public record AuthResponseDTO(String jwt, String message, USER_ROLE role) { }