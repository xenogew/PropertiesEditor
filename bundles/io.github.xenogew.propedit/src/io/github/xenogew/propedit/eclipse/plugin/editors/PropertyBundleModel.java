package io.github.xenogew.propedit.eclipse.plugin.editors;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.util.PropertiesFileUtil;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Model representing a bundle of related properties files (different locales).
 */
public class PropertyBundleModel {

  public static final String DEFAULT_LOCALE = "default";

  private final IFile primaryFile;
  private final String baseName;
  private final Map<String, IFile> localeFiles = new TreeMap<>();
  private final Map<String, Properties> localeProperties = new HashMap<>();
  private final List<String> allKeys = new ArrayList<>();

  public PropertyBundleModel(IFile primaryFile) {
    this.primaryFile = primaryFile;
    this.baseName = PropertiesFileUtil.extractBaseName(primaryFile.getName());
    discoverFiles();
    loadProperties();
  }

  private void discoverFiles() {
    IResource parent = primaryFile.getParent();
    if (!(parent instanceof IContainer))
      return;

    IContainer container = (IContainer) parent;
    try {
      container.refreshLocal(IResource.DEPTH_ONE, null);
      for (IResource resource : container.members()) {
        if (resource instanceof IFile && resource.getName().endsWith(".properties")) {
          String name = resource.getName();
          String nameNoExt = name.substring(0, name.lastIndexOf('.'));

          if (nameNoExt.equals(baseName) || nameNoExt.startsWith(baseName + "_")) {
            String locale = extractLocale(name);
            localeFiles.put(locale, (IFile) resource);
          }
        }
      }
    } catch (CoreException e) {
      PropEditorXPlugin.getDefault().error("Error discovering bundle files", e);
    }
  }

  private String extractLocale(String fileName) {
    String nameNoExt = fileName.substring(0, fileName.lastIndexOf('.'));
    if (nameNoExt.equals(baseName)) {
      return DEFAULT_LOCALE;
    }
    if (nameNoExt.startsWith(baseName + "_")) {
      return nameNoExt.substring(baseName.length() + 1);
    }
    return nameNoExt;
  }

  public String getDisplayName(String locale) {
    if (DEFAULT_LOCALE.equals(locale)) {
      return "English";
    }
    // Use Java's Locale to get a human-readable name in English
    Locale l = Locale.forLanguageTag(locale.replace('_', '-'));
    String name = l.getDisplayLanguage(Locale.ENGLISH);
    if (name != null && !name.isEmpty()) {
      // Capitalize first letter
      return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
    return locale.toUpperCase();
  }

  public void refresh() {
    localeFiles.clear();
    localeProperties.clear();
    discoverFiles();
    loadProperties();
  }

  private void loadProperties() {
    Set<String> keys = new HashSet<>();
    for (Map.Entry<String, IFile> entry : localeFiles.entrySet()) {
      Properties prop = new Properties();
      try (InputStream is = entry.getValue().getContents();
          InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
        prop.load(reader);
        localeProperties.put(entry.getKey(), prop);
        for (Object key : prop.keySet()) {
          keys.add((String) key);
        }
      } catch (Exception e) {
        PropEditorXPlugin.getDefault()
            .error("Error loading properties for locale: " + entry.getKey(), e);
      }
    }
    allKeys.clear();
    allKeys.addAll(keys);
    Collections.sort(allKeys);
  }

  public String getBaseName() {
    return baseName;
  }

  public List<String> getAllKeys() {
    return allKeys;
  }

  public Map<String, IFile> getLocaleFiles() {
    return localeFiles;
  }

  public String getValue(String key, String locale) {
    Properties prop = localeProperties.get(locale);
    return (prop != null) ? prop.getProperty(key) : null;
  }

  public void setValue(String key, String locale, String value) {
    Properties prop = localeProperties.get(locale);
    if (prop == null) {
      prop = new Properties();
      localeProperties.put(locale, prop);
    }
    prop.setProperty(key, value);
    if (!allKeys.contains(key)) {
      allKeys.add(key);
      Collections.sort(allKeys);
    }
  }

  public void saveAll(IProgressMonitor monitor) throws Exception {
    for (Map.Entry<String, IFile> entry : localeFiles.entrySet()) {
      IFile file = entry.getValue();
      Properties prop = localeProperties.get(entry.getKey());
      if (prop != null) {
        StringBuilder sb = new StringBuilder();
        List<String> keys = new ArrayList<>(prop.stringPropertyNames());
        Collections.sort(keys);
        for (String k : keys) {
          sb.append(k).append("=").append(prop.getProperty(k)).append("\n");
        }
        byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
        try (InputStream source = new ByteArrayInputStream(bytes)) {
          if (file.exists()) {
            file.setContents(source, IResource.FORCE, monitor);
          }
        }
      }
    }
  }
}
