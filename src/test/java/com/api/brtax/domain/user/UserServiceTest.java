package com.api.brtax.domain.user;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import com.api.brtax.domain.user.dto.SaveUser;
import com.api.brtax.domain.user.dto.UserDetails;
import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class UserServiceTest {

  @InjectMocks
  UserService userService;

  @Mock
  UserRepository userRepository;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    userService = new UserService(userRepository);
  }

  @Test
  public void shouldGetUserById() {
    var randomId = UUID.randomUUID();
    var user = new User("John doe", "123", "123", UserType.MANAGER);
    user.setId(randomId);
    Mockito.when(userRepository.findById(any())).thenReturn(Optional.of(user));
    assertInstanceOf(UserDetails.class, userService.getUserById(randomId));
  }

  @Test
  public void shouldThrowNotFoundExceptionWhenNotHaveAnyUsers() {
    var randomUUID = UUID.randomUUID();
    Mockito.when(userRepository.findById(randomUUID)).thenReturn(Optional.empty());

    NotFoundException notFoundException = assertThrows(NotFoundException.class, () -> {
      userService.getUserById(randomUUID);
    });

    assertEquals("User not found with ID: " + randomUUID, notFoundException.getMessage());
  }

  @Test
  public void shouldSaveUser() {
    var saveUser = new SaveUser("Test user", "12345678910", "2", UserType.MANAGER);
    var user = new User(saveUser.name(), saveUser.cpf(), saveUser.password(), saveUser.type());
    var randomId = UUID.randomUUID();
    user.setId(randomId);

    Mockito.when(userRepository.save(any())).thenReturn(user);

    var userId = userService.save(saveUser);
    Assertions.assertEquals(userId, randomId);
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

  @Test
  public void shouldDeleteUser() {
    var userId = UUID.randomUUID();
    userService.delete(userId);
    verify(userRepository, Mockito.times(1)).deleteById(userId);
  }
}
