package cz.muni.fi.cdii.common.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Something that can by injected and/or is produced.
 * Set of objects uniquely identified by 
 * <ul>
 * <li> nonempty set of types
 * <li> nonempty set of qualifiers
 * <li> scope
 * <li> EL name (optional)
 * <li> set of interceptor bindings
 * </ul>
 * Beans also may have alternatives.
 * <p>
 * Equality based on type, typeSet, scope, elName, qualifiers, interceptorBindings
 * <p>
 * @see <a href="http://docs.jboss.org/cdi/spec/1.1/cdi-spec.html#concepts?>
 *      http://docs.jboss.org/cdi/spec/1.1/cdi-spec.html#concepts</a>
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Bean implements Viewable {

	@JsonProperty
	private Type type;
	
	@JsonProperty
	private Set<Type> typeSet;
	
	@JsonProperty
	private Set<Qualifier> effectiveQualifierSet = new HashSet<>();
	
	@JsonProperty
	private Scope scope;
	
	@JsonProperty
	private String elName;
	
	@JsonProperty
	private Set<InterceptorBinding> interceptorBindings;
	
	@JsonProperty
	private Set<Delegate> associatedDeletates;

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Set<Type> getTypeSet() {
        return typeSet;
    }

    public void setTypeSet(Set<Type> typeSet) {
        this.typeSet = typeSet;
    }

    public Set<Qualifier> getQualifiers() {
        return effectiveQualifierSet;
    }

    public void setQualifiers(Set<Qualifier> effectiveQualifierSet) {
        this.effectiveQualifierSet = effectiveQualifierSet;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public String getElName() {
        return elName;
    }

    public void setElName(String elName) {
        this.elName = elName;
    }
	
    @Override
	public String toString() {
	    return "Bean " + this.type + "[typeset: " + this.typeSet + "]";
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((effectiveQualifierSet == null) ? 0 : effectiveQualifierSet.hashCode());
        result = prime * result + ((elName == null) ? 0 : elName.hashCode());
        result = prime * result
                + ((interceptorBindings == null) ? 0 : interceptorBindings.hashCode());
        result = prime * result + ((scope == null) ? 0 : scope.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((typeSet == null) ? 0 : typeSet.hashCode());
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
        Bean other = (Bean) obj;
        if (effectiveQualifierSet == null) {
            if (other.effectiveQualifierSet != null)
                return false;
        } else if (!effectiveQualifierSet.equals(other.effectiveQualifierSet))
            return false;
        if (elName == null) {
            if (other.elName != null)
                return false;
        } else if (!elName.equals(other.elName))
            return false;
        if (interceptorBindings == null) {
            if (other.interceptorBindings != null)
                return false;
        } else if (!interceptorBindings.equals(other.interceptorBindings))
            return false;
        if (scope == null) {
            if (other.scope != null)
                return false;
        } else if (!scope.equals(other.scope))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        } else if (!type.equals(other.type))
            return false;
        if (typeSet == null) {
            if (other.typeSet != null)
                return false;
        } else if (!typeSet.equals(other.typeSet))
            return false;
        return true;
    }

    @Override
    public String getNodeText() {
        StringBuilder result = new StringBuilder();
        for (Qualifier qualifier : this.getQualifiers()) {
            result.append(qualifier.toString()).append("\n");
        }
        if (this.getElName() != null) {
            result.append("@Named(\"" + this.getElName() + "\")").append("\n");
        }
        result.append(this.getScope().toString()).append("\n");
        result.append(this.getType().toString(false, true));
        return result.toString();
    }

    @Override
    public String getNodeTooltipText() {
        return this.getType().toString(true, true);
    }

    // TODO
    @Override
    public DetailsElement getDetails() {
        DetailsElement root = new DetailsElement();
        root.addSubElement(new DetailsElement("Type", this.getType()));
        root.addSubElement(new DetailsElement("EL name", 
                this.getElName() == null ? "" : this.getElName()));
        return root;
    };
    
}
