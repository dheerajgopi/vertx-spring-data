package org.example.userservice.common.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpHeaders;
import io.vertx.ext.web.Router;
import org.example.userservice.common.exception.ResourceNotFoundException;
import org.example.userservice.common.exception.RestApiException;
import org.example.userservice.common.http.ApiResponse;

/**
 * Abstract verticle for REST APIs containing common failure handlers
 * and functionality.
 */
public abstract class ApiVerticle extends AbstractVerticle {

    protected final Router apiRouter = Router.router(vertx);

    public ApiVerticle() {
        super();

        // catch-all mechanism for exceptions
        apiRouter.route("/*").failureHandler(ctx -> {
            ctx
                    .response()
                    .setStatusCode(ctx.statusCode())
                    .end(new ApiResponse(ctx.failure()).toJson().encodePrettily());
        });

        // put header values
        apiRouter
                .route("/*")
                .produces("application/json")
                .handler(ctx -> {
                    ctx
                            .response()
                            .setChunked(true)
                            .putHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=utf-8");

                    ctx.next();
                });

        // 404 handling
        apiRouter.route().last().handler(ctx -> {
            ctx
                    .response()
                    .setStatusCode(404)
                    .end(new ApiResponse(
                            new RestApiException(new ResourceNotFoundException("Route not found"))
                    ).toJson().encodePrettily());
        });
    }

    /**
     * Mount sub-routers with <code>/api</code> prefix.
     * @param router subrouter
     */
    protected void addSubRouter(final Router router) {
        apiRouter.mountSubRouter("/api", router);
    }

}
