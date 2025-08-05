package com.fiap.itmoura.tech_challenge_restaurant.presentation.contracts;

import com.fiap.itmoura.tech_challenge_restaurant.application.models.group.OnCreateGroup;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeRequest;
import com.fiap.itmoura.tech_challenge_restaurant.application.models.kitchentype.KitchenTypeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "Kitchen Types", description = "API para gerenciamento de tipos de cozinha")
public interface KitchenTypeControllerInterface {

    @Operation(
        summary = "Criar tipo de cozinha",
        description = "Cria um novo tipo de cozinha no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Tipo de cozinha criado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KitchenTypeResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Tipo de cozinha já existe",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<KitchenTypeResponse> createKitchenType(
        @Parameter(description = "Dados do tipo de cozinha a ser criado", required = true)
        @Validated(OnCreateGroup.class) @RequestBody KitchenTypeRequest request
    );

    @Operation(
        summary = "Listar tipos de cozinha",
        description = "Retorna todos os tipos de cozinha cadastrados no sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Lista de tipos de cozinha retornada com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KitchenTypeResponse.class)
            )
        )
    })
    ResponseEntity<List<KitchenTypeResponse>> getAllKitchenTypes();

    @Operation(
        summary = "Buscar tipo de cozinha por ID",
        description = "Retorna um tipo de cozinha específico baseado no ID fornecido"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de cozinha encontrado",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KitchenTypeResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de cozinha não encontrado",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<KitchenTypeResponse> getKitchenTypeById(
        @Parameter(description = "ID do tipo de cozinha", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String id
    );

    @Operation(
        summary = "Atualizar tipo de cozinha",
        description = "Atualiza um tipo de cozinha existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tipo de cozinha atualizado com sucesso",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = KitchenTypeResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos fornecidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de cozinha não encontrado",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Nome do tipo de cozinha já existe",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<KitchenTypeResponse> updateKitchenType(
        @Parameter(description = "ID do tipo de cozinha", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String id,
        @Parameter(description = "Dados atualizados do tipo de cozinha", required = true)
        @Validated(OnCreateGroup.class) @RequestBody KitchenTypeRequest request
    );

    @Operation(
        summary = "Excluir tipo de cozinha",
        description = "Remove um tipo de cozinha do sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Tipo de cozinha excluído com sucesso"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Tipo de cozinha não encontrado",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Tipo de cozinha está sendo usado por restaurantes",
            content = @Content(mediaType = "application/json")
        )
    })
    ResponseEntity<Void> deleteKitchenType(
        @Parameter(description = "ID do tipo de cozinha", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
        @PathVariable String id
    );
}
