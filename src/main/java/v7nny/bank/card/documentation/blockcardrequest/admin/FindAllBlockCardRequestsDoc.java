package v7nny.bank.card.documentation.blockcardrequest.admin;

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
        @ApiResponse(responseCode = "200",
                content = {@Content(mediaType = "application/json",
                        schema = @Schema(implementation = BlockBankCardRequest[].class))})})
public @interface FindAllBlockCardRequestsDoc {}