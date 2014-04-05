package cz.muni.fi.cdii.eclipse.model;

import org.jboss.tools.cdi.core.IBean;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import cz.muni.fi.cdii.common.model.Bean;

@JsonDeserialize(as=LocalBean.class)
public class LocalBean extends Bean {
    
    private IBean jbossBean;

	public LocalBean() {
	    super();
	}
	
	public void setJbossBean(IBean jbossBean) {
	    this.jbossBean = jbossBean;
	}
	
	public void open() {
	    this.jbossBean.open();
	}

}
