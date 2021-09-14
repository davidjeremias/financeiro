package br.com.deliverit.financeiro.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "conta_a_pagar")
public class ContaApagar implements Serializable {

    private static final long serialVersionUID = -5305426995443250674L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "valorOriginal")
    private BigDecimal valorOriginal;

    @Column(name = "dataVencimento")
    private LocalDate dataVencimento;

    @Column(name = "dataPagamento")
    private LocalDate dataPagamento;
}
