package org.example.userservice.user;

import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import org.example.userservice.common.exception.RestApiException;
import org.example.userservice.common.http.ApiResponse;
import org.example.userservice.common.verticle.ApiVerticle;
import org.example.userservice.user.verticle.UserListVerticle;

/**
 * HTTP server verticle for user APIs.
 */
public class UserHttpServerVerticle extends ApiVerticle {

    public UserHttpServerVerticle() {
        super();
    }

    /**
     * Add routers for user APIs and start an HTTP server.
     * @param promise
     */
    @Override
    public void start(final Promise<Void> promise) {
        final Router router = Router.router(vertx);

        router.route(HttpMethod.GET, "/v1/users").handler(req -> {
            vertx.eventBus().<JsonObject>request(UserListVerticle.USER_LIST_ADDRESS, null, res -> {
                if (res.succeeded()) {
                    req
                            .response()
                            .setStatusCode(200)
                            .end(new ApiResponse(200, res.result().body()).toJson().encodePrettily());
                } else {
                    final RestApiException restApiException = new RestApiException(
                            new JsonObject(res.cause().getMessage())
                    );
                    req.fail(restApiException.getHttpStatus(), restApiException);
                }
            });
        });

        addSubRouter(router);

        vertx
                .createHttpServer()
                .requestHandler(apiRouter)
                .listen(config().getInteger("http.port", 8080), res -> {
                    if (res.succeeded()) {
                        promise.complete();
                    } else {
                        promise.fail(res.cause());
                    }
                });
    }

}
