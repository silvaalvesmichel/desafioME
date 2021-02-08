package com.me.desafio.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.me.desafio.domain.Pedido;
import com.me.desafio.dto.PedidoStatusDTO;
import com.me.desafio.dto.PedidoStatusRespostaDTO;
import com.me.desafio.service.PedidoService;
import com.me.desafio.util.RespostaUtil;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api")
public class DesafioMERestService {

	@Autowired
	private PedidoService service;

	@RequestMapping(value = "/pedido/{pedido}", method = RequestMethod.GET)
	public ResponseEntity<?> ObterPedido(@PathVariable Integer pedido) {

		try {
			Pedido obj = service.find(pedido);

			return ResponseEntity.ok().body(obj);
		} catch (Exception e) {
			return new ResponseEntity<Object>(
					RespostaUtil.montarMensagem(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/pedido", method = RequestMethod.POST)
	public ResponseEntity<?> inserirPedido(@RequestBody Pedido pedido) {

		service.insert(pedido);

		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@RequestMapping(value = "/pedido", method = RequestMethod.PUT)
	public ResponseEntity<?> alterarPedido(@RequestBody Pedido pedido) {

		Pedido obj = service.update(pedido);

		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(value = "/pedido", method = RequestMethod.DELETE)
	public ResponseEntity<?> deletarPedido(@RequestBody Pedido pedido) {
		service.delete(pedido.getPedido());
		return ResponseEntity.accepted().build();
	}

	@RequestMapping(value = "/status", method = RequestMethod.POST)
	public ResponseEntity<?> obterStatusPedido(@RequestBody PedidoStatusDTO pedidoStatus) {

		PedidoStatusRespostaDTO obj = service.obterStatus(pedidoStatus);

		return ResponseEntity.ok().body(obj);

	}

}
