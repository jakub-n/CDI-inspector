package cz.muni.fi.cdii.plugin.visual.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import cz.muni.fi.cdii.plugin.common.model.CdiInspection;
import cz.muni.fi.cdii.plugin.common.model.Class;

/**
 * Root class of local implementation of {@link CdiInspection}.
 *
 */
public class LocalCdiInspection extends CdiInspection {
	
	private Set<LocalClass> classes = new HashSet<LocalClass>();
	
	public LocalCdiInspection() {
	}
	
	public void setClasses(Set<LocalClass> classes) {
		this.classes = classes;
	}

	@Override
	public Set<? extends Class> getClasses() {
		return Collections.unmodifiableSet(this.classes);
	}
	
	@Override
	public String toString() {
		return "Inspection: " + this.classes.toString();
	}

	@Override
	public CdiInspection refresh() {
		// TODO Auto-generated method stub
		return null;
	}
}
