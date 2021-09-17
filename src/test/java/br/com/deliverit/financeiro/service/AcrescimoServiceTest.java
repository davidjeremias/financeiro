package br.com.deliverit.financeiro.service;

import br.com.deliverit.financeiro.dto.AcrescimoRequest;
import br.com.deliverit.financeiro.dto.AcrescimoResponse;
import br.com.deliverit.financeiro.entity.Acrescimo;
import br.com.deliverit.financeiro.exception.NegocioException;
import br.com.deliverit.financeiro.repository.AcrescimoRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AcrescimoServiceTest {

    @InjectMocks
    private AcrescimoService service;

    @Mock
    private AcrescimoRepository repository;

    @Mock
    private static ModelMapper mapper;

    private static Acrescimo acrescimo;
    private static AcrescimoResponse acrescimoResponse;

    @BeforeAll
    public static void setup() {
        acrescimo = Acrescimo.builder()
                .id(1L)
                .nome("ate 3 dias")
                .multa(2.0)
                .jurosDia(0.1).build();
        acrescimoResponse = AcrescimoResponse.builder()
                .id(1L)
                .nome("ate 3 dias")
                .multa(2.0)
                .jurosDia(0.1).build();
    }

    @Test
    public void salvar() {
        //cenário
        AcrescimoRequest acrescimoRequest = AcrescimoRequest.builder()
                .nome("ate 3 dias")
                .multa(2.0)
                .jurosDia(0.1).build();
        when(mapper.map(acrescimoRequest, Acrescimo.class)).thenReturn(acrescimo);
        when(repository.save(acrescimo)).thenReturn(acrescimo);
        when(mapper.map(acrescimo, AcrescimoResponse.class)).thenReturn(acrescimoResponse);

        //ação
        Optional<AcrescimoResponse> response = service.salvar(acrescimoRequest);

        //verificação
        assertNotNull(response.get());
    }

    @Test
    public void deveLancarExcecaoAoNaoLocalizarAcrescimo() throws NegocioException {
        //cenario
        String regra = "regra";

        //ação
        NegocioException exception = assertThrows(NegocioException.class, () -> {
            service.findByNome(regra);
        });

        //verificação
        assertEquals("Regra de acrescimo não localizada", exception.getMessage());
    }

    @Test
    public void deveBuscarRegraDeAcrescimo() throws NegocioException {
        //cenario
        when(repository.findByNome(any(String.class))).thenReturn(acrescimo);
        when(mapper.map(acrescimo, AcrescimoResponse.class)).thenReturn(acrescimoResponse);

        //ação
        Optional<AcrescimoResponse> response = service.findByNome(acrescimo.getNome());

        //verificação
        assertNotNull(response.get());
    }
}
