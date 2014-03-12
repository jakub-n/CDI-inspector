package cz.muni.fi.cdii.plugin.ui.detailview;

import org.eclipse.jface.resource.ImageDescriptor;

public abstract class Action {

    final private ImageDescriptor image;
    final private String tooltipText;
    
    public Action(ImageDescriptor image, String tooltipText) {
        this.image = image;
        this.tooltipText = tooltipText;
    }

    public ImageDescriptor getImage() {
        return image;
    }

    public String getTooltipText() {
        return tooltipText;
    }

    public abstract void perform();
}
