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
    
    
}
