package cz.muni.fi.cdii.common.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

/**
 * Represents java type. For non-parametrized types it is equal to {@link Class}. For parametrized 
 * types there can be multiple {@link Type}s for one {@link Class}.
 * <p>
 * Equality is based on members {@link #clazz}, {@link #typeParameters} and {@link #wildcardType}.
 */
@JsonIdentityInfo(generator=ObjectIdGenerators.IntSequenceGenerator.class, property="@id")
@JsonAutoDetect(getterVisibility=Visibility.NONE, setterVisibility=Visibility.NONE, 
	isGetterVisibility=Visibility.NONE, fieldVisibility=Visibility.DEFAULT)
public class Type {
	
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
	private List<Type> typeParameters;
	
	
	
	
	
	
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
    
    private String toString(final boolean qualified) {
        StringBuilder resultBuilder = new StringBuilder();
        if (qualified) {
            resultBuilder.append(this.package_).append(".");
        }
        resultBuilder.append(this.name);
        if (! this.typeParameters.isEmpty()) {
            resultBuilder.append("<");
            for (Type param : this.typeParameters) {
                resultBuilder.append(param.toString(qualified)).append(",");
            }
            resultBuilder.deleteCharAt(resultBuilder.length() - 1);
            resultBuilder.append(">");
        }
        return resultBuilder.toString();
    }
    
    @Override
    public String toString() {
        return this.toString(true);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((package_ == null) ? 0 : package_.hashCode());
        for (Type param : this.getTypeParameters()) {
            result = prime * result + param.hashCode();
        }
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
        } else if (!paramEquals(this.typeParameters, other.typeParameters))
            return false;
        return true;
    }
    private static boolean paramEquals(List<Type> paramsA, List<Type> paramsB) {
        if (paramsA.size() != paramsB.size()) {
            return false;
        }
        for (int i = 0; i < paramsA.size(); i++) {
            Type paramA = paramsA.get(i);
            Type paramB = paramsB.get(i);
            if (paramA == null && paramB != null) {
                return false;
            }
            if (!paramA.equals(paramB)) {
                return false;
            }
        }
        return true;
    }
    
 
	
	
}
