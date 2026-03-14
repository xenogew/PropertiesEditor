package io.github.xenogew.propedit.eclipse.plugin.editors.view.outline;

import io.github.xenogew.propedit.eclipse.plugin.PropertiesEditorPlugin;
import java.net.URL;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class PropertiesOutlineLabelProvider extends LabelProvider {
  protected static Image markerImage;

  /**
   * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
   */
  @Override
  public Image getImage(Object element) {
    if (markerImage != null && !markerImage.isDisposed()) {
      return markerImage;
    }
    URL url = PropertiesEditorPlugin.getDefault().getBundle().getEntry("/icons/outlineMarker.png"); //$NON-NLS-1$
    ImageDescriptor descriptor = null;
    descriptor = ImageDescriptor.createFromURL(url);
    markerImage = descriptor.createImage();
    return markerImage;
  }

  /**
   * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
   */
  @Override
  public void dispose() {
    super.dispose();
    if (markerImage != null) {
      markerImage.dispose();
    }
  }
}
