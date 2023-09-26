package com.api.brtax.domain.user;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.api.brtax.domain.user.dto.SaveUser;
import com.api.brtax.domain.user.dto.UserDetails;
import com.api.brtax.exception.BusinessException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserRepositoryMock implements UserRepository {

  public List<User> users = new ArrayList<>();

  public UserRepositoryMock() {
    addTwoUsers();
  }

  public void addTwoUsers() {
    var user1 = new User("Test 1", "123456", "222", UserType.ACCOUNTANT);
    user1.setId(UUID.randomUUID());
    users.add(user1);
    var user2 = new User("Test 2", "123456", "222", UserType.ACCOUNTANT);
    user2.setId(UUID.randomUUID());
    users.add(user2);
  }

  public void clearUserList() {
    users.clear();
  }

  @Override
  public <S extends User> S save(S user) {
    users.add(user);
    return user;
  }

  @Override
  public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<User> findById(UUID id) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(UUID uuid) {
    return false;
  }

  @Override
  public Iterable<User> findAll() {
    return null;
  }

  @Override
  public Iterable<User> findAllById(Iterable<UUID> strings) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(UUID uuid) {

  }

  @Override
  public void delete(User entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends UUID> strings) {

  }

  @Override
  public void deleteAll(Iterable<? extends User> entities) {

  }

  @Override
  public void deleteAll() {

  }
}

public class UserServiceTest {

  UserService userService;
  UserRepositoryMock userRepositoryMock = new UserRepositoryMock();

  @BeforeEach
  public void init() {
    userService = new UserService(userRepositoryMock);
  }

  @Test
  public void shouldGetUserById() {
    assertInstanceOf(UserDetails.class, userService.getUserById(UUID.randomUUID()));
  }

  @Test
  public void shouldThrowBusinessExceptionWhenNotHaveAnyUsers() {
    userRepositoryMock.clearUserList();

    Throwable error = assertThrows(BusinessException.class, () -> {
      userService.getUserById(UUID.randomUUID());
    });

    assertEquals("User not found with ID: 1", error.getMessage());
  }

  @Test
  public void shouldSaveUser() {
    var saveUser = new SaveUser("Test user", "12345678910", "2", UserType.MANAGER);
    var user = userService.save(saveUser);
    assertEquals(user, saveUser);
  }

  @Test
  public void shouldThrowBusinessExceptionWhenUserNotHavePassword() {
    Throwable error = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser("Test user", "123", "", UserType.MANAGER);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error.getMessage());

    Throwable error2 = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser("Test user", "123", null, UserType.MANAGER);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error2.getMessage());
  }

  @Test
  public void shouldThrowBusinessExceptionWhenUserNotHaveName() {
    Throwable error = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser("", "123", "1", UserType.MANAGER);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error.getMessage());

    Throwable error2 = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser(null, "123", "1", UserType.MANAGER);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error2.getMessage());
  }

  @Test
  public void shouldThrowBusinessExceptionWhenUserCpfIsNull() {
    Throwable error = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser("test", null, "1", UserType.MANAGER);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error.getMessage());
  }

  @Test
  public void shouldThrowBusinessExceptionWhenUserCpfHasFormat() {
    Throwable error = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser("test", "123.123.123-12", "222", UserType.MANAGER);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error.getMessage());
  }

  @Test
  public void shouldThrowBusinessExceptionWhenUserCpfLengthIsLowerThan12() {
    Throwable error = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser("test", "123", "222", UserType.MANAGER);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error.getMessage());
  }

  @Test
  public void shouldThrowBusinessExceptionWhenUserTypeIsNotOnValidEnums() {
    Throwable error = assertThrows(BusinessException.class, () -> {
      var saveUser = new SaveUser("test", "12345678910", "222", null);
      userService.save(saveUser);
    });

    assertEquals("Passed user is invalid!", error.getMessage());
  }
}
