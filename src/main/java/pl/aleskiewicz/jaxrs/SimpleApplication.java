package pl.aleskiewicz.jaxrs;


import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/api")
public class SimpleApplication extends ResourceConfig {

    public SimpleApplication() {
        super(JacksonFeature.class);
        packages(SimpleApplication.class.getPackage().getName());
    }
}
