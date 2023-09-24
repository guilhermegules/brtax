package com.api.brtax;

import com.api.brtax.domain.user.User;
import java.util.UUID;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.relational.core.mapping.event.BeforeConvertCallback;

@SpringBootApplication
public class BrtaxApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrtaxApplication.class, args);
	}

	@Bean
	BeforeConvertCallback<User> beforeSaveCallback() {
		return (user) -> {
			if (user.getId() == null) {
				user.setId(UUID.randomUUID().toString());
			}
			return user;
		};
	}
}
