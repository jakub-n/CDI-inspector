package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import java.util.Set;

/**
 * Class defining set of criteria for graph filtering
 *
 */
public class FilterModel {

    private String className;
    private Set<String> packages;
    private Set<String> elNames;
    private Set<String> types;
    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    private Set<String> qualifiers;
    
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public Set<String> getPackages() {
        return packages;
    }
    
    public void setPackages(Set<String> packages) {
        this.packages = packages;
    }
    
    public Set<String> getElNames() {
        return elNames;
    }
    
    public void setElNames(Set<String> elNames) {
        this.elNames = elNames;
    }
    
    public Set<String> getQualifiers() {
        return qualifiers;
    }
    
    public void setQualifiers(Set<String> qualifiers) {
        this.qualifiers = qualifiers;
    }
    
    
}
