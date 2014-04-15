package cz.muni.fi.cdii.eclipse.graph.model;

import com.tinkerpop.frames.Property;

import cz.muni.fi.cdii.common.model.Viewable;


public interface GraphElement {

    public static final String NEIGHBOR_HIGHLIGHT = "neighborHighlight";

    public Viewable getOrigin();
    
    // TODO smazat
    @Property(NEIGHBOR_HIGHLIGHT)
    public boolean getNeighborHighlight();

    @Property(NEIGHBOR_HIGHLIGHT)
    public void setNeighborHighlight(boolean neighborHighlight);
    
}
