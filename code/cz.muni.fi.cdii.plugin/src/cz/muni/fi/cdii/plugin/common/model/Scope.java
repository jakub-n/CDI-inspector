package cz.muni.fi.cdii.plugin.common.model;

public class Scope {
	
	public static Scope UNDEFINED = new Scope();
	
	private String name;
	private final boolean isNormal;
	private final boolean isImplicit;
	
	private Scope() {
		this.name = "javax.enterprise.context.Dependent";
		this.isNormal = false;
		this.isImplicit = true;
	}
	
	public Scope(String qualifiedName, boolean isNormal) {
		this.isImplicit = false;
		this.isNormal = isNormal;
		this.name = qualifiedName.trim();
	}

	public String getName() {
		return name;
	}

	public boolean isNormal() {
		return isNormal;
	}

	public boolean isImplicit() {
		return isImplicit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isImplicit ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Scope other = (Scope) obj;
		if (isImplicit != other.isImplicit)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (this.isImplicit) {
			return this.name + " (undefined)";
		} else {
			return this.name + (this.isNormal ? " (normal)" : " (pseudo)");
		}
	}
	
}
