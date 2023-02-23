package com.example.demo.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.demo.entity.CoinDesk;

public interface CoinDeskDao extends CrudRepository<CoinDesk, Integer> {

	@Query(value = "SELECT * FROM COINDESK WHERE CURRENCY = ?1", nativeQuery = true)
	List<CoinDesk> findByCurrency(String currency);

	@Query(value = "DELETE FROM COINDESK WHERE CURRENCY = ?1", nativeQuery = true)
	void deleteByCurreny(String currency);

}
