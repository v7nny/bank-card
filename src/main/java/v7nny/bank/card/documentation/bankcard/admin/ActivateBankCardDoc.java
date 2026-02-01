package v7nny.bank.card.documentation.bankcard.admin;

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
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "400", description = "Card already expired",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))}),
        @ApiResponse(responseCode = "404", description = "Bank card not found",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))}),
        @ApiResponse(responseCode = "409", description = "Bank card status already set",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))}),})
public @interface ActivateBankCardDoc {}
