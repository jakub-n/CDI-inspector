package cz.muni.fi.cdii.common.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

public class InjectionPoint {
    
    /**
     * type required for injection
     */
    // TODO maybe it is not necessary
    @JsonProperty
    private Type type;

	@JsonProperty
	private Set<Qualifier> qualifiers = new HashSet<>();

	/**
	 * typically contains exactly one element
	 */
	@JsonProperty
	private Set<Bean> resolvedBeans = new HashSet<>();
	
	@JsonProperty
	private String elName;
	
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<Qualifier> getQualifiers() {
        return qualifiers;
    }

    public void setQualifiers(Set<Qualifier> qualifiers) {
        this.qualifiers = qualifiers;
    }

    public Set<Bean> getResolvedBeans() {
        return resolvedBeans;
    }

    public void setResolvedBeans(Set<Bean> resolvedBeans) {
        this.resolvedBeans = resolvedBeans;
    }

    public String getElName() {
        return elName;
    }

    public void setElName(String elName) {
        this.elName = elName;
    }

    public DetailsElement getDetails() {
        DetailsElement root = new DetailsElement("Injection point", "");
        if (this.getElName() != null) {
            root.addSubElement(new DetailsElement("EL name", 
                    this.getElName() == null ? "" : this.getElName()));
        }
        addResolvedBeans(root);
        if (!this.getQualifiers().isEmpty()) {
            root.addSubElement(Utils.getQualifiersDetails(this.getQualifiers()));
        }
        return root;
    }

    private void addResolvedBeans(DetailsElement root) {
        if (this.getResolvedBeans().size() == 1) {
            DetailsElement oneBeanDetails = new DetailsElement("Resolved bean", 
                    this.getResolvedBeans().toArray(new Bean[0])[0]);
            root.addSubElement(oneBeanDetails);
            return;
        } else {
            DetailsElement beansRoot = new DetailsElement("Resolved beans", "");
            for (Bean resolvedBean : this.getResolvedBeans()) {
                beansRoot.addSubElement(new DetailsElement("", resolvedBean));
            }
            root.addSubElement(beansRoot);
        }
        
    }
}