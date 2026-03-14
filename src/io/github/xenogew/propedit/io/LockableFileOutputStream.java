package io.github.xenogew.propedit.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LockableFileOutputStream extends OutputStream {

  private static final Logger LOG = Logger.getLogger(LockableFileOutputStream.class.getName());

  private FileOutputStream fOut;

  private FileChannel fc;

  private FileLock fl;

  private final int TRY_LOCK_FLG = 0;

  private final int LOCK_FLG = 1;

  public LockableFileOutputStream(FileOutputStream out) {

    this.fOut = out;
    fc = out.getChannel();
  }

  public boolean tryLock() {

    return lock(TRY_LOCK_FLG);
  }

  private boolean lock(int flg) {

    try {
      if (fc != null) {
        if (flg == TRY_LOCK_FLG) {
          fl = fc.tryLock();
        } else if (flg == LOCK_FLG) {
          fl = fc.lock();
        }
        if (fl == null) {
          return false;
        }
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      LOG.log(Level.SEVERE, e.getMessage(), e);
      try {
        fl.release();
      } catch (IOException ex) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);
      }
      return false;
    }
  }

  public boolean lock() {

    return lock(LOCK_FLG);
  }

  public boolean unlock() throws IOException {

    try {
      if (fl != null) {
        fl.release();
        return true;
      } else {
        return false;
      }
    } catch (IOException e) {
      throw e;
    } finally {
      try {
        if (fl != null) {
          fl.release();
        }
      } catch (IOException ex) {
        LOG.log(Level.SEVERE, ex.getMessage(), ex);
      }
    }
  }

  protected void finalize() throws IOException {

    unlock();
  }

  public void close() throws java.io.IOException {

    fOut.close();
  }

  public void write(byte[] parm1) throws java.io.IOException {

    fOut.write(parm1);
  }

  public void write(int b) throws java.io.IOException {

    fOut.write(b);
  }

  public void write(byte[] parm1, int parm2, int parm3) throws java.io.IOException {

    fOut.write(parm1, parm2, parm3);
  }

  public void flush() throws java.io.IOException {

    fOut.flush();
  }

  public FileChannel getChannel() {

    return fc;
  }
}
