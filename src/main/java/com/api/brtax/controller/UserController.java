package com.api.brtax.controller;

import com.api.brtax.domain.user.dto.UserDetails;
import com.api.brtax.domain.user.UserService;
import com.api.brtax.domain.user.dto.SaveUser;
import com.api.brtax.domain.user.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/{userId}")
  public ResponseEntity<UserDetails> getUserById(@PathVariable String userId) {
    try {
      var user = userService.getUserById(userId);
      return ResponseEntity.ok(user);
    } catch (UserNotFoundException userNotFoundException) {
      return ResponseEntity.notFound().build();
    }
  }

  @PostMapping
  @Transactional
  public ResponseEntity<SaveUser> save(@RequestBody SaveUser body) {
    var user = userService.save(body);
    return ResponseEntity.ok(user);
  }
}
