package com.api.brtax.util;

import com.api.brtax.domain.user.UserType;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Validator {
  public static boolean hasValue(String value) {
    return Objects.nonNull(value) && !value.isEmpty();
  }

  public static boolean isValidCpf(String cpf) {
    if (cpf == null) return false;

    if (cpf.length() < 11) return false;

    return !cpf.contains(".") || !cpf.contains("-");
  }
}
