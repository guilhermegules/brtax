package com.api.brtax.controller;

import com.api.brtax.domain.user.dto.UpdateUser;
import com.api.brtax.domain.user.dto.UserDetails;
import com.api.brtax.domain.user.UserService;
import com.api.brtax.domain.user.dto.SaveUser;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDetails> getUserById(@PathVariable UUID userId) {
    var user = userService.getUserById(userId);
    return ResponseEntity.ok(user);
  }

  @PostMapping
  @Transactional
  public ResponseEntity<SaveUser> save(
      @RequestBody SaveUser body, UriComponentsBuilder uriBuilder) {
    var userId = userService.save(body);
    var uri = uriBuilder.path("/user/{userId}").buildAndExpand(userId).toUri();
    return ResponseEntity.created(uri).body(body);
  }

  @PutMapping("/{userId}")
  @Transactional
  public ResponseEntity<UserDetails> update(
      @PathVariable UUID userId, @RequestBody UpdateUser body) {
    var user = userService.update(userId, body);

    return ResponseEntity.ok(
        new UserDetails(user.getId(), user.getName(), user.getCpf(), user.getType()));
  }

  @DeleteMapping("/{userId}")
  @Transactional
  public ResponseEntity<Object> delete(@PathVariable UUID userId) {
    userService.delete(userId);
    return ResponseEntity.noContent().build();
  }
}
