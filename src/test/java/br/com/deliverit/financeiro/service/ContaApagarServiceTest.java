package br.com.deliverit.financeiro.service;

import br.com.deliverit.financeiro.dto.AcrescimoResponse;
import br.com.deliverit.financeiro.dto.ContaApagarRequest;
import br.com.deliverit.financeiro.dto.ContaApagarResponse;
import br.com.deliverit.financeiro.entity.Acrescimo;
import br.com.deliverit.financeiro.entity.ContaApagar;
import br.com.deliverit.financeiro.exception.NegocioException;
import br.com.deliverit.financeiro.repository.AcrescimoRepository;
import br.com.deliverit.financeiro.repository.ContaApagarRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ContaApagarServiceTest {

    @InjectMocks
    private ContaApagarService service;

    @Mock
    private ContaApagarRepository repository;

    @Mock
    private AcrescimoService acrescimoService;

    @Mock
    private AcrescimoRepository acrescimoRepository;

    @Mock
    private static ModelMapper mapper;

    private static ContaApagar contaApagar;
    private static ContaApagarResponse contaApagarResponse;
    private static  AcrescimoResponse acrescimoResponse;

    @BeforeAll
    public static void setup() {
        contaApagar = ContaApagar.builder()
                .id(1L)
                .nome("Conta de Luz")
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .valorOriginal(100.0).build();
        contaApagarResponse = ContaApagarResponse.builder()
                .id(1L)
                .nome("Conta de Luz")
                .valorOriginal(100.0)
                .valorCorrigido(100.0)
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now()).build();
        acrescimoResponse = AcrescimoResponse.builder()
                .id(1L)
                .nome("ate 3 dias")
                .multa(2.0)
                .jurosDia(0.1).build();
    }

    @Test
    public void save() throws NegocioException {
        //cenario
        ContaApagarRequest contaApagarRequest = ContaApagarRequest.builder()
                .nome("Conta de Luz")
                .dataVencimento(LocalDate.now())
                .dataPagamento(LocalDate.now())
                .valorOriginal(100.0).build();

        when(mapper.map(contaApagarRequest, ContaApagar.class)).thenReturn(contaApagar);
        when(repository.save(any(ContaApagar.class))).thenReturn(contaApagar);
        when(mapper.map(contaApagar, ContaApagarResponse.class)).thenReturn(contaApagarResponse);

        //ação
        Optional<ContaApagarResponse> response = service.salvar(contaApagarRequest);

        //verificação
        assertNotNull(response.get());
    }

    @Test
    public void findAllPageable() throws NegocioException {
        //cenario
        Pageable pageable = PageRequest.of(0, 10);
        List<ContaApagar> contaApagarList = Arrays.asList(contaApagar);
        Page<ContaApagar> contaApagarPage = new PageImpl<>(contaApagarList, pageable, 1);
        when(repository.findAll(pageable)).thenReturn(contaApagarPage);
        when(mapper.map(contaApagar, ContaApagarResponse.class)).thenReturn(contaApagarResponse);
        when(acrescimoService.findByNome("ate 3 dias")).thenReturn(Optional.ofNullable(acrescimoResponse));

        //ação
        Page<ContaApagarResponse> response = service.findAllPageable(pageable);

        //verificação
        assertEquals(response.getTotalElements(), 1);
    }

    @Test
    public void deveRetornarValorCorrigido() throws NegocioException {
        //cenário
        Long diasEmAtraso = 1L;
        Double valorOriginal = 100.0;
        Optional<AcrescimoResponse> acrescimo = Optional.of(acrescimoResponse);

        //ação
        double valorCorrigido = service.getValorCorrigido(diasEmAtraso, valorOriginal, acrescimo);

        //verificação
        assertEquals(valorCorrigido, 102.1);

    }

    @Test
    public void deveSetarDiasEmAtrasoIguaAZero() {
        //cenario
        LocalDate dataVencimento = LocalDate.now();

        //ação
        Long diasAtraso = service.setDiasEmAtraso(dataVencimento);

        //verificação
        assertEquals(diasAtraso, 0);
    }

    @Test
    public void deveSetarDiasEmAtrasoIguaAUm() {
        //cenario
        LocalDate dataVencimento = LocalDate.now().minusDays(1L);

        //ação
        Long diasAtraso = service.setDiasEmAtraso(dataVencimento);

        //verificação
        assertEquals(diasAtraso, 1);
    }

    @Test
    public void deveCalcularValorCorrigidoDeContaSemDiasDeAtraso() throws NegocioException {
        //cenario
        Long diasEmAtraso = 0l;
        Double valorOriginal = 100.0;

        //ação
        Double valorCorrigido = service.calculaValorCorrigido(diasEmAtraso, valorOriginal);

        //verificação
        assertEquals(valorCorrigido, 100.0);
    }

    @Test
    public void deveCalcularValorCorrigidoDeContaCom3DiasDeAtraso() throws NegocioException {
        //cenario
        Long diasEmAtraso = 3l;
        Double valorOriginal = 100.0;
        when(acrescimoService.findByNome("ate 3 dias")).thenReturn(Optional.ofNullable(acrescimoResponse));

        //ação
        Double valorCorrigido = service.calculaValorCorrigido(diasEmAtraso, valorOriginal);

        //verificação
        assertEquals(valorCorrigido, 102.3);
    }

    @Test
    public void deveCalcularValorCorrigidoDeContaCom5DiasDeAtraso() throws NegocioException {
        //cenario
        Long diasEmAtraso = 5l;
        Double valorOriginal = 100.0;
        AcrescimoResponse acrescimoResponse = AcrescimoResponse.builder()
                .id(1L)
                .nome("superior a 3 dias")
                .multa(3.0)
                .jurosDia(0.2).build();
        when(acrescimoService.findByNome("superior a 3 dias")).thenReturn(Optional.ofNullable(acrescimoResponse));

        //ação
        Double valorCorrigido = service.calculaValorCorrigido(diasEmAtraso, valorOriginal);

        //verificação
        assertEquals(valorCorrigido, 104.0);
    }

    @Test
    public void deveCalcularValorCorrigidoDeContaComMaisDe5DiasDeAtraso() throws NegocioException {
        //cenario
        Long diasEmAtraso = 6l;
        Double valorOriginal = 100.0;
        AcrescimoResponse acrescimoResponse = AcrescimoResponse.builder()
                .id(1L)
                .nome("superior a 5 dias")
                .multa(5.0)
                .jurosDia(0.3).build();
        when(acrescimoService.findByNome("superior a 5 dias")).thenReturn(Optional.ofNullable(acrescimoResponse));

        //ação
        Double valorCorrigido = service.calculaValorCorrigido(diasEmAtraso, valorOriginal);

        //verificação
        assertEquals(valorCorrigido, 106.8);
    }
}
