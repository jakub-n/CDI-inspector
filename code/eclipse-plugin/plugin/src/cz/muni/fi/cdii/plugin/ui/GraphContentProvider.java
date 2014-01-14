package cz.muni.fi.cdii.plugin.ui;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import cz.muni.fi.cdii.plugin.common.model.Bean;
import cz.muni.fi.cdii.plugin.common.model.Class;

public class GraphContentProvider extends ArrayContentProvider  implements IGraphEntityContentProvider {

	@Override
	public Object[] getConnectedTo(Object element) {
		if (element instanceof Class) {
			final Class clazz = (Class) element;
			return clazz.getBeans().toArray(new Bean[] {});
		}
		return new Object[] {};
	}

}
