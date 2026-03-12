package jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.jdt.proposal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.resources.Messages;
import jp.gr.java_conf.ussiy.app.propedit.eclipse.plugin.util.ProjectProperties;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.ui.text.java.ContentAssistInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposalComputer;
import org.eclipse.jdt.ui.text.java.JavaContentAssistInvocationContext;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContextInformation;

public class PropertiesCompletionProposalComputer implements
		IJavaCompletionProposalComputer {

	@Override
	public List<ICompletionProposal> computeCompletionProposals(ContentAssistInvocationContext context,
			IProgressMonitor monitor) {
		if (!(context instanceof JavaContentAssistInvocationContext)) {
			return Collections.emptyList();
		}
		
		JavaContentAssistInvocationContext jContext = (JavaContentAssistInvocationContext)context;
		IJavaProject jProject = jContext.getProject();
		IProject project = jProject.getProject();

		List<String> keyList = new ArrayList<>(ProjectProperties.getInstance().getProperty(project).stringPropertyNames());
		
		String source = context.getDocument().get();
		int offset = context.getInvocationOffset();
		int idx = source.charAt(offset) == '\"' ? source.lastIndexOf("\"", offset - 1) : source.lastIndexOf("\"", offset); //$NON-NLS-1$ //$NON-NLS-2$
		StringBuilder buf = new StringBuilder();
		for (int i = idx + 1; i < offset; i++) {
			char c = source.charAt(i);
			buf.append(c);
		}
		
		String match = buf.toString();
		
		List<ICompletionProposal> list = new ArrayList<>();
		
		Collections.sort(keyList);
		for (String key : keyList) {
			if (key.startsWith(match)) {
				list.add(new CompletionProposal(key, offset - match.length(),
						match.length(), key.length()));
			}
		}
		
		return list;
	}

	@Override
	public List<IContextInformation> computeContextInformation(ContentAssistInvocationContext arg0,
			IProgressMonitor arg1) {
		return Collections.emptyList();
	}

	@Override
	public String getErrorMessage() {
		return Messages.getString("eclipse.propertieseditor.PropertiesCompletionProposalComputer.4"); //$NON-NLS-1$
	}

	@Override
	public void sessionEnded() {
	}

	@Override
	public void sessionStarted() {
	}

}
