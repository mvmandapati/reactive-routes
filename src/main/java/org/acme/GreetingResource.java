package org.acme;

import io.quarkus.vertx.web.Param;
import io.quarkus.vertx.web.ReactiveRoutes;
import io.quarkus.vertx.web.Route;
import io.quarkus.vertx.web.RouteBase;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.annotation.security.RolesAllowed;

@RouteBase(path = "/api", produces = "application/json")
public class GreetingResource {

    @Route(path = "greetings", methods = Route.HttpMethod.GET)
    @RolesAllowed("greet")
    public Multi<Greeting> greetings() {
        return ReactiveRoutes.asJsonArray(Multi.createFrom()
                .items(new Greeting("Hello!"), new Greeting("Hola!"), new Greeting("Bonjour!"), new Greeting("Ciao!")));
    }

    @Route(path = "greetings/:msg", methods = Route.HttpMethod.GET)
    @RolesAllowed("greet")
    public Uni<Greeting> greeting(@Param("msg") String message) {
        return Uni.createFrom().item(new Greeting(message));
    }

}