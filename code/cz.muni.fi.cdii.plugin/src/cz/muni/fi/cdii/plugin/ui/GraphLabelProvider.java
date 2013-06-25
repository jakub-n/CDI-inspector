package cz.muni.fi.cdii.plugin.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.zest.core.viewers.EntityConnectionData;

import cz.muni.fi.cdii.plugin.model.IBean;
import cz.muni.fi.cdii.plugin.model.IEdge;

public class GraphLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof IBean) {
			final IBean bean = (IBean) element;
			return bean.getQalifiedName();
		}
		if (element instanceof IEdge) {
			// TODO
			return "edge";
		}
		if (element instanceof EntityConnectionData) {
			// nothing
			return "";
		}
		throw new RuntimeException("Wrong type in label provider: "
				+ element.getClass().toString());
	}

}
