package com.springer.nature.watermark.service;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.springer.nature.watermark.core.model.Book;
import com.springer.nature.watermark.core.model.Document;
import com.springer.nature.watermark.core.model.Topic;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:watermarkerTest-context.xml" })
public class WatermarkerTest {
    
    private String baseUrl =  "http://localhost:8080/services/watermark";
    
   /* private ResteasyClient resteasyClient;
    
    private String context = "http://localhost:8080/services/watermark";
    private String path = "/service/{serviceId}/transaction/{extId}/authorize";
    private String inquiryPath = "/service/{serviceId}/allowedValues/ddd/{ddd}";
    
    @Before
    public void setUp() throws Exception {
        this.resteasyClient = new ResteasyClientBuilder().build();
    }   
    
    private void sendAuthorize(String externalId, long serviceId, String amount, String msisdn, String domain) {
        ResteasyWebTarget target = resteasyClient.target(domain + context + path).resolveTemplate("serviceId", serviceId, true).resolveTemplate("extId", externalId, true);
        Form form = new Form().param("msisdn", msisdn).param("amount", amount);
        sendRestRequest(target, form);
    }
    
    private void sendRestRequest(ResteasyWebTarget target, Form form) {
        // tive que implementar ClientRequestFilter, set do header nao funcionou: target.request().header("x-forwarded-for", ip);
        resteasyClient.register(new AddHeaderRequestFilter("x-forwarded-for", ip));
        Response response = target.request().accept("application/json").put(Entity.form(form));
        response.close();
    }*/
    
    @Test
    public void teste() throws Exception{
	RestTemplate restTemplate = new RestTemplate();
	ResponseEntity<String> postBook = restTemplate.postForEntity(baseUrl + "/book", new Book("001","TTT","AAA", Topic.SCIENCE), String.class);
	
	String ticket = postBook.getBody();
	
	String status = "RUNNING";
	while("RUNNING".equalsIgnoreCase(status)){
	    TimeUnit.MILLISECONDS.sleep(500);
	    status = restTemplate.getForObject(baseUrl + "/status/" + ticket, String.class);	    
	}
	Document doc = restTemplate.getForObject(baseUrl + "/" + ticket, Document.class);
	System.out.println(doc.getId());
	
    }    

}
