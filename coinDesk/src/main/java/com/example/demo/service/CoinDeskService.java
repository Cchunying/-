package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Dao.CoinDeskDao;
import com.example.demo.entity.CoinDesk;

@Service
public class CoinDeskService {

	@Autowired
    CoinDeskDao coinDeskDao;
	
	public CoinDeskService() {}
	
	public List<CoinDesk> getCoinDesks() {
        return (List<CoinDesk>) coinDeskDao.findAll();
    }

    public boolean createCoinDesk(CoinDesk coinDesk) {
    	try {
    		coinDeskDao.save(coinDesk);
    	}
    	catch(Exception e) {
    		return false;
    	}
        return true;
    }

    public CoinDesk updateCoinDesk(String currency, CoinDesk coinDesk) {
    	List<CoinDesk> coinDeskList = findByCurrency(currency);
    	CoinDesk newCoinDesk = new CoinDesk();
        if (coinDeskList.isEmpty()) {
        	return null;
        }else {
        	newCoinDesk = coinDeskList.get(0);
        	newCoinDesk.setCurrencyCName(coinDesk.getCurrencyCName());
        	coinDeskDao.save(newCoinDesk);
        }
        return coinDeskDao.findByCurrency(currency).isEmpty() ? null : coinDeskDao.findByCurrency(currency).get(0);
    }

    public List<CoinDesk> findByCurrency(String currency) {
    	List<CoinDesk> coinDeskList = coinDeskDao.findByCurrency(currency);
        return coinDeskList;
    }

    public boolean deleteCoinDesk(String currency) {
    	List<CoinDesk> coinDeskList = findByCurrency(currency);
        if (coinDeskList.isEmpty()) {
        	return false;
        }else {
        	coinDeskDao.deleteByCurreny(currency);
        }
        return true;
    }
}
