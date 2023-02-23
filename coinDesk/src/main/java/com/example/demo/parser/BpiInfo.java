package com.example.demo.parser;

import com.fasterxml.jackson.annotation.JsonAlias;

public class BpiInfo {

	@JsonAlias("USD")
	private CurrencyInfo currency1;

	@JsonAlias("GBP")
	private CurrencyInfo currency2;
	
	@JsonAlias("EUR")
	private CurrencyInfo currency3;
	
	public CurrencyInfo getCurrency1() {
		return currency1;
	}
	public void setCurrency1(CurrencyInfo currency1) {
		this.currency1 = currency1;
	}
	
	public CurrencyInfo getCurrency2() {
		return currency2;
	}
	public void setCurrency2(CurrencyInfo currency2) {
		this.currency2 = currency2;
	}
	
	public CurrencyInfo getCurrency3() {
		return currency3;
	}
	public void setCurrency3(CurrencyInfo currency3) {
		this.currency3 = currency3;
	}
	
}
