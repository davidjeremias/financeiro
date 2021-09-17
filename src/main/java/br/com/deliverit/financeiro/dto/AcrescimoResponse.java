package br.com.deliverit.financeiro.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcrescimoResponse implements Serializable {

    private static final long serialVersionUID = 5804727083782095350L;

    private Long id;

    private String nome;

    private Double multa;

    private Double jurosDia;
}
