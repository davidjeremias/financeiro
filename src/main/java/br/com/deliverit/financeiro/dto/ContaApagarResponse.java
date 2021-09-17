package br.com.deliverit.financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContaApagarResponse implements Serializable {

    private static final long serialVersionUID = 9065689554057553907L;
    private Long id;
    private String nome;
    private Double valorOriginal;
    private Double valorCorrigido;
    private LocalDate dataVencimento;
    private LocalDate dataPagamento;
    private Long diasAtraso;
}
