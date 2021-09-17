package br.com.deliverit.financeiro.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "acrescimo")
public class Acrescimo implements Serializable {

    private static final long serialVersionUID = -1172847896673123530L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "multa")
    private Double multa;

    @Column(name = "juros_dia")
    private Double jurosDia;
}
