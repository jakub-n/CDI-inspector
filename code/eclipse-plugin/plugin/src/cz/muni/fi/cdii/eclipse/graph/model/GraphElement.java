package cz.muni.fi.cdii.eclipse.graph.model;

import cz.muni.fi.cdii.common.model.Viewable;
import cz.muni.fi.cdii.eclipse.ui.parts.details.DetailsElement;


public interface GraphElement {


    public Viewable getOrigin();
    
    public String getVertexType();
    
    public DetailsElement getDetails();
    
    public String getDetailsLinkLabel();
    
}
