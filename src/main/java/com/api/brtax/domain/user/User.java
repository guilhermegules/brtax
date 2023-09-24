package com.api.brtax.domain.user;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table(name = "brtax_user")
public class User {
    @Id
    private String id;
    private final String name;
    private final String cpf;
    private final String password;
    private final UserType type;

    User(String name, String cpf, String password, UserType type) {
        this.name = name;
        this.cpf = cpf;
        this.password = password;
        this.type = type;
    }

    public void setId(String id) {
        this.id = id;
    }
}
