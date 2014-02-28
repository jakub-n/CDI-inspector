package cz.muni.fi.cdii.common.model;

public interface Member {
    
    public String getName();
    
    public Type getType();
    
    public Bean getProducedBean();
    
    public void setProducedBean(final Bean producedBean);

    public void setName(final String elementName);

    public void setType(final Type type);
}
