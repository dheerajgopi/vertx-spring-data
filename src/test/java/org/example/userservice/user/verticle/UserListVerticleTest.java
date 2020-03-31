package org.example.userservice.user.verticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.ReplyException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.example.userservice.entity.User;
import org.example.userservice.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(VertxExtension.class)
class UserListVerticleTest {

    private Integer port = 8080;

    private ApplicationContext appContext;

    private UserService userService;

    @BeforeEach
    void setUp(final Vertx vertx, final VertxTestContext context) {
        appContext = Mockito.mock(ApplicationContext.class);
        userService = Mockito.mock(UserService.class);
        Mockito.when(appContext.getBean("userService")).thenReturn(userService);

        final DeploymentOptions options = new DeploymentOptions()
                .setConfig(new JsonObject().put("http.port", port));
        vertx.deployVerticle(new UserListVerticle(appContext), options, context.completing());
    }

    @AfterEach
    void tearDown(final Vertx vertx, final VertxTestContext context) {
        vertx.close(context.completing());
    }

    @Test
    public void testStartForRuntimeException(final Vertx vertx, final VertxTestContext context) {
        Mockito.when(userService.fetchAll()).thenThrow(new RuntimeException("error"));

        vertx.eventBus().<JsonObject>request(UserListVerticle.USER_LIST_ADDRESS, null, context.failing(res -> {
            context.verify(() -> {
                assertTrue(res instanceof ReplyException);
                assertEquals(500, ((ReplyException) res).failureCode());

                context.completeNow();
            });
        }));
    }

    @Test
    public void testStart(final Vertx vertx, final VertxTestContext context) {
        List<User> users = Arrays.asList(createUser(1L, "test name"));
        Mockito.when(userService.fetchAll()).thenReturn(users);

        vertx.eventBus().<JsonObject>request(UserListVerticle.USER_LIST_ADDRESS, null, context.succeeding(res -> {
            context.verify(() -> {
                final JsonObject actual = res.body();

                assertNotNull(actual);
                assertTrue(actual.containsKey("users"));

                final JsonArray userArray = actual.getJsonArray("users");

                assertEquals(1, userArray.size());
                assertEquals(1L, userArray.getJsonObject(0).getLong("id"));
                assertEquals("test name", userArray.getJsonObject(0).getString("name"));

                context.completeNow();
            });
        }));
    }

    private User createUser(final Long id, final String name) {
        final LocalDateTime now = LocalDateTime.now();
        final User user = new User();
        user.setId(id);
        user.setName(name);
        user.setIsActive(true);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        return user;
    }
}
