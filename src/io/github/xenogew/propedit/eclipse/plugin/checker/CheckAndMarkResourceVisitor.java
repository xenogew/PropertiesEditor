package io.github.xenogew.propedit.eclipse.plugin.checker;

import io.github.xenogew.propedit.eclipse.plugin.PropertiesEditorPlugin;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.PlatformUI;

/**
 * @author Administrator
 * 
 *         この生成されたコメントの挿入されるテンプレートを変更するため ウィンドウ > 設定 > Java > コード生成 > コードとコメント
 */
public class CheckAndMarkResourceVisitor implements IResourceVisitor {

  private CheckAndMarkDuplicateKey camd;

  public CheckAndMarkResourceVisitor(CheckAndMarkDuplicateKey camd) {

    this.camd = camd;
  }

  @Override
  public boolean visit(IResource resource) throws CoreException {

    if (resource.getFileExtension() != null && resource.getFileExtension().equals("properties")) { //$NON-NLS-1$
      Charset charset;
      try {
        charset = Charset.forName(((IFile) resource).getCharset());
      } catch (Exception e) {
        charset = StandardCharsets.UTF_8;
      }
      try (BufferedInputStream in = new BufferedInputStream(((IFile) resource).getContents());
          ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        byte[] tmp = new byte[10240];
        int readCnt = 0;
        while ((readCnt = in.read(tmp)) != -1) {
          out.write(tmp, 0, readCnt);
        }
        camd.checkAndMarkDuplicateKeyInString(out.toString(charset), resource);
      } catch (IOException e) {
        IStatus status =
            new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
        ILog log = PropertiesEditorPlugin.getDefault().getLog();
        log.log(status);
        throw new CoreException(status);
      }
    }

    return true;
  }

}
