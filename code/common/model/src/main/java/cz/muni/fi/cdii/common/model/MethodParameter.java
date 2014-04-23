package cz.muni.fi.cdii.common.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
    isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.NONE)
public class MethodParameter {

    @JsonProperty
    private Type type;
    
    /**
     * null iff parameter is not injected
     */
    @JsonProperty
    private InjectionPoint injectionPoint;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public InjectionPoint getInjectionPoint() {
        return injectionPoint;
    }

    public void setInjectionPoint(InjectionPoint injectionPoint) {
        this.injectionPoint = injectionPoint;
    }
    
    public DetailsElement getDetails() {
        DetailsElement root = new DetailsElement("", this.getType().toString(true, true));
        if (this.getInjectionPoint() != null) {
            root.addSubElement(this.getInjectionPoint().getDetails());
        }
        return root;
    }
}
