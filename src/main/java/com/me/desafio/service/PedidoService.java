package com.me.desafio.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.me.desafio.domain.Item;
import com.me.desafio.domain.Pedido;
import com.me.desafio.dto.PedidoStatusDTO;
import com.me.desafio.dto.PedidoStatusRespostaDTO;
import com.me.desafio.enuns.StatusEnum;
import com.me.desafio.repositories.ItemRepository;
import com.me.desafio.repositories.PedidoRepository;
import com.me.desafio.rule.RegraStatus;
import com.me.desafio.rule.RegraStatusAprovado;
import com.me.desafio.rule.RegraStatusAprovadoQtdMaior;
import com.me.desafio.rule.RegraStatusAprovadoQtdMenor;
import com.me.desafio.rule.RegraStatusAprovadoValorMaior;
import com.me.desafio.rule.RegraStatusAprovadoValorMenor;
import com.me.desafio.rule.RegraStatusReprovado;
import com.me.desafio.rule.Status;
import com.me.desafio.service.exception.DataIntegrityException;
import com.me.desafio.service.exception.ObjectNotFoundException;
import com.me.desafio.util.Constantes;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class PedidoService {

	private PedidoRepository repo;
	private ItemRepository itemRepo;

	public Pedido find(Integer pedido) {
		// objeto container que vai carregar o tipo Pedido
		Optional<Pedido> obj = repo.findById(pedido);
		if (obj.isEmpty()) {
			throw new ObjectNotFoundException(Constantes.STATUS_CODIGO_PEDIDO_INVALIDO);
		}
		// se objeto for encontrado retorna o objeto se não retorna null
		return obj.orElse(null);
	}

	public Pedido insert(Pedido obj) {
		if (obj.getItens() == null || obj.getItens().isEmpty()) {
			throw new ObjectNotFoundException(Constantes.INSIRA_ITENS_AO_SEU_PEDIDO);
		}
		obj.setPedido(null);
		salvarPedidoComItens(obj);

		return obj;
	}

	private void setarPedidoEmItens(Pedido obj) {
		for (Item item : obj.getItens()) {
			item.setPedido(obj);
		}
	}

	public Pedido update(Pedido obj) {
		if (obj.getItens() == null || obj.getItens().isEmpty()) {
			throw new ObjectNotFoundException(Constantes.INSIRA_ITENS_AO_SEU_PEDIDO);
		}
		salvarPedidoComItens(obj);
		return obj;
	}

	private void salvarPedidoComItens(Pedido obj) {
		repo.save(obj);
		setarPedidoEmItens(obj);
		itemRepo.saveAll(obj.getItens());
	}

	public void delete(Integer id) {
		Pedido pedido = find(id);

		try {
			itemRepo.deleteAll(pedido.getItens());
			repo.deleteById(pedido.getPedido());
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(Constantes.NAO_E_POSSIVEL_EXCLUIR_UM_PEDIDO_QUE_POSSUI_ITENS);
		}
	}

	public PedidoStatusRespostaDTO obterStatus(PedidoStatusDTO pedidoStatus) {
		Pedido pedido = repo.findById(pedidoStatus.getPedido())
				.orElseThrow(() -> new ObjectNotFoundException(Constantes.STATUS_CODIGO_PEDIDO_INVALIDO));

		Integer quantidadeItensPedido = 0;
		Double valorPedido = Double.valueOf(0);

		for (Item item : pedido.getItens()) {
			valorPedido = valorPedido + (item.getPrecoUnitario() * item.getQtd());
			quantidadeItensPedido = quantidadeItensPedido + item.getQtd();
		}

		RegraStatusAprovado regraAprovado = new RegraStatusAprovado(pedidoStatus, valorPedido, quantidadeItensPedido,
				pedido.getPedido());
		RegraStatusReprovado regraReprovado = new RegraStatusReprovado(pedidoStatus, valorPedido, quantidadeItensPedido,
				pedido.getPedido());
		RegraStatusAprovadoQtdMaior regraAprovadoQtdMaior = new RegraStatusAprovadoQtdMaior(pedidoStatus, valorPedido,
				quantidadeItensPedido, pedido.getPedido());
		RegraStatusAprovadoQtdMenor regraAprovadoQtdMenor = new RegraStatusAprovadoQtdMenor(pedidoStatus, valorPedido,
				quantidadeItensPedido, pedido.getPedido());
		RegraStatusAprovadoValorMaior regraAprovadoValorMaior = new RegraStatusAprovadoValorMaior(pedidoStatus,
				valorPedido, quantidadeItensPedido, pedido.getPedido());
		RegraStatusAprovadoValorMenor regraAprovadoValorMenor = new RegraStatusAprovadoValorMenor(pedidoStatus,
				valorPedido, quantidadeItensPedido, pedido.getPedido());
		
		List<RegraStatus> regras = Arrays.asList(regraAprovado, regraReprovado, regraAprovadoQtdMaior,
				regraAprovadoQtdMenor, regraAprovadoValorMaior, regraAprovadoValorMenor);

		Status statusRetorno = new Status(regras);

		return statusRetorno.getStatus();

	}

	public void setPedidoRepository(PedidoRepository repo) {
		this.repo = repo;
	}

	public void setItemRepository(ItemRepository repo) {
		this.itemRepo = repo;
	}

}
