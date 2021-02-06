package com.me.desafio;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.me.desafio.domain.Item;
import com.me.desafio.domain.Pedido;
import com.me.desafio.enuns.StatusEnum;
import com.me.desafio.repositories.ItemRepository;
import com.me.desafio.repositories.PedidoRepository;

@SpringBootApplication
public class DesafioMeApplication implements CommandLineRunner{
	
	//automaticamente instanciado pelo spring
	@Autowired
	private PedidoRepository pedidoRepository;
	@Autowired
	private ItemRepository itemRepository;

	public static void main(String[] args) {
		SpringApplication.run(DesafioMeApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		Pedido pedido = new Pedido(null, null);
		pedidoRepository.save(pedido);
		List<Item> itens = new ArrayList<Item>();
		Item item1 = new Item("Item A", Double.valueOf("10"), 1, pedido);
		Item item2 = new Item("Item B", Double.valueOf("5"), 2, pedido);
		Item item3 = new Item("Item C", Double.valueOf("25"), 3, pedido);
		itens.add(item1);
		itens.add(item2);
		itens.add(item3);
		
		pedido.setItens(itens);
		pedidoRepository.save(pedido);
		itemRepository.saveAll(itens);
		
	}

}
