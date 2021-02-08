package com.me.desafio.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.me.desafio.domain.Item;
import com.me.desafio.domain.Pedido;
import com.me.desafio.dto.PedidoStatusDTO;
import com.me.desafio.dto.PedidoStatusRespostaDTO;
import com.me.desafio.enuns.StatusEnum;
import com.me.desafio.repositories.ItemRepository;
import com.me.desafio.repositories.PedidoRepository;
import com.me.desafio.service.exception.ObjectNotFoundException;
import com.me.desafio.util.Constantes;

@RunWith(SpringRunner.class)
public class PedidoServiceTest {

	private PedidoService service;
	@MockBean
	private PedidoRepository pedidoRepo;
	@MockBean
	private ItemRepository itemRepo;

	@Before
	public void setup() {
		service = new PedidoService(pedidoRepo, itemRepo);
	}

	@Test
	public void ObterPedidoTest() {

		Optional<Pedido> pedidoretorno = montarPedidoRetorno();
		when(pedidoRepo.findById(1)).thenReturn(pedidoretorno);

		Pedido pedido = service.find(1);
		assertNotNull(pedido.getPedido());
		assertNotNull(pedido.getItens());
	}

	@Test
	public void ObterPedidoObjectNotFoundExceptionTest() {

		when(pedidoRepo.findById(1)).thenReturn(Optional.empty());

		try {
			service.find(2);
		} catch (ObjectNotFoundException e) {
			assertEquals(Constantes.STATUS_CODIGO_PEDIDO_INVALIDO, e.getMessage());
		}
	}

	@Test
	public void insertTest() {
		Pedido pedidoretorno = montarPedido();
		when(pedidoRepo.save(pedidoretorno)).thenReturn(pedidoretorno);
		when(itemRepo.saveAll(pedidoretorno.getItens())).thenReturn(pedidoretorno.getItens());

		Pedido pedido = service.insert(pedidoretorno);
		assertNotNull(pedido);
	}

	@Test
	public void insertObjectNotFoundExceptionTest() {
		Pedido pedidoretorno = montarPedido();
		pedidoretorno.setItens(null);
		try {
			service.insert(pedidoretorno);
		} catch (ObjectNotFoundException e) {
			assertEquals(Constantes.INSIRA_ITENS_AO_SEU_PEDIDO, e.getMessage());
		}

	}

	@Test
	public void updateTest() {
		Pedido pedidoretorno = montarPedido();
		when(pedidoRepo.save(pedidoretorno)).thenReturn(pedidoretorno);
		when(itemRepo.saveAll(pedidoretorno.getItens())).thenReturn(pedidoretorno.getItens());

		Pedido pedido = service.update(pedidoretorno);
		assertNotNull(pedido);
	}

	@Test
	public void updateObjectNotFoundExceptionTest() {
		Pedido pedidoretorno = montarPedido();
		pedidoretorno.setItens(null);
		try {
			service.update(pedidoretorno);
		} catch (ObjectNotFoundException e) {
			assertEquals(Constantes.INSIRA_ITENS_AO_SEU_PEDIDO, e.getMessage());
		}
	}

