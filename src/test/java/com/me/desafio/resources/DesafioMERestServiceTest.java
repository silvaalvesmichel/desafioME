package com.me.desafio.resources;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.me.desafio.domain.Item;
import com.me.desafio.domain.Pedido;
import com.me.desafio.dto.PedidoStatusDTO;
import com.me.desafio.dto.PedidoStatusRespostaDTO;
import com.me.desafio.repositories.ItemRepository;
import com.me.desafio.repositories.PedidoRepository;
import com.me.desafio.service.PedidoService;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class DesafioMERestServiceTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private PedidoService pedidoService;

	@MockBean
	private PedidoRepository pedidoRepo;

	@MockBean
	private ItemRepository itemRepo;

	private JacksonTester<Pedido> dtoPedidoJacksonTester;

	private JacksonTester<PedidoStatusDTO> dtoPedidoStatusJacksonTester;

	@Before
	public void setup() {
		ObjectMapper objectMapper = new ObjectMapper();
		JacksonTester.initFields(this, objectMapper);

		/*
		 * Pedido pedido = new Pedido(null, null); pedidoRepo.save(pedido); List<Item>
		 * itens = new ArrayList<Item>(); Item item1 = new Item("Item A",
		 * Double.valueOf("10"), 1, pedido); Item item2 = new Item("Item B",
		 * Double.valueOf("5"), 2, pedido); Item item3 = new Item("Item C",
		 * Double.valueOf("25"), 3, pedido); itens.add(item1); itens.add(item2);
		 * itens.add(item3);
		 * 
		 * pedido.setItens(itens); pedidoRepo.save(pedido); itemRepo.saveAll(itens);
		 */
	}

	@Test
	public void ObterPedidoTest() throws Exception {
		when(pedidoService.find(1)).thenReturn(montarPedido());
		mockMvc.perform(get("/api/pedido/1")).andExpect(status().isOk());
		verify(pedidoService, times(1)).find(anyInt());
	}

	@Test
	public void inserirPedidoTest() throws Exception {
		Pedido pedidoretorno = montarPedido();
		when(pedidoService.insert(pedidoretorno)).thenReturn(pedidoretorno);
		mockMvc.perform(post("/api/pedido").contentType("application/json")
				.content(dtoPedidoJacksonTester.write(pedidoretorno).getJson())).andExpect(status().isCreated());

	}

	@Test
	public void alterarPedidoTest() throws Exception {
		Pedido pedidoretorno = montarPedido();
		mockMvc.perform(put("/api/pedido").contentType("application/json")
				.content(dtoPedidoJacksonTester.write(pedidoretorno).getJson())).andExpect(status().isOk());

	}

	@Test
	public void deletarPedidoTest() throws Exception {
		Pedido pedidoretorno = montarPedido();
		mockMvc.perform(delete("/api/pedido").contentType("application/json")
				.content(dtoPedidoJacksonTester.write(pedidoretorno).getJson())).andExpect(status().isAccepted());

	}

	@Test
	public void obterStatusPedidoTest() throws Exception {
		PedidoStatusDTO pedidoStatus = montarPedidoStatus(montarPedido());
		PedidoStatusRespostaDTO pedidoretorno = montarPedidoStatusRetorno();
		when(pedidoService.obterStatus(pedidoStatus)).thenReturn(pedidoretorno);
		mockMvc.perform(post("/api/status").contentType("application/json")
				.content(dtoPedidoStatusJacksonTester.write(pedidoStatus).getJson())).andExpect(status().isOk());

	}

	private PedidoStatusRespostaDTO montarPedidoStatusRetorno() {
		List<String> status = Arrays.asList("APROVADO");
		return new PedidoStatusRespostaDTO(status, 1);
	}

	private PedidoStatusDTO montarPedidoStatus(Pedido pedido) {
		return new PedidoStatusDTO("APROVADO", 10, Double.valueOf("150"), pedido.getPedido());
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

	private Optional<Pedido> montarPedidoRetorno() {
		Pedido pedido = montarPedido();
		return Optional.ofNullable(pedido);
	}
}
