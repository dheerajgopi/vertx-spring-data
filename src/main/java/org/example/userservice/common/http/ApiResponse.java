package org.example.userservice.common.http;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import org.example.userservice.common.exception.RestApiException;

/**
 * Container for all REST API responses.
 */
@Getter
public class ApiResponse implements JsonResponse {

    /**
     * HTTP status.
     */
    private Integer status;

    /**
     * Error.
     */
    private Error error;

    /**
     * Payload.
     */
    private JsonObject data;

    public ApiResponse(final Integer status, final JsonObject data) {
        this.status = status;
        this.data = data;
    }

    public ApiResponse(final Throwable ex) {
        final Error error = new Error(ex.getMessage());

        if (ex instanceof RestApiException) {
            this.status = ((RestApiException) ex).getHttpStatus();
        } else {
            this.status = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
            error.message = "internal server error";
        }

        this.error = error;
    }

    @Override
    public JsonObject toJson() {
        final JsonObject jsonResponse = new JsonObject();

        jsonResponse.put("status", this.status);

        if (this.error != null) {
            jsonResponse.put("error", this.error.toJson());
        }

        if (this.data != null) {
            jsonResponse.put("data", this.data);
        }

        return jsonResponse;
    }

    @Getter
    public static class Error {

        private String message;

        public Error(final String message) {
            this.message = message;
        }

        public JsonObject toJson() {
            final JsonObject errorJson = new JsonObject();
            errorJson.put("message", message);

            return errorJson;
        }
    }

}
