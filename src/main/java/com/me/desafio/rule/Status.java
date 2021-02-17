package com.me.desafio.rule;

import java.util.List;

import com.me.desafio.dto.PedidoStatusRespostaDTO;

public class Status {

	private List<RegraStatus> regras;

	public Status(List<RegraStatus> regras) {
		this.setRegras(regras);
	}

	public List<RegraStatus> getRegras() {
		return regras;
	}

	public void setRegras(List<RegraStatus> regras) {
		this.regras = regras;
	}

	public PedidoStatusRespostaDTO getStatus() {
		for (RegraStatus regra : regras) {
			PedidoStatusRespostaDTO retorno = regra.getStatus();
			if (!retorno.getStatus().isEmpty()) {
				return retorno;
			}
		}
		return null;
	}

}
