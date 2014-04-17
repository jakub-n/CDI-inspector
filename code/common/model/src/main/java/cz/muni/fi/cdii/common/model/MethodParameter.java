package cz.muni.fi.cdii.common.model;

public class MethodParameter {

    private Type type;
    
    /**
     * null iff parameter is not injected
     */
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
