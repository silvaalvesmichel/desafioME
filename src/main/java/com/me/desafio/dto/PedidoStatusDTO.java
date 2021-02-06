package com.me.desafio.dto;

import java.io.Serializable;

public class PedidoStatusDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String status;
	private Integer itensAprovados;
	private Double valorAprovado;
	private Integer pedido;
	
	public PedidoStatusDTO() {
		
	}
	
	public PedidoStatusDTO(String status, Integer itensAprovados, Double valorAprovado, Integer pedido) {
		super();
		this.status = status;
		this.itensAprovados = itensAprovados;
		this.valorAprovado = valorAprovado;
		this.pedido = pedido;
	}
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getItensAprovados() {
		return itensAprovados;
	}
	public void setItensAprovados(Integer itensAprovados) {
		this.itensAprovados = itensAprovados;
	}
	public Double getValorAprovado() {
		return valorAprovado;
	}
	public void setValorAprovado(Double valorAprovado) {
		this.valorAprovado = valorAprovado;
	}
	public Integer getPedido() {
		return pedido;
	}
	public void setPedido(Integer pedido) {
		this.pedido = pedido;
	}
	
	
}
