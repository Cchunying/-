package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.demo.entity.CoinDesk;
import com.example.demo.parser.CoinDeskInfo;
import com.example.demo.service.CoinDeskService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class CoinDeskController {

	@Autowired
	CoinDeskService coinDeskService;
	
	@RequestMapping("/coinDesks/hello")
	public String hello() {
		return "Hello world";
	}
	
	@GetMapping(value = "/coinDesks/getCoinDesk")
	public String getCoinDeskFromApi() {
		String url = "https://api.coindesk.com/v1/bpi/currentprice.json";
		RestTemplate rs = new RestTemplate();
		
		String jsonString = rs.getForObject(url, String.class);
		return jsonString;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@GetMapping(value = "/coinDesks/getNewCoinDesk")
	public ResponseEntity getNewCoinDeskApi() {
		String url = "http://localhost:8080/coinDesks/getCoinDesk";
		RestTemplate rs = new RestTemplate();
		
		String jsonString = rs.getForObject(url, String.class);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		CoinDeskInfo coinDeskResponse = new CoinDeskInfo();
		try {
			coinDeskResponse = mapper.readValue(jsonString, CoinDeskInfo.class);
		}
		catch(Exception e) {
			System.out.println(e);
		}
		
		String updateTime = "";
		String cur1 = "";
		String cur1Cname = "";
		String rate1 = "";
		String cur2 = "";
		String cur2Cname = "";
		String rate2 = "";
		String cur3 = "";
		String cur3Cname = "";
		String rate3 = "";
		
		if (coinDeskResponse != null) {
			if (coinDeskResponse.getTime() != null) {
				updateTime = coinDeskResponse.getTime().getUpdated();
			}
			if (coinDeskResponse.getBpi() != null) {
				RestTemplate rstmp = new RestTemplate();
				String urltmp = "http://localhost:8080/coinDesks/";
				
				if (coinDeskResponse.getBpi().getCurrency1() != null) {
					cur1 = coinDeskResponse.getBpi().getCurrency1().getCode();
					rate1 = coinDeskResponse.getBpi().getCurrency1().getRate();
					
					String url1 = urltmp + cur1;
					
					CoinDesk obj = rstmp.getForObject(url1, CoinDesk.class);
					if (obj != null) {
						cur1Cname = obj.getCurrencyCName();
					}
					
				}
				if (coinDeskResponse.getBpi().getCurrency2() != null) {
					cur2 = coinDeskResponse.getBpi().getCurrency2().getCode();
					rate2 = coinDeskResponse.getBpi().getCurrency2().getRate();
					
					String url2 = urltmp + cur2;
					
					CoinDesk obj = rstmp.getForObject(url2, CoinDesk.class);
					if (obj != null) {
						cur2Cname = obj.getCurrencyCName();
					}
				}
				if (coinDeskResponse.getBpi().getCurrency3() != null) {
					cur3 = coinDeskResponse.getBpi().getCurrency3().getCode();
					rate3 = coinDeskResponse.getBpi().getCurrency3().getRate();
					
					String url3 = urltmp + cur3;
					
					CoinDesk obj = rstmp.getForObject(url3, CoinDesk.class);
					if (obj != null) {
						cur3Cname = obj.getCurrencyCName();
					}
				}
			}
		}
		
		Map map = new HashMap();
		map.put("updateTime", updateTime);
		map.put("cur1_code", cur1);
		map.put("cur1_cName", cur1Cname);
		map.put("cur1_rate", rate1);
		map.put("cur2_code", cur2);
		map.put("cur2_cName", cur2Cname);
		map.put("cur2_rate", rate2);
		map.put("cur3_code", cur3);
		map.put("cur3_cName", cur3Cname);
		map.put("cur3_rate", rate3);
		JSONObject jsonObject = new JSONObject(map);

		
		return ResponseEntity.status(HttpStatus.OK).body(jsonObject.toString());
	}
	
	@SuppressWarnings("rawtypes")
	@GetMapping("/coinDesks")
    public ResponseEntity getCoinDesks() {
        Iterable<CoinDesk> coinDeskList = coinDeskService.getCoinDesks();
        return ResponseEntity.status(HttpStatus.OK).body(coinDeskList);
    }

    @GetMapping("/coinDesks/{currency}")
    public List<CoinDesk> getCoinDesks(@PathVariable String currency) {
        List<CoinDesk> coinDesk = coinDeskService.findByCurrency(currency);
        return coinDesk;
    }

    @SuppressWarnings("rawtypes")
	@PostMapping("/coinDesks")
    public ResponseEntity createCoinDesks(@RequestBody CoinDesk coinDesk) {
        boolean rs = coinDeskService.createCoinDesk(coinDesk);
        if (!rs) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("create 失敗");
        }
        return ResponseEntity.status(HttpStatus.OK).body("");
    }

    @SuppressWarnings("rawtypes")
	@PutMapping("/coinDesks/{currency}")
    public ResponseEntity upadteCoinDesks(@PathVariable String currency, @RequestBody CoinDesk coinDesk) {
    	CoinDesk newCoinDesk = coinDeskService.updateCoinDesk(currency, coinDesk);
        if (coinDesk == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can not find coinDesk by currency : " + currency);
        }
        return ResponseEntity.status(HttpStatus.OK).body(newCoinDesk.getJson());
    }

    @SuppressWarnings("rawtypes")
	@DeleteMapping("/coinDesks/{currency}")
    public ResponseEntity deleteCoinDesks(@PathVariable String currency) {
        Boolean rs = coinDeskService.deleteCoinDesk(currency);
        if (!rs) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("currency 不存在");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
    }
}
