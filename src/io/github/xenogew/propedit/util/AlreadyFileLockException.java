package io.github.xenogew.propedit.util;

import java.io.IOException;

public class AlreadyFileLockException extends IOException {

  public AlreadyFileLockException() {

  }

  public AlreadyFileLockException(String s) {

    super(s);
  }
}
