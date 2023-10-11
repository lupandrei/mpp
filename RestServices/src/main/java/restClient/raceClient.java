package restClient;

import com.controller.ServiceException;
import com.example.model.Race;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

public class raceClient {
    public static final String URL = "http://localhost:8080/app/races";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    public Iterable<Race> getAll(){
        Race[] races = execute(() -> restTemplate.getForObject(URL, Race[].class));
      return Arrays.asList(races);

    }

    public Race findById(Integer id){
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Race.class));
    }
    public Race create(Race race){
        return execute(() -> restTemplate.postForObject(URL, race, Race.class));
    }

    public void update(Race race,Integer id){
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL,id), race);
            return null;
        });
    }
    public void delete(Integer id){
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }

}
