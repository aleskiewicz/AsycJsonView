package pl.aleskiewicz.jaxrs;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.annotate.JsonView;

import pl.aleskiewicz.jaxrs.JsonViews.DetailsWebView;
import pl.aleskiewicz.jaxrs.JsonViews.WebView;


@Path("simple")
public class SimpleResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("async")
    @JsonView(DetailsWebView.class)
    public void async(@Suspended final AsyncResponse ar,
            @QueryParam("offset") @DefaultValue("0") final int offset)
                    throws InterruptedException {
        SimpleService.INSTANCE.addResponseListener(ar, offset);
    }

    @GET
    @Path("sync")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(WebView.class)
    public Response sync(@QueryParam("limit") @DefaultValue("10") int limit,
            @QueryParam("offset") @DefaultValue("0") int offset)
                    throws InterruptedException {
        return Response.ok(SimpleService.INSTANCE.list(limit, offset)).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkActivityChange(SimpleEntry entry) {
        // TODO remove this method, it is just temporary
        SimpleService.INSTANCE.notifyListeners(entry);
        return Response.ok(entry).build();
    }
}
