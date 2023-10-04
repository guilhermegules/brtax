package com.api.brtax.domain.user;

import com.api.brtax.domain.user.dto.SaveUser;
import com.api.brtax.domain.user.dto.UpdateUser;
import com.api.brtax.domain.user.dto.UserDetails;

import com.api.brtax.exception.BusinessException;
import com.api.brtax.exception.NotFoundException;
import com.api.brtax.util.Validator;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public UserDetails getUserById(UUID id) {
    return userRepository
        .findById(id)
        .map(user -> new UserDetails(user.getId(), user.getName(), user.getCpf(), user.getType()))
        .orElseThrow(() -> new NotFoundException("User not found with ID: " + id));
  }

  @Transactional
  public UUID save(SaveUser saveUser) {
    if (!isSavePayloadValid(saveUser)) {
      throw new BusinessException("Passed user is invalid!");
    }

    var user = new User(saveUser.name(), saveUser.cpf(), saveUser.password(), saveUser.type());
    var savedUser = userRepository.save(user);
    return savedUser.getId();
  }

  @Transactional
  public User update(UUID userId, UpdateUser updateUser) {
    if(!isUpdatePayloadValid(updateUser)) {
      throw new BusinessException("User data for update is invalid! " + updateUser);
    }

    var user =
        userRepository
            .findById(userId)
            .map(u -> updateUserData(u, updateUser))
            .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));

    return userRepository.save(user);
  }

  @Transactional
  public void delete(UUID userId) {
    userRepository.deleteById(userId);
  }

  private User updateUserData(User user, UpdateUser updateUser) {
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

  private boolean isSavePayloadValid(SaveUser saveUser) {
    return Validator.hasValue(saveUser.password())
        && Validator.hasValue(saveUser.name())
        && Validator.isValidCpf(saveUser.cpf())
        && hasValueOnList(saveUser.type());
  }

  private boolean isUpdatePayloadValid(UpdateUser updateUser) {
    return Objects.nonNull(updateUser.cpf()) && Validator.isValidCpf(updateUser.cpf())
        || Objects.nonNull(updateUser.password())
        || Objects.nonNull(updateUser.type()) && hasValueOnList(updateUser.type())
        || Objects.nonNull(updateUser.name());
  }

  private boolean hasValueOnList(UserType type) {
    return Arrays.asList(UserType.values()).contains(type);
  }
}
