package com.api.brtax.domain.user.dto;

import com.api.brtax.domain.user.UserType;

public record UserDetails(String id, String name, String cpf, UserType type) {
}
