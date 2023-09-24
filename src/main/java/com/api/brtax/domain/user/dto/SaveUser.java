package com.api.brtax.domain.user.dto;

import com.api.brtax.domain.user.UserType;

public record SaveUser(String name, String cpf, String password, UserType type) {
}
