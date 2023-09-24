package com.api.brtax.domain.user;

import java.util.UUID;

public record UserDetails(UUID id, String name, String cpf, UserType type) {
}
