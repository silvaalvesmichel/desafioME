package com.me.desafio.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.me.desafio.domain.Pedido;
import com.me.desafio.dto.PedidoStatusDTO;
import com.me.desafio.dto.PedidoStatusRespostaDTO;
import com.me.desafio.service.PedidoService;
import com.me.desafio.util.RespostaUtil;

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

		try {

			Pedido obj = service.insert(pedido);

			URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getPedido())
					.toUri();

			return ResponseEntity.created(uri).build();
		} catch (Exception e) {
			return new ResponseEntity<Object>(
					RespostaUtil.montarMensagem(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/pedido", method = RequestMethod.PUT)
	public ResponseEntity<?> alterarPedido(@RequestBody Pedido pedido) {

		Pedido obj = service.update(pedido);

		return ResponseEntity.ok().body(obj);
	}

	@RequestMapping(value = "/pedido", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@RequestBody Pedido pedido) {
		service.delete(pedido.getPedido(), pedido.getItens());
		return ResponseEntity.accepted().build();
	}

	@RequestMapping(value = "/status", method = RequestMethod.POST)
	public ResponseEntity<?> obterStatusPedido(@RequestBody PedidoStatusDTO pedidoStatus) {

		try {

			PedidoStatusRespostaDTO obj = service.obterStatus(pedidoStatus);

			return ResponseEntity.ok().body(obj);

		} catch (Exception e) {
			return new ResponseEntity<Object>(
					RespostaUtil.montarMensagem(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), new HttpHeaders(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
