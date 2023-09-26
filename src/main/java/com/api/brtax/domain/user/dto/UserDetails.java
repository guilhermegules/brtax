package com.api.brtax.domain.user.dto;

import com.api.brtax.domain.user.UserType;
import java.util.UUID;

public record UserDetails(UUID id, String name, String cpf, UserType type) {
}
