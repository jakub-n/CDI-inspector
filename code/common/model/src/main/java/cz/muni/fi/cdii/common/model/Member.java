package cz.muni.fi.cdii.common.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="@type")
@JsonSubTypes({@com.fasterxml.jackson.annotation.JsonSubTypes.Type(name="field", value=Field.class),
    @com.fasterxml.jackson.annotation.JsonSubTypes.Type(name="method", value=Method.class)})
public interface Member extends Viewable {
    
    public String getName();
    
    public Type getType();
    
    public Bean getProducedBean();
    
    public void setProducedBean(final Bean producedBean);

    public void setName(final String elementName);

    /**
     * @param type return type / type of field
     */
    public void setType(final Type type);
    
    public String getNodeText();
    
    public Type getSurroundingType();    
    
    /**
     * @param surroundingType type this member lives in
     */
    public void setSurroundingType(Type surroundingType);
    
    public String getDetailsLinkLabel();
}
