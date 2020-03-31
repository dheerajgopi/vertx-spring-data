package org.example.userservice.user.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.example.userservice.common.exception.RestApiException;
import org.example.userservice.entity.User;
import org.example.userservice.user.service.UserService;
import org.example.userservice.user.dto.UserDto;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Verticle for user listing.
 */
public class UserListVerticle extends AbstractVerticle {

    /**
     * Message handler address for user listing.
     */
    public static final String USER_LIST_ADDRESS = "user.list";

    /**
     * User service.
     */
    private UserService userService;

    public UserListVerticle(final ApplicationContext appContext) {
        super();
        this.userService = (UserService) appContext.getBean("userService");
    }

    /**
     * Message handler for user listing.
     * @param userService user service
     * @return Message containing the user list
     */
    private Handler<Message<JsonObject>> userListHandler(final UserService userService) {
        return msg -> vertx.<List<UserDto>>executeBlocking(promise -> {
            final List<User> users = userService.fetchAll();
            final List<UserDto> userDtos = users.stream().map(UserDto::new).collect(Collectors.toList());
            promise.complete(userDtos);
        }, false, res -> {
            if (res.succeeded()) {
                final JsonObject reply = new JsonObject();
                final JsonArray userList = new JsonArray();
                res.result().forEach(user -> userList.add(user.toJson()));
                reply.put("users", userList);

                msg.reply(reply);
            } else {
                final RestApiException restApiException = new RestApiException(res.cause());

                msg.fail(restApiException.getHttpStatus(), restApiException.toJson().encodePrettily());
            }
        });
    }

    /**
     * Registers consumer for user listing message handler.
     */
    @Override
    public void start() {
        vertx.eventBus().<JsonObject>consumer(USER_LIST_ADDRESS).handler(userListHandler(userService));
    }
}
