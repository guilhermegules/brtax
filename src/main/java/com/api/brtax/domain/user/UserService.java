package com.api.brtax.domain.user;

import com.api.brtax.domain.user.dto.SaveUser;
import com.api.brtax.domain.user.dto.UpdateUser;
import com.api.brtax.domain.user.dto.UserDetails;

import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDetails getUserById(UUID id) {
    return userRepository
        .findById(id)
        .map(user -> new UserDetails(user.getId(), user.getName(), user.getCpf(), user.getType()))
        .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
  }

  public UUID save(SaveUser saveUser) {
    var isValid = saveUserValidityChecker(saveUser);

    if (!isValid) {
      throw new BusinessException("Passed user is invalid!");
    }

    var user = new User(saveUser.name(), saveUser.cpf(), saveUser.password(), saveUser.type());
    var savedUser = userRepository.save(user);
    return savedUser.getId();
  }

  public User update(UUID userId, UpdateUser updateUser) {
    var user =
        userRepository
            .findById(userId)
            .map(u -> updateUserChecker(u, updateUser))
            .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

    return userRepository.save(user);
  }

  private User updateUserChecker(User user, UpdateUser updateUser) {
    if (!Objects.equals(user.getName(), updateUser.name()) && Objects.nonNull(updateUser.name())) {
      user.setName(updateUser.name());
    }

    if (!Objects.equals(user.getPassword(), updateUser.password())
        && Objects.nonNull(updateUser.password())) {
      user.setPassword(updateUser.password());
    }

    if (!Objects.equals(user.getType(), updateUser.type()) && Objects.nonNull(updateUser.type())) {
      user.setType(updateUser.type());
    }

    if (!Objects.equals(user.getCpf(), updateUser.cpf()) && Objects.nonNull(updateUser.cpf())) {
      user.setCpf(updateUser.cpf());
    }

    return user;
  }

  private boolean saveUserValidityChecker(SaveUser saveUser) {
    return emptyValidator(saveUser.password())
        && emptyValidator(saveUser.name())
        && cpfValidator(saveUser.cpf())
        && userTypeValidator(saveUser.type());
  }

  private boolean userTypeValidator(UserType type) {
    return Arrays.asList(UserType.values()).contains(type);
  }

  private boolean emptyValidator(String value) {
    if (value == null) return false;

    return !value.isEmpty();
  }

  private boolean cpfValidator(String cpf) {
    if (cpf == null) return false;

    if (cpf.length() < 11) return false;

    return !cpf.contains(".") || !cpf.contains("-");
  }
}
