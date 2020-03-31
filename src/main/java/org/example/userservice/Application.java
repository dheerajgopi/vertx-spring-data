package org.example.userservice;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.example.userservice.config.JpaConfig;
import org.example.userservice.user.UserHttpServerVerticle;
import org.example.userservice.user.verticle.UserListVerticle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Application {

    public static void main(final String[] args) {
        final ApplicationContext ctx = new AnnotationConfigApplicationContext(JpaConfig.class);
        final Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(5));
        final DeploymentOptions options = new DeploymentOptions().setWorker(true);
        options.setWorkerPoolSize(5);

        vertx.deployVerticle(new UserListVerticle(ctx), options, res -> {
            if (res.succeeded()) {
                System.out.println("user list verticle deployed");
            } else {
                System.out.println("user list verticle deployment failed");
            }
        });
        vertx.deployVerticle(new UserHttpServerVerticle(), res -> {
            if (res.succeeded()) {
                System.out.println("user HTTP server verticle deployed");
            } else {
                System.out.println("user HTTP server verticle deployment failed");
            }
        });
    }

}
