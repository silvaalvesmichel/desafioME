package com.me.desafio.enuns;

public enum StatusEnum {

	REPROVADO(1, "REPROVADO"), APROVADO(2, "APROVADO"), APROVADO_VALOR_A_MENOR(3, "APROVADO_VALOR_A_MENOR"),
	APROVADO_QTD_A_MENOR(4, "APROVADO_QTD_A_MENOR"), APROVADO_VALOR_A_MAIOR(5, "APROVADO_VALOR_A_MAIOR"),
	APROVADO_QTD_A_MAIOR(6, "APROVADO_QTD_A_MAIOR");

	private final Integer codStatus;
	private final String status;

	StatusEnum(Integer codStatus, String status) {
		this.codStatus = codStatus;
		this.status = status;

	}

	public Integer getCodStatus() {
		return codStatus;
	}

	public String getStatus() {
		return status;
	}

}
