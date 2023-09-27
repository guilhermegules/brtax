package com.api.brtax.domain.user.dto;

import com.api.brtax.domain.user.UserType;

public record UpdateUser(String name, String cpf, String password, UserType type) {}
