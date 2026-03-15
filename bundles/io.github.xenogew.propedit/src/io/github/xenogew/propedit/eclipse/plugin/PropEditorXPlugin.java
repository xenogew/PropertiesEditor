package io.github.xenogew.propedit.eclipse.plugin;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class PropEditorXPlugin extends AbstractUIPlugin {

  public static String PLUGIN_ID = "io.github.xenogew.propedit"; //$NON-NLS-1$

  // The shared instance.
  private static PropEditorXPlugin plugin;

  // Resource bundle.
  private ResourceBundle resourceBundle;

  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
    try {
      resourceBundle = ResourceBundle.getBundle(
          "io.github.xenogew.propedit.eclipse.plugin.editors.PropEditorXPluginResources"); //$NON-NLS-1$
    } catch (MissingResourceException x) {
      resourceBundle = null;
    }
  }

  @Override
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance.
   */
  public static PropEditorXPlugin getDefault() {

    return plugin;
  }

  /**
   * Returns the plugin logger (available even before the activator starts).
   */
  public static ILog log() {

    return Platform.getLog(PropEditorXPlugin.class);
  }

  /**
   * Logs an error message.
   */
  public void error(String message, Throwable exception) {
    getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, message, exception));
  }

  /**
   * Returns the workspace instance.
   */
  public static IWorkspace getWorkspace() {

    return ResourcesPlugin.getWorkspace();
  }

  /**
   * Returns the string from the plugin's resource bundle, or 'key' if not found.
   */
  public static String getResourceString(String key) {

    ResourceBundle bundle = PropEditorXPlugin.getDefault().getResourceBundle();
    try {
      return bundle.getString(key);
    } catch (MissingResourceException e) {
      return key;
    }
  }

  /**
   * Returns the plugin's resource bundle,
   */
  public ResourceBundle getResourceBundle() {

    return resourceBundle;
  }
}
