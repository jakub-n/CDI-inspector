package cz.muni.fi.cdii.common.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * Represents java type. For non-parametrized types it is equal to {@link Class}. For parametrized 
 * types there can be multiple {@link Type}s for one {@link Class}.
 * <p>
 * Equality is based on members {@link #clazz}, {@link #typeParameters} and {@link #wildcardType}.
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Type implements Viewable {
	
//	/**
//	 * null iff {@link #wildcardType} is {@link TypeWildcardEnum#QUESTIONMARK}
//	 */
//
//	@JsonProperty
//	private Class clazz;
//	/**
//	 * null if 
//	 * <ul>
//	 * <li>{@link #wildcardType} is {@link TypeWildcardEnum#QUESTIONMARK}
//	 * <li>type is non-parametrized
//	 * </ul>
//	 */

//	@JsonProperty
//	private List<Type> typeParameters;

//	@JsonProperty
//	private TypeWildcardEnum wildcardType;
	
	// ----------------------------------------------------------
	
	private String package_;
	private String name;
	private List<Type> typeParameters = new ArrayList<Type>();
	private boolean isArray;
	
	private Set<Member> members = new HashSet<Member>();
	
	
	
	
	public String getPackage() {
        return package_;
    }
    public void setPackage(String package_) {
        this.package_ = package_;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Type> getTypeParameters() {
        return typeParameters;
    }
    
    public void setTypeParameters(List<Type> typeParameters) {
        this.typeParameters = typeParameters;
    }
    
    public Set<Member> getMembers() {
        return members;
    }
    
    public void setMembers(Set<Member> members) {
        this.members = members;
    }
    
    public boolean isArray() {
        return isArray;
    }
    public void setArray(boolean isArray) {
        this.isArray = isArray;
    }
    /**
     * Gets member by it's name.
     * @param name requested member name
     * @return requested member or null iff member of that time does not exist
     */
    public Member getMemberByName(final String name) {
        for (Member member : this.members) {
            if (name.equals(member.getName())) {
                return member;
            }
        }
        return null;
    }
    
    public String getFullyQualifiedName() {
        return this.toString(true, false);
    }
    
    /**
     * 
     * @param qualified show names in qualified form
     * @param showTypeParams show type parameters
     * @return string representation of type
     */
    public String toString(final boolean qualified, final boolean showTypeParams) {
        StringBuilder resultBuilder = new StringBuilder();
        if (qualified && !this.package_.isEmpty()) {
            resultBuilder.append(this.package_).append(".");
        }
        resultBuilder.append(this.name);
        if ((!this.typeParameters.isEmpty()) && showTypeParams) {
            resultBuilder.append("<");
            for (Type param : this.typeParameters) {
                resultBuilder.append(param.toString(qualified, true)).append(",");
            }
            resultBuilder.deleteCharAt(resultBuilder.length() - 1);
            resultBuilder.append(">");
        }
        if (this.isArray) {
            resultBuilder.append("[]");
        }
        return resultBuilder.toString();
    }
    
    @Override
    public String toString() {
        return this.toString(true, true);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isArray ? 1231 : 1237);
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((package_ == null) ? 0 : package_.hashCode());
        result = prime * result + ((typeParameters == null) ? 0 : typeParameters.hashCode());
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
        Type other = (Type) obj;
        if (isArray != other.isArray)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (package_ == null) {
            if (other.package_ != null)
                return false;
        } else if (!package_.equals(other.package_))
            return false;
        if (typeParameters == null) {
            if (other.typeParameters != null)
                return false;
        } else if (!typeParameters.equals(other.typeParameters))
            return false;
        return true;
    }
    public String getNodeText() {
        return this.toString(false, true);
    }
    @Override
    public String getNodeTooltipText() {
        return this.toString(true, true);
    }
    
    // TODO
    @Override
    public DetailsElement getDetails() {
        DetailsElement root = new DetailsElement();
        root.addSubElement(new DetailsElement("Package:", this.getPackage()));
        root.addSubElement(new DetailsElement("Name:", this.getName()));
        return root;
    }
    
    
}
