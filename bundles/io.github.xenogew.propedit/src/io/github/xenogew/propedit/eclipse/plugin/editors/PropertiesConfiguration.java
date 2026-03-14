package io.github.xenogew.propedit.eclipse.plugin.editors;

import io.github.xenogew.propedit.eclipse.plugin.PropEditorXPlugin;
import io.github.xenogew.propedit.eclipse.plugin.preference.PropEditorXPreference;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class PropertiesConfiguration extends SourceViewerConfiguration {

  private static final String EXTENSION_POINT = "io.github.xenogew.propedit.hyperlinkdetectors"; //$NON-NLS-1$

  private PropertiesDoubleClickStrategy doubleClickStrategy;

  private ColorManager colorManager;

  private PropEditorX editor;

  public PropertiesConfiguration(ColorManager colorManager, PropEditorX editor) {

    this.colorManager = colorManager;
    this.editor = editor;
  }

  @Override
  public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {

    return new String[] {IDocument.DEFAULT_CONTENT_TYPE,
        PropertiesPartitionScanner.PROPERTIES_COMMENT,
        PropertiesPartitionScanner.PROPERTIES_SEPARATOR,
        PropertiesPartitionScanner.PROPERTIES_VALUE};
  }

  @Override
  public ITextDoubleClickStrategy getDoubleClickStrategy(ISourceViewer sourceViewer,
      String contentType) {

    if (doubleClickStrategy == null) {
      doubleClickStrategy = new PropertiesDoubleClickStrategy();
    }
    return doubleClickStrategy;
  }

  public IPreferenceStore getPreferenceStore() {

    return PropEditorXPlugin.getDefault().getPreferenceStore();
  }

  @Override
  public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {

    IPreferenceStore pStore = getPreferenceStore();

    PresentationReconciler reconciler = new PresentationReconciler();

    RGB rgb = PreferenceConverter.getColor(pStore, PropEditorXPreference.P_COMMENT_COLOR);
    Color color = colorManager.getColor(rgb);
    TextAttribute attr = new TextAttribute(color);
    NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(attr);
    reconciler.setDamager(ndr, PropertiesPartitionScanner.PROPERTIES_COMMENT);
    reconciler.setRepairer(ndr, PropertiesPartitionScanner.PROPERTIES_COMMENT);

    rgb = PreferenceConverter.getColor(pStore, PropEditorXPreference.P_SEPARATOR_COLOR);
    color = colorManager.getColor(rgb);
    attr = new TextAttribute(color);
    ndr = new NonRuleBasedDamagerRepairer(attr);
    reconciler.setDamager(ndr, PropertiesPartitionScanner.PROPERTIES_SEPARATOR);
    reconciler.setRepairer(ndr, PropertiesPartitionScanner.PROPERTIES_SEPARATOR);

    rgb = PreferenceConverter.getColor(pStore, PropEditorXPreference.P_VALUE_COLOR);
    color = colorManager.getColor(rgb);
    attr = new TextAttribute(color);
    ndr = new NonRuleBasedDamagerRepairer(attr);
    reconciler.setDamager(ndr, PropertiesPartitionScanner.PROPERTIES_VALUE);
    reconciler.setRepairer(ndr, PropertiesPartitionScanner.PROPERTIES_VALUE);

    PropertiesScanner scanner = new PropertiesScanner(colorManager, pStore);
    DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
    reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
    reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

    return reconciler;
  }

  /**
   * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getReconciler(org.eclipse.jface.text.source.ISourceViewer)
   */
  @Override
  public IReconciler getReconciler(ISourceViewer sourceViewer) {
    PropertiesReconcilingStrategy strategy = new PropertiesReconcilingStrategy();
    strategy.setEditor(editor);

    MonoReconciler reconciler = new MonoReconciler(strategy, false);

    return reconciler;
  }

  /**
   * @see org.eclipse.jface.text.source.SourceViewerConfiguration#getHyperlinkDetectors(org.eclipse.jface.text.source.ISourceViewer)
   */
  @Override
  public IHyperlinkDetector[] getHyperlinkDetectors(ISourceViewer viewer) {
    IHyperlinkDetector[] detectors = super.getHyperlinkDetectors(viewer);
    List<IHyperlinkDetector> list = new ArrayList<>();
    for (IHyperlinkDetector detector : detectors) {
      if (detector != null)
        list.add(detector);
    }
    list.addAll(computePropertiesHyperlinkDetectors());
    return list.toArray(new IHyperlinkDetector[0]);
  }

  protected List<IHyperlinkDetector> computePropertiesHyperlinkDetectors() {
    IExtensionRegistry registry = Platform.getExtensionRegistry();
    IExtensionPoint extensionPoint = registry.getExtensionPoint(EXTENSION_POINT);
    IExtension[] extensions = extensionPoint.getExtensions();
    ArrayList<IHyperlinkDetector> results = new ArrayList<>();
    for (IExtension extension : extensions) {
      IConfigurationElement[] elements = extension.getConfigurationElements();
      for (IConfigurationElement element : elements) {
        try {
          Object detector = element.createExecutableExtension("class"); //$NON-NLS-1$
          if (detector instanceof io.github.xenogew.propedit.eclipse.plugin.editors.detector.IHyperlinkDetector) {
            ((io.github.xenogew.propedit.eclipse.plugin.editors.detector.IHyperlinkDetector) detector)
                .setTextEditor(editor);
            results.add((IHyperlinkDetector) detector);
          }
        } catch (CoreException e) {
          IStatus status =
              new Status(IStatus.ERROR, PropEditorXPlugin.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
          ILog log = PropEditorXPlugin.getDefault().getLog();
          log.log(status);
        }
      }
    }

    return results;
  }

}
