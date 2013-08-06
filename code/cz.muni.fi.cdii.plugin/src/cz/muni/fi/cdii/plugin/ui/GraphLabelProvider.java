package cz.muni.fi.cdii.plugin.ui;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;

import cz.muni.fi.cdii.plugin.common.model.Bean;
import cz.muni.fi.cdii.plugin.common.model.LabelText;

// TODO reimplement to use org.eclipse.zest.core.viewers.IFigureProvider (plugin org.eclipse.zest.jface (2.0.0.201307152033))
public class GraphLabelProvider extends LabelProvider implements IEntityStyleProvider {

	private ColorManager colorManager;

	public GraphLabelProvider(ColorManager colorManager) {
		this.colorManager = colorManager;
	}

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

	@Override // selected node
	public Color getNodeHighlightColor(Object entity) {
		if (entity instanceof cz.muni.fi.cdii.plugin.common.model.Class) {
			return this.colorManager.getNamedColor(ColorManager.GraphColorEnum.CLASS_SELECTED_NODE_COLOR);
		}
		if (entity instanceof Bean) {
			return this.colorManager.getNamedColor(ColorManager.GraphColorEnum.BEAN_SELECTED_NODE_COLOR);
		}
		throw new RuntimeException("Unknown node type");
	}

	@Override // border unselected node
	public Color getBorderColor(Object entity) {
		return this.colorManager.getNamedColor(ColorManager.GraphColorEnum.BORDER_COLOR);
	}

	@Override // border selected node
	public Color getBorderHighlightColor(Object entity) {
		return this.colorManager.getNamedColor(ColorManager.GraphColorEnum.SELECTED_BORDER_COLOR);
	}

	@Override
	public int getBorderWidth(Object entity) {
		return 1;
	}

	@Override // unselected node
	public Color getBackgroundColour(Object entity) {
		if (entity instanceof cz.muni.fi.cdii.plugin.common.model.Class) {
			return this.colorManager.getNamedColor(ColorManager.GraphColorEnum.CLASS_NODE_COLOR);
		}
		if (entity instanceof Bean) {
			return this.colorManager.getNamedColor(ColorManager.GraphColorEnum.BEAN_NODE_COLOR);
		}
		throw new RuntimeException("Unknown node type");
	}

	@Override // font color
	public Color getForegroundColour(Object entity) {
		return this.colorManager.getNamedColor(ColorManager.GraphColorEnum.FONT_COLOR);
	}

	@Override
	public IFigure getTooltip(Object entity) {
		return null;
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		return true;
	}

}
