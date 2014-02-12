package cz.muni.fi.cdii.webapp.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import cz.muni.fi.cdii.common.model.Model;

@Path("/")
public class Rest {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Model getModel() {
		return new Model();
	}
}
