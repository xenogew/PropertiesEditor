package io.github.xenogew.propedit.util;

import java.io.IOException;

public class AlreadyFileLockException extends IOException {

  private static final long serialVersionUID = 9082096435969517133L;

  public AlreadyFileLockException() {

  }

  public AlreadyFileLockException(String s) {

    super(s);
  }
}
