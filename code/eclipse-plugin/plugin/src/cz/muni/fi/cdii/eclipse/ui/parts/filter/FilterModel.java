package cz.muni.fi.cdii.eclipse.ui.parts.filter;

import java.util.Set;

import cz.muni.fi.cdii.common.model.Qualifier;
import cz.muni.fi.cdii.common.model.Type;

/**
 * Class defining set of criteria for graph filtering
 *
 * @see FilterPartModelFactory
 */
public class FilterModel {

    private String className;
    private Set<String> packages;
    private Set<String> elNames;
    private Set<String> types;
    
    /**
     * string returned by {@link Type#toString(boolean, boolean)} with parameters {@code true, true}
     * <br>
     * e.g. "java.util.Set&lt;java.lang.Integer&gt;"
     */
    public Set<String> getTypes() {
        return types;
    }

    public void setTypes(Set<String> types) {
        this.types = types;
    }

    private Set<String> qualifiers;
    
    /**
     * Arbitrary user string
     */
    public String getClassName() {
        return className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    /**
     * e.g. "java.lang"
     */
    public Set<String> getPackages() {
        return packages;
    }
    
    public void setPackages(Set<String> packages) {
        this.packages = packages;
    }
    
    /**
     * expression language names
     */
    public Set<String> getElNames() {
        return elNames;
    }
    
    public void setElNames(Set<String> elNames) {
        this.elNames = elNames;
    }
    
    /**
     * String returned by {@link Qualifier#toString(boolean)} with parameter {@code true}
     */
    public Set<String> getQualifiers() {
        return qualifiers;
    }
    
    public void setQualifiers(Set<String> qualifiers) {
        this.qualifiers = qualifiers;
    }
    
    
}
