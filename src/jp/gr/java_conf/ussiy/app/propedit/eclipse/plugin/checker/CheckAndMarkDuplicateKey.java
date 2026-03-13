package jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.checker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.PropertiesEditorPlugin;
import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.preference.PropertiesEditorDuplicationCheckerPreference;
import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.resources.Messages;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

public class CheckAndMarkDuplicateKey {

  public void checkAndMarkDuplicateKeyInResource(IResource resource) throws CoreException {

    try {
      CheckAndMarkResourceVisitor visitor = new CheckAndMarkResourceVisitor(this);
      resource.accept(visitor);
    } catch (CoreException e) {
      IStatus coreStatus =
          new Status(IStatus.ERROR, PropertiesEditorPlugin.PLUGIN_ID, e.getMessage(), e);
      PropertiesEditorPlugin.getDefault().getLog().log(coreStatus);
      throw e;
    } catch (Exception e) {
      IStatus status =
          new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
      ILog log = PropertiesEditorPlugin.getDefault().getLog();
      log.log(status);
      throw new CoreException(status);
    }
  }

  public void checkAndMarkDuplicateKeyInString(String text, IFileEditorInput editorInput)
      throws CoreException {

    IResource resource = editorInput.getFile();
    checkAndMarkDuplicateKeyInString(text, resource);
  }

  public void checkAndMarkDuplicateKeyInString(String text, IResource resource)
      throws CoreException {

    // delete marker
    IMarker[] markers = resource.findMarkers("jp.gr.java_conf.ussiy.app.propedit.duplicationmarker", //$NON-NLS-1$
        false, IResource.DEPTH_ZERO);
    for (IMarker marker : markers) {
      marker.delete();
    }

    if (!PropertiesEditorPlugin.getDefault().getPreferenceStore()
        .getBoolean(PropertiesEditorDuplicationCheckerPreference.P_CHECK_KEY)) {
      return;
    }

    Map<String, List<Integer>> keyMap;
    try {
      keyMap = extractKeys(text);
    } catch (IOException e) {
      IStatus status =
          new Status(IStatus.ERROR, PlatformUI.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
      ILog log = PropertiesEditorPlugin.getDefault().getLog();
      log.log(status);
      throw new CoreException(status);
    }

    String warn = Messages.getString("eclipse_editor_key_duplicate"); //$NON-NLS-1$
    for (var entry : keyMap.entrySet()) {
      var lines = entry.getValue();
      if (lines.size() > 1) {
        for (int lineNum : lines) {
          IMarker marker =
              resource.createMarker("jp.gr.java_conf.ussiy.app.propedit.duplicationmarker"); //$NON-NLS-1$
          marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_WARNING);
          marker.setAttribute(IMarker.TRANSIENT, true);
          marker.setAttribute(IMarker.MESSAGE, replace(warn, entry.getKey(), "$key$")); //$NON-NLS-1$
          marker.setAttribute(IMarker.LINE_NUMBER, lineNum);
        }
      }
    }
  }

  public static Map<String, List<Integer>> extractKeys(String text) throws IOException {

    Map<String, List<Integer>> map = new HashMap<>();
    int cntLine = 0;
    boolean multipleValueFlg = false;

    try (BufferedReader reader = new BufferedReader(new StringReader(text))) {
      String key = null;
      String line = null;
      boolean lineEscape = false;
      while ((line = reader.readLine()) != null) {
        if (!lineEscape) {
          key = ""; //$NON-NLS-1$
        }
        cntLine++;
        if (line.trim().equals("") || line.trim().startsWith("#") || line.trim().startsWith("!")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          multipleValueFlg = false;
          continue;
        }
        if (multipleValueFlg) {
          if (line.endsWith("\\")) { //$NON-NLS-1$
            multipleValueFlg = true;
          } else {
            multipleValueFlg = false;
          }
          continue;
        }
        line = line.trim();
        boolean escapeFlg = false;
        boolean nonSeparate = true;
        for (int i = 0; i < line.length(); i++) {
          char achar = line.charAt(i);
          if (achar == '\\') {
            if (escapeFlg) {
              String tmp = line;
              line = ""; //$NON-NLS-1$
              line = tmp.substring(0, i);
              line += tmp.substring(i + 1, tmp.length());
              escapeFlg = false;
              i--;
            } else {
              escapeFlg = true;
            }
          } else if (achar == '=' || achar == '\t' || achar == ':' || achar == ' ') {
            if (escapeFlg) {
              String tmp = line;
              line = ""; //$NON-NLS-1$
              line = tmp.substring(0, i - 1);
              line += tmp.substring(i, tmp.length());
              escapeFlg = false;
              i--;
              continue;
            } else {
              nonSeparate = false;
              escapeFlg = false;
              key += line.substring(0, i);
              break;
            }
          } else {
            if (escapeFlg) {
              String tmp = line;
              line = ""; //$NON-NLS-1$
              line = tmp.substring(0, i - 1);
              line += tmp.substring(i, tmp.length());
              i--;
            }
            escapeFlg = false;
          }
        }
        if (nonSeparate) {
          if (line.endsWith("\\")) { //$NON-NLS-1$
            lineEscape = true;
            key += line;
            continue;
          } else {
            lineEscape = false;
            key += line;
          }
        } else {
          lineEscape = false;
        }
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(cntLine);
        if (line.endsWith("\\")) { //$NON-NLS-1$
          multipleValueFlg = true;
        } else {
          multipleValueFlg = false;
        }
      }
    }
    return map;
  }

  public static String replace(String str, String toString, String fromString) {

    return str.replace(fromString, toString);
  }
}
