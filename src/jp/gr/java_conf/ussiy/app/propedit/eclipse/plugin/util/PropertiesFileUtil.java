package jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.util;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.PropertiesEditorPlugin;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class PropertiesFileUtil {

	public static IFile[] findFileExt(IContainer container, IPath excludePath, String extension) {
		IResource[] list = null;
		try {
			list = container.members();
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, PropertiesEditorPlugin.PLUGIN_ID, IStatus.OK, e.getMessage(), e);
			ILog log = PropertiesEditorPlugin.getDefault().getLog();
			log.log(status);
			return new IFile[0];
		}
		if (list == null) {
			return new IFile[0];
		}
		List<IFile> fileList = new ArrayList<>();
		for (IResource resource : list) {
			if (resource instanceof IFile) {
				if (extension.equals(resource.getFileExtension())) {
					fileList.add((IFile) resource);
				}
			} else 	if (resource instanceof IContainer) {
				if (excludePath != null && excludePath.matchingFirstSegments(resource.getFullPath()) == excludePath.segmentCount()) {
					continue;
				}
				IFile[] files = findFileExt((IContainer) resource, excludePath, extension);
				for (IFile file : files) {
					fileList.add(file);
				}
			}
		}
		
		return fileList.toArray(new IFile[0]);
	}
}
