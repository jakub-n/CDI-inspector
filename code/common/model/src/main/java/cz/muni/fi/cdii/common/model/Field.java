package cz.muni.fi.cdii.common.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * 
 * equality base on {@code name} and {@code surroundingType}
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.NONE)
public class Field implements Member {

    @JsonProperty
    private String name;

    @JsonProperty
    private Type type;

    @JsonProperty
    private Bean producedBean;

    @JsonProperty
    private InjectionPoint injectionPoint;
    
    /**
     * the only purpose of this field is equality computation
     */
    @JsonProperty
    private Type surroundingType;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Bean getProducedBean() {
        return producedBean;
    }

    public void setProducedBean(Bean producedBean) {
        this.producedBean = producedBean;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public void setInjectionPoint(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }

    public Type getSurroundingType() {
        return surroundingType;
    }

    public void setSurroundingType(Type surroundingType) {
        this.surroundingType = surroundingType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((surroundingType == null) ? 0 : surroundingType.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Field other = (Field) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (surroundingType == null) {
            if (other.surroundingType != null)
                return false;
        } else if (!surroundingType.equals(other.surroundingType))
            return false;
        return true;
    }

    @Override
    public String getNodeText() {
        return this.getType().toString(false, true) + " " + this.getName();
    }
    
    @Override
    public String getNodeTooltipText() {
        StringBuilder result = new StringBuilder();
        if (this.getProducedBean() != null) {
            result.append("@Produces").append("\n");
        }
        if (this.getInjectionPoint() != null) {
            result.append("@Inject").append("\n");
        }
        result.append(this.getType().toString(true, true));
        result.append(" ");
        result.append(this.getName());
        return result.toString();
    }

    @Override
    public String toString() {
        return this.getNodeText();
    }

    @Override
    public DetailsElement getDetails() {
        DetailsElement root = new DetailsElement();
        DetailsElement name = new DetailsElement("Name", this.getName());
        root.addSubElement(name);
        DetailsElement type = new DetailsElement("Type", this.getType().toString(true, true));
        root.addSubElement(type);
        if (this.getProducedBean() != null) {
            root.addSubElement(new DetailsElement("Produced bean", this.getProducedBean()));
        }
        if (this.getInjectionPoint() != null) {
            root.addSubElement(this.getInjectionPoint().getDetails());
        }
        
        return root;
    }
    
}
