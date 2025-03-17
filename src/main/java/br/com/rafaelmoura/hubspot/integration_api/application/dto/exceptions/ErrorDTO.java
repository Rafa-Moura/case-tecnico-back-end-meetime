package br.com.rafaelmoura.hubspot.integration_api.application.dto.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(name = "ErrorDTO", description = "Objeto padrão para mapeamento de erros na aplicação")
public class ErrorDTO {

    @Schema(description = "Título do erro. Indica o motivo pelo qual o erro foi gerado", example = "MISSING_PARAMETER_EXCEPTION")
    private String title;
    @Schema(description = "Contém os dados do erro", example = "Parâmetro code não informado")
    private String message;
    @Schema(description = "Data e hora que o erro foi gerado", example = "2025-03-17T12:23:23.231323")
    private String requestDateTime;
}
