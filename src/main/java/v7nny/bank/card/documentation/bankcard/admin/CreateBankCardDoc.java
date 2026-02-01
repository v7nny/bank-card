package v7nny.bank.card.documentation.bankcard.admin;


import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import v7nny.bank.card.entity.BankCard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(responseCode = "201",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(implementation = BankCard.class))}),
        @ApiResponse(responseCode = "404", description = "Username not found",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))}),
        @ApiResponse(responseCode = "500", description = "Bank card encryption exception",
                content = { @Content(mediaType = "application/json",
                        schema = @Schema(type = "object", properties = @StringToClassMapItem(key = "message", value = String.class)))}),})
public @interface CreateBankCardDoc {}