package cz.muni.fi.cdii.common.model;

/**
 * Interface for classes that serve as skeleton for graph nodes.
 */
public interface Viewable {

    /**
     * Text shown in node corresponding to instance of this interface.
     */
    public String getNodeText();
    
    /**
     * Tooltip text for nodes corresponding to instance of this interface.
     */
    public String getNodeTooltipText();
    
    /**
     * Details part content.
     */
    public DetailsElement getDetails();
}
