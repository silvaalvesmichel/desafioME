package com.me.desafio.dto;

import java.io.Serializable;
import java.util.List;

public class PedidoStatusRespostaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer pedido;
	private List<String> status;
	
	
	public PedidoStatusRespostaDTO() {
		
	}

	public PedidoStatusRespostaDTO(List<String> status, Integer pedido) {
		super();
		this.status = status;
		this.pedido = pedido;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public Integer getPedido() {
		return pedido;
	}

	public void setPedido(Integer pedido) {
		this.pedido = pedido;
	}
	
	
}
