/**
 * 
 */
package io.github.xenogew.propedit.eclipse.plugin.util;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 *
 */
public class ProjectProperties {

  private static ProjectProperties instance = null;

  private Map<IProject, Map<IFile, Properties>> propertyMap = new HashMap<>();

  private ProjectProperties() {}

  public static ProjectProperties getInstance() {
    if (ProjectProperties.instance == null) {
      ProjectProperties.instance = new ProjectProperties();
    }
    return ProjectProperties.instance;
  }

  public void loadAllProperty(IWorkspace workspace) {
    IProject[] projects = workspace.getRoot().getProjects();
    for (IProject project : projects) {
      if (project.isOpen()) {
        loadProjectProperties(project);
      }
    }
  }

  public void deleteProjectProperties(IProject project) {
    // log("properties removing for project '" + project.getName() + "'"); //$NON-NLS-1$
    // //$NON-NLS-2$
    this.propertyMap.remove(project);
  }

  /**
   * @param project
   */
  public void loadProjectProperties(IProject project) {
    // log("properties loading for project '" + project.getName() + "'"); //$NON-NLS-1$
    // //$NON-NLS-2$
    if (!project.isOpen()) {
      this.propertyMap.remove(project);
      return;
    }
    IJavaProject jProject = JavaCore.create(project);
    IPath outputPath = null;
    try {
      outputPath = jProject.getOutputLocation();
    } catch (JavaModelException e) {
    }
    IFile[] pFiles = PropertiesFileUtil.findFileExt(project, outputPath, "properties"); //$NON-NLS-1$
    Map<IFile, Properties> list = new HashMap<>();
    for (IFile pFile : pFiles) {
      // log("loading file '" + pFile.getName() + "'"); //$NON-NLS-1$ //$NON-NLS-2$
      Properties prop = new Properties();
      Charset charset;
      try {
        charset = Charset.forName(pFile.getCharset());
      } catch (Exception e) {
        charset = StandardCharsets.UTF_8;
      }
      try (InputStream is = pFile.getContents();
          InputStreamReader reader = new InputStreamReader(is, charset)) {
        prop.load(reader);
      } catch (IOException e) {
        IStatus status =
            new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
        ILog log = PropEditorXPlugin.getDefault().getLog();
        log.log(status);
      } catch (CoreException e) {
        IStatus status =
            new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
        ILog log = PropEditorXPlugin.getDefault().getLog();
        log.log(status);
      }
      list.put(pFile, prop);
    }
    propertyMap.put(project, list);
  }

  public Properties getProperty(IProject project) {
    Properties prop = new Properties();
    Map<IFile, Properties> properties = propertyMap.get(project);
    for (Properties p : properties.values()) {
      prop.putAll(p);
    }
    return prop;
  }

  public Map<IFile, Properties> getProperty(IProject project, String key) {
    Map<IFile, Properties> list = new HashMap<>();
    Map<IFile, Properties> properties = propertyMap.get(project);
    for (Map.Entry<IFile, Properties> entry : properties.entrySet()) {
      if (entry.getValue().containsKey(key)) {
        list.put(entry.getKey(), entry.getValue());
      }
    }
    return list;
  }

}
