package cz.muni.fi.cdii.webapp;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
	
@Named
@SessionScoped
public class HelloBean implements Serializable, Externalizable{
	
	private boolean everUsed = false;
	private String creationTimestamp;
	
	public HelloBean() {
		this.creationTimestamp = (new Date()).toString();
	}
	
   
   public String getHello() {
	   String prefix = this.everUsed ? "" : "first usage ";
	   this.everUsed = true;
	   return prefix + this.creationTimestamp;
   }


@Override
public void writeExternal(ObjectOutput out) throws IOException {
	out.writeBoolean(this.everUsed);
	out.writeObject(this.creationTimestamp);
	
}


@Override
public void readExternal(ObjectInput in) throws IOException,
		ClassNotFoundException {
	this.everUsed = in.readBoolean();
	this.creationTimestamp = (String) in.readObject();
	
}

}
