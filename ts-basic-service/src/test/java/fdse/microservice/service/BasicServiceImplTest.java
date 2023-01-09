package fdse.microservice.service;

import edu.fudan.common.entity.*;
import edu.fudan.common.util.Response;
import edu.fudan.common.util.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@RunWith(JUnit4.class)
public class BasicServiceImplTest {

    @InjectMocks
    private BasicServiceImpl basicServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();
    private HttpEntity requestEntity = new HttpEntity(headers);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testQueryForTravel() {
        Trip trip = new Trip();
        trip.setTripId(new TripId());
        trip.setRouteId("route_id");
        trip.setStartTime(StringUtils.Date2String(new Date()));
        trip.setEndTime(StringUtils.Date2String(new Date()));
        trip.setTrainTypeName("type1");
        Travel info = new Travel();
        info.setTrip(trip);
        info.setStartPlace("starting_place");
        info.setEndPlace("end_place");
        info.setDepartureTime(StringUtils.Date2String(new Date()));

        Response responseStn = new Response<>(1, null, null);
        ResponseEntity<Response> resStn = new ResponseEntity<>(responseStn, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/type1",
                HttpMethod.GET,
                requestEntity,
                Response.class)).thenReturn(resStn);

        HttpEntity requestEntityRoute = new HttpEntity(null);
        Route route = new Route();
        route.setStations(Arrays.asList("starting_place", "end_place"));
        Response responseRoute = new Response<>(1, null, route);
        ResponseEntity<Response> resRoute = new ResponseEntity<>(responseRoute, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-route-service/api/v1/routeservice/routes/route_id",
                HttpMethod.GET,
                requestEntityRoute,
                Response.class)).thenReturn(resRoute);

        HttpEntity requestEntityPrice = new HttpEntity(null);
        PriceConfig priceConfig = new PriceConfig();
        priceConfig.setBasicPriceRate(10);
        priceConfig.setFirstClassPriceRate(25);
        Response responsePrice = new Response<>(1, null, priceConfig);
        ResponseEntity<Response> resPrice = new ResponseEntity<>(responsePrice, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-price-service/api/v1/priceservice/prices/route_id/Train1",
                HttpMethod.GET,
                requestEntityPrice,
                Response.class)).thenReturn(resPrice);

        Response response = new Response<>(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/id/" + "starting_place",
                HttpMethod.GET,
                requestEntity,
                Response.class)).thenReturn(re);

        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/id/" + "end_place",
                HttpMethod.GET,
                requestEntity,
                Response.class)).thenReturn(re);

        Response result = basicServiceImpl.queryForTravel(info, headers);
        Assert.assertEquals("Train type doesn't exist", result.getMsg());
    }

    @Test
    public void testQueryForStationId() {
        Response response = new Response<>(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/id/" + "stationName",
                HttpMethod.GET,
                requestEntity,
                Response.class)).thenReturn(re);
        Response result = basicServiceImpl.queryForStationId("stationName", headers);
        Assert.assertEquals(new Response<>(1, null, null), result);
    }

    @Test
    public void testCheckStationExists() {
        Response response = new Response<>(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                "http://ts-station-service/api/v1/stationservice/stations/id/" + "stationName",
                HttpMethod.GET,
                requestEntity,
                Response.class)).thenReturn(re);
        Boolean result = basicServiceImpl.checkStationExists("stationName", headers);
        Assert.assertTrue(result);
    }

    @Test
    public void testQueryTrainType() {
        Response response = new Response<>(1, null, null);
        ResponseEntity<Response> re = new ResponseEntity<>(response, HttpStatus.OK);
        HttpEntity requestEntity = new HttpEntity(null);
        Mockito.when(restTemplate.exchange(
                "http://ts-train-service/api/v1/trainservice/trains/byName/trainTypeNameDefault",
                HttpMethod.GET,
                requestEntity,
                Response.class)).thenReturn(re);
        TrainType result = basicServiceImpl.queryTrainTypeByName("trainTypeNameDefault", headers);
        Assert.assertNull(result);
    }

}
