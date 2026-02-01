package v7nny.bank.card.documentation.blockcardrequest.user;

import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import v7nny.bank.card.entity.BlockBankCardRequest;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = BlockBankCardRequest.class))}),
        @ApiResponse(responseCode = "403", description = "Bank card access denied",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))}),
        @ApiResponse(responseCode = "404", description = "Bank card not found",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))})})
public @interface CreateBlockCardRequestDoc {}