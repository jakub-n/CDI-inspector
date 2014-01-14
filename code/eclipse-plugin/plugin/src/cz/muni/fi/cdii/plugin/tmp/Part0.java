package cz.muni.fi.cdii.plugin.tmp;

import javax.annotation.PostConstruct;

public class Part0 {
	
	public static final String ID = "cz.muni.fi.cdii.tmp.partDesc0";
	
	@PostConstruct
	public void createControl() {
		System.out.println("Part0.createControl() called");
	}

}
