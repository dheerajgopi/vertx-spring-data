package org.example.userservice.user;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.userservice.user.verticle.UserListVerticle;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class UserHttpServerVerticleTest {

    private Integer port = 8080;

    @BeforeEach
    void setUp(final Vertx vertx, final VertxTestContext context) {
        final DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("http.port", port));
        vertx.deployVerticle(new UserHttpServerVerticle(), options, context.completing());
    }

    @AfterEach
    void tearDown(final Vertx vertx, final VertxTestContext context) {
        vertx.close(context.completing());
    }

    @Test
    void testStart(final Vertx vertx, final VertxTestContext context) {
        vertx.eventBus().<JsonObject>consumer(UserListVerticle.USER_LIST_ADDRESS, new Handler<Message<JsonObject>>() {
            @Override
            public void handle(final Message<JsonObject> message) {
                final JsonObject response = new JsonObject().put("users", new JsonArray());
                message.reply(response);
            }
        });

        final WebClient client = WebClient.create(vertx);

        client.get(port, "localhost", "/api/v1/users").as(BodyCodec.string()).send(context.succeeding(res -> context.verify(() -> {
            assertEquals(200, res.statusCode());

            final JsonObject actual = new JsonObject(res.body());

            assertEquals(200, actual.getInteger("status"));
            assertTrue(actual.getJsonObject("data").getJsonArray("users").isEmpty());
            context.completeNow();
        })));
    }

    @Test
    void testStartForInvalidRoute(final Vertx vertx, final VertxTestContext context) {
        final WebClient client = WebClient.create(vertx);

        client.get(port, "localhost", "/api/v1/invalid").as(BodyCodec.string()).send(context.succeeding(res -> context.verify(() -> {
            assertEquals(404, res.statusCode());

            final JsonObject actual = new JsonObject(res.body());

            assertEquals(404, actual.getInteger("status"));
            assertTrue(!actual.getJsonObject("error").getString("message").isEmpty());
            context.completeNow();
        })));
    }
}
