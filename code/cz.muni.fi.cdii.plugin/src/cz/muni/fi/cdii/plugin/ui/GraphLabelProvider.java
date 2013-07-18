package cz.muni.fi.cdii.plugin.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import cz.muni.fi.cdii.plugin.common.model.LabelText;

public class GraphLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof LabelText) {
			final LabelText labeledElement = (LabelText) element;
			return labeledElement.toLabelString();
		}
		if (element instanceof EntityConnectionData) {
			// nothing
			return "";
		}
		throw new RuntimeException("Wrong type in label provider: "
				+ element.getClass().toString());
	}

}
