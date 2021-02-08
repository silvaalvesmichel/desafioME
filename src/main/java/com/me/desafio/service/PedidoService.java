package com.me.desafio.service;

import java.util.ArrayList;
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

	public void delete(Integer id, List<Item> itens) {
		find(id);
		try {
			itemRepo.deleteAll(itens);
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException(Constantes.NAO_E_POSSIVEL_EXCLUIR_UM_PEDIDO_QUE_POSSUI_ITENS);
		}
	}

	public PedidoStatusRespostaDTO obterStatus(PedidoStatusDTO pedidoStatus) {
		Optional<Pedido> obj = repo.findById(pedidoStatus.getPedido());
		Pedido pedido = obj.orElse(null);
		if (pedido == null) {
			throw new ObjectNotFoundException(Constantes.STATUS_CODIGO_PEDIDO_INVALIDO);
		}

		Integer quantidadeItensPedido = 0;
		Double valorPedido = Double.valueOf(0);
		List<String> status = new ArrayList<String>();
		PedidoStatusRespostaDTO retorno = new PedidoStatusRespostaDTO(null, pedido.getPedido());

		for (Item item : pedido.getItens()) {
			valorPedido = valorPedido + (item.getPrecoUnitario() * item.getQtd());
			quantidadeItensPedido = quantidadeItensPedido + item.getQtd();
		}

		// verificando se o status é igual a reprovado
		if (pedidoStatus.getStatus().equals(StatusEnum.REPROVADO.getStatus())) {
			status.add(StatusEnum.REPROVADO.getStatus());
			retorno.setStatus(status);
			return retorno;
		}

		else if (pedidoStatus.getItensAprovados() == quantidadeItensPedido
				&& pedidoStatus.getValorAprovado().equals(valorPedido) && isStatusAprovado(pedidoStatus)) {

			status.add(StatusEnum.APROVADO.getStatus());
			retorno.setStatus(status);
			return retorno;
		}

		else if (pedidoStatus.getValorAprovado() < valorPedido && isStatusAprovado(pedidoStatus)) {
			status.add(StatusEnum.APROVADO_VALOR_A_MENOR.getStatus());
			retorno.setStatus(status);
			return retorno;
		}

		else if (pedidoStatus.getItensAprovados() < quantidadeItensPedido && isStatusAprovado(pedidoStatus)) {
			status.add(StatusEnum.APROVADO_QTD_A_MENOR.getStatus());
			retorno.setStatus(status);
			return retorno;
		}

		else if (pedidoStatus.getValorAprovado() > valorPedido && isStatusAprovado(pedidoStatus)) {
			status.add(StatusEnum.APROVADO_VALOR_A_MAIOR.getStatus());
			retorno.setStatus(status);
			return retorno;
		}

		else if (pedidoStatus.getItensAprovados() > quantidadeItensPedido && isStatusAprovado(pedidoStatus)) {
			status.add(StatusEnum.APROVADO_QTD_A_MAIOR.getStatus());
			retorno.setStatus(status);
			return retorno;
		}

		else {
			return retorno;
		}
	}

	private boolean isStatusAprovado(PedidoStatusDTO pedido) {
		return pedido.getStatus().equals(StatusEnum.APROVADO.getStatus());
	}

	public void setPedidoRepository(PedidoRepository repo) {
		this.repo = repo;
	}

	public void setItemRepository(ItemRepository repo) {
		this.itemRepo = repo;
	}

	

}
