/*****************************************************
 *
 * @author Sou Miyazaki
 *
 ****************************************************/
package io.github.xenogew.propedit.util;

import io.github.xenogew.propedit.io.LockableFileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 
 * @author Sou Miyazaki
 * 
 */
public class FileOpener extends File {

  /**
   */
  private StringBuilder txt = null;

  /**
   * 
   * @param arg0
   * @since 1.0.0
   */
  public FileOpener(String filepath) {

    super(filepath);
  }

  /**
   * 
   * @param arg0
   * @since 1.0.0
   */
  public FileOpener(File file) {

    super(file.getPath());
  }

  /**
   * 
   * @param Code
   * @since 1.0.0
   */
  public void read(String code) throws IOException {

    if (this.isFile()) {
      // In the case of a file (nothing is carried out when it is not a
      // file)
      try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(this));
          ByteArrayOutputStream bin = new ByteArrayOutputStream()) {
        byte[] buffer = new byte[1024];
        int getLength = 0;
        // Reading of a file
        while ((getLength = bis.read(buffer)) != -1) {
          bin.write(buffer, 0, getLength);
        }
        txt =
            new StringBuilder(StringUtil.removeCarriageReturn(new String(bin.toByteArray(), code)));
      }
    }
  }

  /**
   * 
   * @param code
   * @since 1.0.0
   */
  public void write(String code) throws AlreadyFileLockException, IOException {

    try (LockableFileOutputStream out = new LockableFileOutputStream(new FileOutputStream(this))) {
      if (!out.tryLock()) {
        throw new AlreadyFileLockException(Messages.getString("FileOpener.0")); //$NON-NLS-1$
      }
      try {
        out.write(this.getText().getBytes(code));
      } finally {
        out.unlock();
      }
      out.flush();
    }
  }

  /**
   * 
   * @since 1.0.0
   */
  public String getText() {

    if (txt == null) {
      return null;
    } else {
      return txt.toString();
    }
  }

  /**
   * 
   * @param buffer
   * @since 1.0.0
   */
  public void setText(String buffer) {

    txt = new StringBuilder();
    if (buffer != null) {
      txt.append(buffer);
    }
  }
}
