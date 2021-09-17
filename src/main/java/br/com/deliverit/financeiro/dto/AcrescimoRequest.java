package br.com.deliverit.financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcrescimoRequest implements Serializable {

    private static final long serialVersionUID = -555947016197297432L;

    @NotBlank
    private String nome;

    @NotNull
    private Double multa;

    @NotNull
    private Double jurosDia;
}
