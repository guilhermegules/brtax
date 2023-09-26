package com.api.brtax.domain.user;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "brtax_user")
public class User {
    @Id
    private UUID id;
    private String name;
    private String cpf;
    private String password;
    private UserType type;

    User(String name, String cpf, String password, UserType type) {
        this.name = name;
        this.cpf = cpf;
        this.password = password;
        this.type = type;
    }
}
