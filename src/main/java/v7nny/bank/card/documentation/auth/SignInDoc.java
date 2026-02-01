package v7nny.bank.card.documentation.auth;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "200",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "token", value = String.class)))}),
        @ApiResponse(responseCode = "401", description = "Bad credentials",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))})})
public @interface SignInDoc {}