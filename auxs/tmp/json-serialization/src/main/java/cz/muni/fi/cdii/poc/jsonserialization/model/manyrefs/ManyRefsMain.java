package cz.muni.fi.cdii.poc.jsonserialization.model.manyrefs;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class ManyRefsMain {

	public static void main(String[] args) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		
		Container container = new Container();
		container.generate();
		String json = mapper.writeValueAsString(container);
		
		System.out.println(json);
		
		Container readContainer = mapper.readValue(json, Container.class);
		System.out.println(readContainer);
	}
	
	public static String getHashCode(Object obj) {
		return String.valueOf((obj == null) ? "null" : obj.hashCode());
	}

}
