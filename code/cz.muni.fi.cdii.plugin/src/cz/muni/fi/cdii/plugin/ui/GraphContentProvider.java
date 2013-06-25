package cz.muni.fi.cdii.plugin.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

public class GraphContentProvider extends ArrayContentProvider  implements IGraphEntityContentProvider {

	@Override
	public Object[] getConnectedTo(Object element) {
		// TODO dopsat
		return new Object[] {};
	}

}
