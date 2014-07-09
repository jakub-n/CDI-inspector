package cz.muni.fi.cdii.webapp;

import java.util.Collections;
import java.util.Set;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

public class ManyMembers {
	
	@Inject
	public ManyMembers(int input) {
		System.out.println(input);
	}

	@Inject
	public int intMember1;
	
	@Inject
	public int intMember2;
	
	@Inject
	public int intMember3;
	
	@Inject
	public int intMember4;
	
	@Inject 
	public long longMemver;
	
	@Produces
	public long getLong() {
		return 23l;
	}
	
	@Produces
	public Set<Integer> emptySet = Collections.emptySet();
	
	@Produces
	@Named
	public String a = "a";
	
	@Produces
	@Named
	public String b = "b";
	
	@Produces
	public byte getByte(@Named("a") String a, @Named("b") String b) {
		return (byte) 3;
	}
	
	@Inject
	public InnerClass innerClassField;
	
	public static class InnerClass {}
}
