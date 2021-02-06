package com.me.desafio.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.me.desafio.domain.Item;


@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>{

}
