package com.me.desafio.rule;

import java.util.ArrayList;
import java.util.List;

import com.me.desafio.dto.PedidoStatusDTO;
import com.me.desafio.dto.PedidoStatusRespostaDTO;
import com.me.desafio.enuns.StatusEnum;
import com.me.desafio.util.Util;

public class RegraStatusAprovadoQtdMenor implements RegraStatus {

	private PedidoStatusDTO pedidoStatus;
	private Double valorPedido;
	private Integer quantidadeItensPedido;
	private Integer pedido;

	public RegraStatusAprovadoQtdMenor(PedidoStatusDTO pedidoStatus, Double valorPedido, Integer quantidadeItensPedido,
			Integer pedido) {
		this.pedidoStatus = pedidoStatus;
		this.valorPedido = valorPedido;
		this.quantidadeItensPedido = quantidadeItensPedido;
		this.pedido = pedido;
	}

	@Override
	public PedidoStatusRespostaDTO getStatus() {
		List<String> status = new ArrayList<String>();
		PedidoStatusRespostaDTO retorno = new PedidoStatusRespostaDTO(null, pedido);
		if (pedidoStatus.getItensAprovados() < quantidadeItensPedido && Util.isStatusAprovado(pedidoStatus)) {
			status.add(StatusEnum.APROVADO_QTD_A_MENOR.getStatus());
			retorno.setStatus(status);

		}
		return retorno;
	}

}
