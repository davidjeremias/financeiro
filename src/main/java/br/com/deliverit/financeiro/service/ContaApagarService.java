package br.com.deliverit.financeiro.service;

import br.com.deliverit.financeiro.dto.AcrescimoResponse;
import br.com.deliverit.financeiro.dto.ContaApagarRequest;
import br.com.deliverit.financeiro.dto.ContaApagarResponse;
import br.com.deliverit.financeiro.entity.ContaApagar;
import br.com.deliverit.financeiro.exception.NegocioException;
import br.com.deliverit.financeiro.repository.ContaApagarRepository;
import br.com.deliverit.financeiro.util.PageableUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@Slf4j
public class ContaApagarService {

    private static final String ATE_3_DIAS = "ate 3 dias";
    private static final String SUPERIOR_A_3_DIAS = "superior a 3 dias";
    private static final String SUPERIOR_A_5_DIAS = "superior a 5 dias";

    @Autowired
    private ContaApagarRepository repository;

    @Autowired
    private AcrescimoService acrescimoService;

    @Autowired
    ModelMapper modelMapper;

    public Optional<ContaApagarResponse> salvar(ContaApagarRequest contaApagarRequest) throws NegocioException {
        ContaApagar contaApagar = modelMapper.map(contaApagarRequest, ContaApagar.class);
        repository.save(contaApagar);
        ContaApagarResponse contaApagarResponse = modelMapper.map(contaApagar, ContaApagarResponse.class);
        long diasEmAtraso = setDiasEmAtraso(contaApagarResponse.getDataVencimento());
        contaApagarResponse.setDiasAtraso(diasEmAtraso);
        contaApagarResponse.setValorCorrigido(calculaValorCorrigido(diasEmAtraso, contaApagarResponse.getValorOriginal()));
        return Optional.of(contaApagarResponse);
    }

    public Page<ContaApagarResponse> findAllPageable(Pageable pageable) throws NegocioException {
        Page<ContaApagar> response = repository.findAll(pageable);
        List<ContaApagarResponse> contaResponse = new ArrayList<>();
        for (ContaApagar contaApagar : response.getContent()) {
            ContaApagarResponse contaApagarResponse = modelMapper.map(contaApagar, ContaApagarResponse.class);
            contaApagarResponse.setDiasAtraso(setDiasEmAtraso(contaApagar.getDataVencimento()));
            contaApagarResponse.setValorCorrigido(calculaValorCorrigido(contaApagarResponse.getDiasAtraso(),contaApagarResponse.getValorOriginal()));
            contaResponse.add(contaApagarResponse);
        }
        return new PageImpl<ContaApagarResponse>(contaResponse, response.getPageable(), response.getTotalElements());
    }

    public Long setDiasEmAtraso(LocalDate dataVencimento) {
        LocalDate hoje = LocalDate.now();
        long diferencaEmDias = 0L;
        if (dataVencimento.isBefore(hoje))
            diferencaEmDias = ChronoUnit.DAYS.between(dataVencimento, hoje);
        return diferencaEmDias;
    }

    public Double calculaValorCorrigido(Long diasEmAtraso , Double valorOriginal) throws NegocioException {
        double valorCorrigido = 0;
        if (diasEmAtraso <= 0) {
            valorCorrigido = valorOriginal;
        } else if (diasEmAtraso > 0 && diasEmAtraso <= 3){
            log.info("Calculando valor corrigido de conta a pagar aplicando multa de 2% e juros ao dia de 0.1%");
            Optional<AcrescimoResponse> acrescimo = acrescimoService.findByNome(ATE_3_DIAS);
            valorCorrigido = getValorCorrigido(diasEmAtraso, valorOriginal, acrescimo);
        }
        else if (diasEmAtraso > 3 && diasEmAtraso <= 5){
            log.info("Calculando valor corrigido de conta a pagar aplicando multa de 3% e juros ao dia de 0.2%");
            Optional<AcrescimoResponse> acrescimo = acrescimoService.findByNome(SUPERIOR_A_3_DIAS);
            valorCorrigido = getValorCorrigido(diasEmAtraso, valorOriginal, acrescimo);
        }
        else if (diasEmAtraso > 5){
            log.info("Calculando valor corrigido de conta a pagar aplicando multa de 5% e juros ao dia de 0.3%");
            Optional<AcrescimoResponse> acrescimo = acrescimoService.findByNome(SUPERIOR_A_5_DIAS);
            valorCorrigido = getValorCorrigido(diasEmAtraso, valorOriginal, acrescimo);
        }
        return valorCorrigido;
    }

    public double getValorCorrigido(Long diasEmAtraso, Double valorOriginal, Optional<AcrescimoResponse> acrescimo) throws NegocioException {
        double valorCorrigido;
        double multa = acrescimo.get().getMulta() / 100.0;
        double juros = acrescimo.get().getJurosDia() / 100.0;
        double valorMulta = (multa * valorOriginal);
        double valorJuros = (juros * valorOriginal) * diasEmAtraso;
        valorCorrigido = valorOriginal + (valorMulta + valorJuros);
        return valorCorrigido;
    }
}
