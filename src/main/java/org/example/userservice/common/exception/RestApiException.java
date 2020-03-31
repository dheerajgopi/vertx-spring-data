package org.example.userservice.common.exception;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;

/**
 * Wrapper for exceptions with its corresponding HTTP status.
 */
@Getter
@Setter
public class RestApiException extends RuntimeException {

    /**
     * HTTP status.
     */
    private Integer httpStatus;

    /**
     * error message.
     */
    private String message;

    public RestApiException(final Throwable ex) {
        super(ex);
        this.message = ex.getMessage();

        if (ex instanceof ServiceException) {
            this.httpStatus = ((ServiceException) ex).getHttpStatus();
        } else {
            this.httpStatus = HttpResponseStatus.INTERNAL_SERVER_ERROR.code();
            this.message = "internal server error";
        }
    }

    public RestApiException(final JsonObject jsonObject) {
        this.httpStatus = jsonObject.getInteger("httpStatus");
        this.message = jsonObject.getString("message");
    }

    /**
     * Returns a <code>JsonObject</code> containing HTTP status and message.
     * @return {@link JsonObject}
     */
    public JsonObject toJson() {
        final JsonObject errorJson = new JsonObject();

        errorJson.put("httpStatus", this.httpStatus);
        errorJson.put("message", this.message);

        return errorJson;
    }
}