	@Test
	public void deleteTest() {
		Pedido pedidoretorno = montarPedido();
		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());
		doNothing().when(pedidoRepo).delete(any(Pedido.class));
		doNothing().when(itemRepo).delete(any(Item.class));
		service.delete(pedidoretorno.getPedido());
		verify(pedidoRepo, times(1)).deleteById(anyInt());
		verify(itemRepo, times(1)).deleteAll(Matchers.anyListOf((Item.class)));
	}

	@Test
	public void deleteDataIntegrityViolationExceptionTest() {
		Pedido pedidoretorno = montarPedido();
		pedidoretorno.setItens(null);
		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());
		try {
			service.delete(pedidoretorno.getPedido());
		} catch (DataIntegrityViolationException e) {
			assertEquals(Constantes.NAO_E_POSSIVEL_EXCLUIR_UM_PEDIDO_QUE_POSSUI_ITENS, e.getMessage());
		}
	}

	@Test
	public void obterStatusObjectNotFoundExceptionTest() {

		when(pedidoRepo.findById(1)).thenReturn(Optional.empty());

		try {
			service.obterStatus(new PedidoStatusDTO(null, null, null, 1));
		} catch (ObjectNotFoundException e) {
			assertEquals(Constantes.STATUS_CODIGO_PEDIDO_INVALIDO, e.getMessage());
		}
	}

	@Test
	public void obterStatusREPROVADOTest() {

		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());

		PedidoStatusRespostaDTO pedido = service.obterStatus(montarPedidoStatusReprovado());
		assertEquals(pedido.getStatus().get(0), StatusEnum.REPROVADO.getStatus());
	}

	@Test
	public void obterStatusAPROVADOTest() {

		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());

		PedidoStatusRespostaDTO pedido = service.obterStatus(montarPedidoStatusAprovado());
		assertEquals(pedido.getStatus().get(0), StatusEnum.APROVADO.getStatus());
	}

	@Test
	public void obterStatusAPROVADO_VALOR_A_MENORTest() {

		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());

		PedidoStatusRespostaDTO pedido = service.obterStatus(montarPedidoStatusAprovadoValorMenor());
		assertEquals(pedido.getStatus().get(0), StatusEnum.APROVADO_VALOR_A_MENOR.getStatus());
	}
	
	@Test
	public void obterStatusAPROVADO_QTD_A_MENORTest() {

		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());

		PedidoStatusRespostaDTO pedido = service.obterStatus(montarPedidoStatusAprovadoQtdMenor());
		assertEquals(pedido.getStatus().get(0), StatusEnum.APROVADO_QTD_A_MENOR.getStatus());
	}
	
	@Test
	public void obterStatusAPROVADO_VALOR_A_MAIORTest() {

		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());

		PedidoStatusRespostaDTO pedido = service.obterStatus(montarPedidoStatusAprovadoValorMaior());
		assertEquals(pedido.getStatus().get(0), StatusEnum.APROVADO_VALOR_A_MAIOR.getStatus());
	}
	
	@Test
	public void obterStatusAPROVADO_QTD_A_MAIORTest() {

		when(pedidoRepo.findById(1)).thenReturn(montarPedidoRetorno());

		PedidoStatusRespostaDTO pedido = service.obterStatus(montarPedidoStatusAprovadoQtdMaior());
		assertEquals(pedido.getStatus().get(0), StatusEnum.APROVADO_QTD_A_MAIOR.getStatus());
	}

	private PedidoStatusDTO montarPedidoStatusAprovadoQtdMaior() {
		return new PedidoStatusDTO(StatusEnum.APROVADO.getStatus(), 10, Double.valueOf("95"), 1);
	}

	private PedidoStatusDTO montarPedidoStatusAprovadoValorMaior() {
		return new PedidoStatusDTO(StatusEnum.APROVADO.getStatus(), 6, Double.valueOf("100"), 1);
	}

	private PedidoStatusDTO montarPedidoStatusAprovadoQtdMenor() {
		return new PedidoStatusDTO(StatusEnum.APROVADO.getStatus(), 5, Double.valueOf("95"), 1);
	}

	private PedidoStatusDTO montarPedidoStatusAprovadoValorMenor() {
		return new PedidoStatusDTO(StatusEnum.APROVADO.getStatus(), 6, Double.valueOf("90"), 1);
	}

	private PedidoStatusDTO montarPedidoStatusAprovado() {
		return new PedidoStatusDTO(StatusEnum.APROVADO.getStatus(), 6, Double.valueOf("95"), 1);
	}

	private PedidoStatusDTO montarPedidoStatusReprovado() {
		return new PedidoStatusDTO(StatusEnum.REPROVADO.getStatus(), 1, 20.10, 1);
	}

	private Optional<Pedido> montarPedidoRetorno() {
		Pedido pedido = montarPedido();
		return Optional.ofNullable(pedido);
	}

	private Pedido montarPedido() {
		Pedido pedido = new Pedido(1, null);
		List<Item> itens = new ArrayList<Item>();
		Item item1 = new Item("Item A", Double.valueOf("10"), 1, pedido);
		Item item2 = new Item("Item B", Double.valueOf("5"), 2, pedido);
		Item item3 = new Item("Item C", Double.valueOf("25"), 3, pedido);
		itens.add(item1);
		itens.add(item2);
		itens.add(item3);
		pedido.setItens(itens);
		return pedido;
	}
}
