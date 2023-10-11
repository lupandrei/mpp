package start;

import com.controller.ServiceException;
import com.example.model.Race;
import org.springframework.web.client.RestClientException;
import restClient.raceClient;

import java.util.List;

public class StartRestClient {
    private final static raceClient racesClient=new raceClient();
    public static void main(String[] args) {
        //  RestTemplate restTemplate=new RestTemplate();
        Race raceT=new Race(800,"racetest");
        try{
            //  User result= restTemplate.postForObject("http://localhost:8080/chat/users%22,userT, User.class);

            //  System.out.println("Result received "+result);
      /*  System.out.println("Updating  user ..."+userT);
        userT.setName("New name 2");
        restTemplate.put("http://localhost:8080/chat/users/test124", userT);

*/
            // System.out.println(restTemplate.postForObject("http://localhost:8080/chat/users%22,userT, User.class));
            //System.out.println( restTemplate.postForObject("http://localhost:8080/chat/users%22,userT, User.class));
            show(()-> System.out.println(racesClient.create(raceT)));
            show(()->{
                Iterable<Race> res=racesClient.getAll();
                int id = 0;
                for(Race race:res){
                    //System.out.println(race.getRaceName()+": "+race.getEngineCapacity());
                    id=race.getID();
                }
                raceT.setRaceName("racetestmodified");
                int finalId = id;
                show(()-> System.out.println(racesClient.findById(finalId)));
                int finalId1 = id;
                show(()-> racesClient.update(raceT, finalId1));
                int finalId2 = id;
                show(()-> System.out.println(racesClient.findById(finalId2)));
                int finalId3 = id;
                show(()->racesClient.delete(finalId3));
            });

        }catch(RestClientException ex){
            System.out.println("Exception ... "+ex.getMessage());
        }
//        List<Race> res=racesClient.getAll();
//        Integer id = res.get(res.size()-1).getID();
//        System.out.println("ID ----- : "+id);


    }

    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
