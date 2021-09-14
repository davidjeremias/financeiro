package br.com.deliverit.financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaApagarRequest implements Serializable {

    private static final long serialVersionUID = 3971837210787356967L;
    @Size(max = 100)
    @NotBlank
    private String nome;

    @NotNull
    private Double valorOriginal;

    @NotNull
    private LocalDate dataVencimento;

    @NotNull
    private LocalDate dataPagamento;
}
