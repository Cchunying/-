package com.example.demo.entity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "COINDESK")
public class CoinDesk {
	@Id
	String currency;
	
	@Column
	String currencyCName;
    
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    
    public String getCurrencyCName() {
        return currencyCName;
    }

    public void setCurrencyCName(String currencyCName) {
        this.currencyCName = currencyCName;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public String getJson() {
    	Map map = new HashMap();
    	map.put("currency", currency);
    	map.put("currencyCName", currencyCName);
    	JSONObject jsonObject = new JSONObject(map);
    	return jsonObject.toString();
    }
}
