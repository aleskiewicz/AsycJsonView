package pl.aleskiewicz.jaxrs;


import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

@ApplicationPath("/api")
public class SimpleApplication extends ResourceConfig {

    public SimpleApplication() {
        super(JacksonJaxbJsonProvider.class);
        packages(SimpleApplication.class.getPackage().getName());
    }
}
