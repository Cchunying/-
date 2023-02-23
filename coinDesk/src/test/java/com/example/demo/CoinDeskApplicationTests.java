package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestTemplate;

import com.example.demo.Dao.CoinDeskDao;
import com.example.demo.controller.CoinDeskController;
import com.example.demo.entity.CoinDesk;
import com.example.demo.service.CoinDeskService;

@SpringBootTest
@Sql(scripts = "classpath:test/data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CoinDeskApplicationTests {

	@Autowired
	CoinDeskService coinDeskService;
	
	@MockBean
	CoinDeskDao coinDeskDao;
	
	@Test
	@Order(1)
	void contextLoads() {
		
		String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
		RestTemplate rs = new RestTemplate();
		
		String object = rs.getForObject(url, String.class);
		System.out.print("contextLoads : " + object);
		
	}
	
	@Test
	@Order(2)
	public void testGetCoinDesks () {
	    
	    try {
	    	List<CoinDesk> coinDeskList = coinDeskService.getCoinDesks();
	    	for(CoinDesk coinDesk : coinDeskList) {
	    		System.out.println("coinDesk : " + coinDesk);
	    	}
	    }
	    catch(Exception e) {
	    	System.out.println("testGetCoinDesks exception : " + e);
	    }
	    
	    System.out.println("testGetCoinDesks success");
	 }
	
	@Test
	@Order(3)
	public void testFindByCurrency () {
	    // [Arrange] 預期資料
	    List<CoinDesk> expectedCoinDeskList = new ArrayList<CoinDesk>();
	    CoinDesk coinDesk = new CoinDesk();
	    coinDesk.setCurrency("USD");
	    coinDesk.setCurrencyCName("美金");
	    expectedCoinDeskList.add(coinDesk);

	    // [Act]操作CoinDeskService.findByCurrency(currency);
	    List<CoinDesk> actualCoinDeskList = new ArrayList<CoinDesk>();
	    try {
	    	actualCoinDeskList = coinDeskService.findByCurrency("USD");
	    	System.out.println("CoinDeskService.findByCurrency(currency) : " + actualCoinDeskList);
	    }
	    catch(Exception e) {
	    	System.out.println("testFindByCurrency exception : " + e);
	    }

	 }

	@Test
	@Order(4)
	public void testCreateCoinDesk () {
	    // [Arrange] 準備資料
		CoinDesk coinDesk = new CoinDesk();
		coinDesk.setCurrency("USD");
		coinDesk.setCurrencyCName("美金");

		CoinDeskService cs = new CoinDeskService();
		try {
			boolean isSuccess = cs.createCoinDesk(coinDesk);
			if (isSuccess) {
				System.out.println("testCreateCoinDesk createCoinDesk success");
			}else {
				System.out.println("testCreateCoinDesk createCoinDesk fail");
			}
		}
		catch(Exception e) {
			System.out.println("testCreateCoinDesk exception : " + e);
		}
	}
	
	@Test
	@Order(5)
	public void testUpdateCoinDesk () {
	    // 準備資料
		CoinDesk coinDesk = new CoinDesk();
		coinDesk.setCurrency("USD");
		coinDesk.setCurrencyCName("美金USD");
		
		CoinDeskService cs = new CoinDeskService();
		try {
			CoinDesk newCoinDesk = cs.updateCoinDesk("USD", coinDesk);
			System.out.println("testUpdateCoinDesk success : " + newCoinDesk.getJson());
			
		}
		catch(Exception e) {
			System.out.println("testUpdateCoinDesk exception : " + e);
		}
	}
	
	@Test
	@Order(6)
	public void testDeleteCoinDeskSuccess () {
		CoinDeskService cs = new CoinDeskService();
		try {
			boolean isSuccess = cs.deleteCoinDesk("USD");
			if (isSuccess) {
				System.out.println("testCreateCoinDesk createCoinDesk success");
			}else {
				System.out.println("testCreateCoinDesk createCoinDesk fail");
			}
		}
		catch(Exception e) {
			System.out.println("testCreateCoinDesk exception : " + e);
		}
	 }
	
	@Test
	@Order(7)
	public void testDeleteCoinDeskFail () {
		CoinDeskService cs = new CoinDeskService();
		try {
			boolean isSuccess = cs.deleteCoinDesk("ZUR");
			if (isSuccess) {
				System.out.println("testCreateCoinDesk createCoinDesk success");
			}else {
				System.out.println("testCreateCoinDesk createCoinDesk fail");
			}
		}
		catch(Exception e) {
			System.out.println("testCreateCoinDesk exception : " + e);
		}
	 }
	
	@Test
	@Order(8)
	public void testGetCoinDeskFromApi () {
		CoinDeskController controller = new CoinDeskController();
		String strFromApi = controller.getCoinDeskFromApi();
		System.out.println("String from api : " + strFromApi);
	 }
	
	@SuppressWarnings("rawtypes")
	@Test
	@Order(9)
	public void testGetNewCoinDeskApi () {
		CoinDeskController controller = new CoinDeskController();
		ResponseEntity entity = controller.getNewCoinDeskApi();
		System.out.println("String from new api : " + entity.getBody());
	 }
}
