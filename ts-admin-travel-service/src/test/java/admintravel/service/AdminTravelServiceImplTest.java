package admintravel.service;

import edu.fudan.common.entity.AdminTrip;
import edu.fudan.common.entity.Route;
import edu.fudan.common.entity.TrainType;
import edu.fudan.common.entity.TravelInfo;
import edu.fudan.common.util.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4.class)
public class AdminTravelServiceImplTest {

    @InjectMocks
    private AdminTravelServiceImpl adminTravelServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity requestEntity = new HttpEntity(headers);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetAllTravels1() {
        Response<ArrayList<AdminTrip>> response = new Response<>(0, null, null);
        ResponseEntity<Response<ArrayList<AdminTrip>>> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel-service/api/v1/travelservice/admin_trip",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<ArrayList<AdminTrip>>>() {
                })).thenReturn(re);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel2-service/api/v1/travel2service/admin_trip",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<ArrayList<AdminTrip>>>() {
                })).thenReturn(re);
        Response result = adminTravelServiceImpl.getAllTravels(headers);
        Assert.assertEquals(new Response<>(0, null, new ArrayList<>()), result);
    }

    @Test
    public void testGetAllTravels2() {
        ArrayList<AdminTrip> adminTrips = new ArrayList<>();
        adminTrips.add(new AdminTrip());
        Response<ArrayList<AdminTrip>> response = new Response<>(1, null, adminTrips);
        ResponseEntity<Response<ArrayList<AdminTrip>>> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel-service/api/v1/travelservice/admin_trip",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<ArrayList<AdminTrip>>>() {
                })).thenReturn(re);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel2-service/api/v1/travel2service/admin_trip",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<Response<ArrayList<AdminTrip>>>() {
                })).thenReturn(re);
        Response result = adminTravelServiceImpl.getAllTravels(headers);
        Assert.assertNotNull(result);
    }

    @Test
    public void testAddTravel1() {
        TravelInfo request = new TravelInfo();
        request.setStartStationName("NYC");
        request.setTerminalStationName("CHI");
        request.setTrainTypeName("G");
        request.setRouteId("101");
        request.setTripId("G101");

        HttpEntity requestEntity = new HttpEntity(Arrays.asList("NYC", "CHI"), null);
        Response responseStn = new Response();
        responseStn.setStatus(1);
        Map<String, String> stationMap = new HashMap<>();
        stationMap.put("NYC", "1");
        stationMap.put("CHI", "3");
        responseStn.setData(stationMap);
        ResponseEntity<Response> resStn = new ResponseEntity<>(responseStn, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/idlist",
                HttpMethod.POST,
                requestEntity,
                Response.class)).thenReturn(resStn);

        HttpEntity requestEntityTrainType = new HttpEntity(null);
        TrainType trainType = new TrainType();
        trainType.setName("Train1");
        Response responseTrainType = new Response();
        responseStn.setStatus(1);
        responseTrainType.setData(trainType);
        ResponseEntity<Response> resTrainType = new ResponseEntity<>(responseTrainType, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/G",
                HttpMethod.GET,
                requestEntityTrainType,
                Response.class)).thenReturn(resTrainType);

        HttpEntity requestEntityRoute = new HttpEntity(null);
        Route route = new Route();
        route.setStations(Arrays.asList("NYC", "CHI"));
        Response responseRoute = new Response<>(1, null, route);
        ResponseEntity<Response> resRoute = new ResponseEntity<>(responseRoute, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-route-service/api/v1/routeservice/routes/101",
                HttpMethod.GET,
                requestEntityRoute,
                Response.class)).thenReturn(resRoute);

        HttpEntity requestEntity2 = new HttpEntity<>(request, headers);
        Response response = new Response<>(0, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel-service/api/v1/travelservice/trips",
                HttpMethod.POST,
                requestEntity2,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.addTravel(request, headers);
        Assert.assertEquals(new Response<>(0, "Admin add new travel failed", null), result);
    }

    @Test
    public void testAddTravel2() {
        TravelInfo request = new TravelInfo();
        request.setStartStationName("NYC");
        request.setTerminalStationName("CHI");
        request.setTrainTypeName("G");
        request.setRouteId("101");
        request.setTripId("G101");

        HttpEntity requestEntity = new HttpEntity(Arrays.asList("NYC", "CHI"), null);
        Response responseStn = new Response();
        responseStn.setStatus(1);
        Map<String, String> stationMap = new HashMap<>();
        stationMap.put("NYC", "1");
        stationMap.put("CHI", "3");
        responseStn.setData(stationMap);
        ResponseEntity<Response> resStn = new ResponseEntity<>(responseStn, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/idlist",
                HttpMethod.POST,
                requestEntity,
                Response.class)).thenReturn(resStn);

        HttpEntity requestEntityTrainType = new HttpEntity(null);
        TrainType trainType = new TrainType();
        trainType.setName("Train1");
        Response responseTrainType = new Response();
        responseStn.setStatus(1);
        responseTrainType.setData(trainType);
        ResponseEntity<Response> resTrainType = new ResponseEntity<>(responseTrainType, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/G",
                HttpMethod.GET,
                requestEntityTrainType,
                Response.class)).thenReturn(resTrainType);

        HttpEntity requestEntityRoute = new HttpEntity(null);
        Route route = new Route();
        route.setStations(Arrays.asList("NYC", "CHI"));
        Response responseRoute = new Response<>(1, null, route);
        ResponseEntity<Response> resRoute = new ResponseEntity<>(responseRoute, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-route-service/api/v1/routeservice/routes/101",
                HttpMethod.GET,
                requestEntityRoute,
                Response.class)).thenReturn(resRoute);

        HttpEntity<TravelInfo> requestEntity2 = new HttpEntity<>(request, headers);
        Response response = new Response<>(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel-service/api/v1/travelservice/trips",
                HttpMethod.POST,
                requestEntity2,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.addTravel(request, headers);
        Assert.assertEquals(new Response<>(1, "[Admin add new travel]", null), result);
    }

    @Test
    public void testAddTravel3() {
        TravelInfo request = new TravelInfo();
        request.setStartStationName("NYC");
        request.setTerminalStationName("CHI");
        request.setTrainTypeName("K");
        request.setRouteId("101");
        request.setTripId("K101");

        HttpEntity requestEntity = new HttpEntity(Arrays.asList("NYC", "CHI"), null);
        Response responseStn = new Response();
        responseStn.setStatus(1);
        Map<String, String> stationMap = new HashMap<>();
        stationMap.put("NYC", "1");
        stationMap.put("CHI", "3");
        responseStn.setData(stationMap);
        ResponseEntity<Response> resStn = new ResponseEntity<>(responseStn, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/idlist",
                HttpMethod.POST,
                requestEntity,
                Response.class)).thenReturn(resStn);

        HttpEntity requestEntityTrainType = new HttpEntity(null);
        TrainType trainType = new TrainType();
        trainType.setName("Train1");
        Response responseTrainType = new Response();
        responseStn.setStatus(1);
        responseTrainType.setData(trainType);
        ResponseEntity<Response> resTrainType = new ResponseEntity<>(responseTrainType, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/K",
                HttpMethod.GET,
                requestEntityTrainType,
                Response.class)).thenReturn(resTrainType);

        HttpEntity requestEntityRoute = new HttpEntity(null);
        Route route = new Route();
        route.setStations(Arrays.asList("NYC", "CHI"));
        Response responseRoute = new Response<>(1, null, route);
        ResponseEntity<Response> resRoute = new ResponseEntity<>(responseRoute, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-route-service/api/v1/routeservice/routes/101",
                HttpMethod.GET,
                requestEntityRoute,
                Response.class)).thenReturn(resRoute);


        HttpEntity<TravelInfo> requestEntity2 = new HttpEntity<>(request, headers);
        Response response = new Response<>(0, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel2-service/api/v1/travel2service/trips",
                HttpMethod.POST,
                requestEntity2,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.addTravel(request, headers);
        Assert.assertEquals(new Response<>(0, "Admin add new travel failed", null), result);
    }

    @Test
    public void testAddTravel4() {
        TravelInfo request = new TravelInfo();
        request.setStartStationName("NYC");
        request.setTerminalStationName("CHI");
        request.setTrainTypeName("K");
        request.setRouteId("101");
        request.setTripId("K101");

        HttpEntity requestEntity = new HttpEntity(Arrays.asList("NYC", "CHI"), null);
        Response responseStn = new Response();
        responseStn.setStatus(1);
        Map<String, String> stationMap = new HashMap<>();
        stationMap.put("NYC", "1");
        stationMap.put("CHI", "3");
        responseStn.setData(stationMap);
        ResponseEntity<Response> resStn = new ResponseEntity<>(responseStn, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/idlist",
                HttpMethod.POST,
                requestEntity,
                Response.class)).thenReturn(resStn);

        HttpEntity requestEntityTrainType = new HttpEntity(null);
        TrainType trainType = new TrainType();
        trainType.setName("Train1");
        Response responseTrainType = new Response();
        responseStn.setStatus(1);
        responseTrainType.setData(trainType);
        ResponseEntity<Response> resTrainType = new ResponseEntity<>(responseTrainType, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/K",
                HttpMethod.GET,
                requestEntityTrainType,
                Response.class)).thenReturn(resTrainType);

        HttpEntity requestEntityRoute = new HttpEntity(null);
        Route route = new Route();
        route.setStations(Arrays.asList("NYC", "CHI"));
        Response responseRoute = new Response<>(1, null, route);
        ResponseEntity<Response> resRoute = new ResponseEntity<>(responseRoute, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-route-service/api/v1/routeservice/routes/101",
                HttpMethod.GET,
                requestEntityRoute,
                Response.class)).thenReturn(resRoute);

        HttpEntity<TravelInfo> requestEntity2 = new HttpEntity<>(request, headers);
        Response response = new Response<>(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel2-service/api/v1/travel2service/trips",
                HttpMethod.POST,
                requestEntity2,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.addTravel(request, headers);
        Assert.assertEquals(new Response<>(1, "[Admin add new travel]", null), result);
    }


    @Test
    public void testUpdateTravel1() {
        TravelInfo request = new TravelInfo();
        request.setStartStationName("NYC");
        request.setTerminalStationName("CHI");
        request.setTrainTypeName("G");
        request.setRouteId("101");
        request.setTripId("G101");

        HttpEntity requestEntity = new HttpEntity(Arrays.asList("NYC", "CHI"), null);
        Response responseStn = new Response();
        responseStn.setStatus(1);
        Map<String, String> stationMap = new HashMap<>();
        stationMap.put("NYC", "1");
        stationMap.put("CHI", "3");
        responseStn.setData(stationMap);
        ResponseEntity<Response> resStn = new ResponseEntity<>(responseStn, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/idlist",
                HttpMethod.POST,
                requestEntity,
                Response.class)).thenReturn(resStn);

        HttpEntity requestEntityTrainType = new HttpEntity(null);
        TrainType trainType = new TrainType();
        trainType.setName("Train1");
        Response responseTrainType = new Response();
        responseStn.setStatus(1);
        responseTrainType.setData(trainType);
        ResponseEntity<Response> resTrainType = new ResponseEntity<>(responseTrainType, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/G",
                HttpMethod.GET,
                requestEntityTrainType,
                Response.class)).thenReturn(resTrainType);

        HttpEntity requestEntityRoute = new HttpEntity(null);
        Route route = new Route();
        route.setStations(Arrays.asList("NYC", "CHI"));
        Response responseRoute = new Response<>(1, null, route);
        ResponseEntity<Response> resRoute = new ResponseEntity<>(responseRoute, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-route-service/api/v1/routeservice/routes/101",
                HttpMethod.GET,
                requestEntityRoute,
                Response.class)).thenReturn(resRoute);

        HttpEntity<TravelInfo> requestEntity2 = new HttpEntity<>(request, headers);
        Response response = new Response(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel-service/api/v1/travelservice/trips",
                HttpMethod.PUT,
                requestEntity2,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.updateTravel(request, headers);
        Assert.assertEquals(new Response<>(1, null, null), result);
    }

    @Test
    public void testUpdateTravel2() {
        TravelInfo request = new TravelInfo();
        request.setStartStationName("NYC");
        request.setTerminalStationName("CHI");
        request.setTrainTypeName("K");
        request.setRouteId("101");
        request.setTripId("K101");

        HttpEntity requestEntity = new HttpEntity(Arrays.asList("NYC", "CHI"), null);
        Response responseStn = new Response();
        responseStn.setStatus(1);
        Map<String, String> stationMap = new HashMap<>();
        stationMap.put("NYC", "1");
        stationMap.put("CHI", "3");
        responseStn.setData(stationMap);
        ResponseEntity<Response> resStn = new ResponseEntity<>(responseStn, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/idlist",
                HttpMethod.POST,
                requestEntity,
                Response.class)).thenReturn(resStn);

        HttpEntity requestEntityTrainType = new HttpEntity(null);
        TrainType trainType = new TrainType();
        trainType.setName("Train1");
        Response responseTrainType = new Response();
        responseStn.setStatus(1);
        responseTrainType.setData(trainType);
        ResponseEntity<Response> resTrainType = new ResponseEntity<>(responseTrainType, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/K",
                HttpMethod.GET,
                requestEntityTrainType,
                Response.class)).thenReturn(resTrainType);

        HttpEntity requestEntityRoute = new HttpEntity(null);
        Route route = new Route();
        route.setStations(Arrays.asList("NYC", "CHI"));
        Response responseRoute = new Response<>(1, null, route);
        ResponseEntity<Response> resRoute = new ResponseEntity<>(responseRoute, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-route-service/api/v1/routeservice/routes/101",
                HttpMethod.GET,
                requestEntityRoute,
                Response.class)).thenReturn(resRoute);

        HttpEntity<TravelInfo> requestEntity2 = new HttpEntity<>(request, headers);
        Response response = new Response(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel2-service/api/v1/travel2service/trips",
                HttpMethod.PUT,
                requestEntity2,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.updateTravel(request, headers);
        Assert.assertEquals(new Response<>(1, null, null), result);
    }

    @Test
    public void testDeleteTravel1() {
        Response response = new Response(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel-service/api/v1/travelservice/trips/" + "GaoTie",
                HttpMethod.DELETE,
                requestEntity,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.deleteTravel("GaoTie", headers);
        Assert.assertEquals(new Response<>(1, null, null), result);
    }

    @Test
    public void testDeleteTravel2() {
        Response response = new Response(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-travel2-service/api/v1/travel2service/trips/" + "K1024",
                HttpMethod.DELETE,
                requestEntity,
                Response.class)).thenReturn(re);
        Response result = adminTravelServiceImpl.deleteTravel("K1024", headers);
        Assert.assertEquals(new Response<>(1, null, null), result);
    }

}
