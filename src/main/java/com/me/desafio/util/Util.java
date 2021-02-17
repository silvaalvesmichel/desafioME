package com.me.desafio.util;

import com.me.desafio.dto.PedidoStatusDTO;
import com.me.desafio.enuns.StatusEnum;

public class Util {

	public static boolean isStatusAprovado(PedidoStatusDTO pedido) {
		return pedido.getStatus().equals(StatusEnum.APROVADO.getStatus());
	}
}
