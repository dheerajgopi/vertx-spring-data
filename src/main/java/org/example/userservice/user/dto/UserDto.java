package org.example.userservice.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.example.userservice.common.http.JsonResponse;
import org.example.userservice.common.serializers.DateTimeToEpochSerializer;
import org.example.userservice.entity.User;

import java.time.LocalDateTime;

/**
 * Container for user payload in REST API response.
 */
@Getter
@Setter
public class UserDto implements JsonResponse {

    private Long id;

    private String username;

    private String name;

    private Boolean isActive;

    @JsonSerialize(using = DateTimeToEpochSerializer.class)
    private LocalDateTime createdAt;

    @JsonSerialize(using = DateTimeToEpochSerializer.class)
    private LocalDateTime updatedAt;

    public UserDto(final User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.isActive = user.getIsActive();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }

    public JsonObject toJson() {
        return JsonObject.mapFrom(this);
    }

}
