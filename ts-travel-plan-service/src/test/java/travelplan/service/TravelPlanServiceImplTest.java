package travelplan.service;

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
import edu.fudan.common.entity.*;
import travelplan.entity.TransferTravelInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(JUnit4.class)
public class TravelPlanServiceImplTest {

    @InjectMocks
    private TravelPlanServiceImpl travelPlanServiceImpl;

    @Mock
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetTransferSearch() {
        TransferTravelInfo info = new TransferTravelInfo("from_station", "", "to_station", "", "G");

        //mock tripsFromHighSpeed() and tripsFromNormal()
        List<TripResponse> tripResponseList = new ArrayList<>();
        Response<List<TripResponse>> response1 = new Response<>(null, null, tripResponseList);
        ResponseEntity<Response<List<TripResponse>>> re1 = new ResponseEntity<>(response1, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re1);
        Response result = travelPlanServiceImpl.getTransferSearch(info, headers);
        Assert.assertEquals("Success.", result.getMsg());
    }

    @Test
    public void testGetCheapest() {
        TripInfo info = new TripInfo("start_station", "end_station", "");

        //response for getRoutePlanResultCheapest()
        RoutePlanResultUnit rpru = new RoutePlanResultUnit("trip_id", "type_id", "from_station", "to_station", new ArrayList<>(), "1.0", "2.0", "", "");
        ArrayList<RoutePlanResultUnit> routePlanResultUnits = new ArrayList<RoutePlanResultUnit>(){{ add(rpru); }};
        Response<ArrayList<RoutePlanResultUnit>> response1 = new Response<>(null, null, routePlanResultUnits);
        ResponseEntity<Response<ArrayList<RoutePlanResultUnit>>> re1 = new ResponseEntity<>(response1, HttpStatus.OK);

        //response for transferStationIdToStationName()
        List<String> list = new ArrayList<>();
        Response<List<String>> response2 = new Response<>(null, null, list);
        ResponseEntity<Response<List<String>>> re2 = new ResponseEntity<>(response2, HttpStatus.OK);

        //response for queryForStationId()
        Response<String> response3 = new Response<>(null, null, "");
        ResponseEntity<Response<String>> re3 = new ResponseEntity<>(response3, HttpStatus.OK);

        //response for getRestTicketNumber()
        Response<Integer> response4 = new Response<>(null, null, 0);
        ResponseEntity<Response<Integer>> re4 = new ResponseEntity<>(response4, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re1)
                .thenReturn(re2)
                .thenReturn(re3).thenReturn(re3).thenReturn(re4)
                .thenReturn(re3).thenReturn(re3).thenReturn(re4);

        TrainType trainType = new TrainType();
        Response response = new Response<>(1, "", trainType);
        ResponseEntity<Response> res = new ResponseEntity<>(response, HttpStatus.OK);
        HttpEntity requestEntity = new HttpEntity(null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-train-service/api/v1/trainservice/trains/byName/type_id",
                        HttpMethod.GET,
                        requestEntity,
                        Response.class))
                .thenReturn(res);

        Seat seatRequest = new Seat();
        seatRequest.setDestStation("from_station");
        seatRequest.setStartStation("to_station");
        seatRequest.setTrainNumber("trip_id");
        seatRequest.setTravelDate("");
        seatRequest.setSeatType(2);
        seatRequest.setStations(new ArrayList<>());
        seatRequest.setTotalNum(0);
        HttpEntity requestEntity2 = new HttpEntity(seatRequest, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity2,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        RoutePlanInfo routePlanInfo = new RoutePlanInfo();
        routePlanInfo.setNum(5);
        routePlanInfo.setStartStation("start_station");
        routePlanInfo.setEndStation("end_station");
        routePlanInfo.setTravelDate("");
        HttpEntity requestEntity3 = new HttpEntity(routePlanInfo, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-route-plan-service/api/v1/routeplanservice/routePlan/cheapestRoute",
                        HttpMethod.POST,
                        requestEntity3,
                        new ParameterizedTypeReference<Response<ArrayList<RoutePlanResultUnit>>>() {
                        }))
                .thenReturn(re1);

        HttpEntity requestEntity4 = new HttpEntity(seatRequest, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity4,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        Seat seatRequest2 = new Seat();
        seatRequest2.setDestStation("from_station");
        seatRequest2.setStartStation("to_station");
        seatRequest2.setTrainNumber("trip_id");
        seatRequest2.setTravelDate("");
        seatRequest2.setSeatType(3);
        seatRequest2.setStations(new ArrayList<>());
        seatRequest2.setTotalNum(0);
        HttpEntity requestEntity5 = new HttpEntity(seatRequest2, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity5,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        Response result = travelPlanServiceImpl.getCheapest(info, headers);
        Assert.assertEquals("Success", result.getMsg());
    }

    @Test
    public void testGetQuickest() {
        TripInfo info = new TripInfo("start_station", "end_station", "");

        //response for getRoutePlanResultQuickest()
        RoutePlanResultUnit rpru = new RoutePlanResultUnit("trip_id", "type_id", "from_station", "to_station", new ArrayList<>(), "1.0", "2.0", "", "");
        ArrayList<RoutePlanResultUnit> routePlanResultUnits = new ArrayList<RoutePlanResultUnit>(){{ add(rpru); }};
        Response<ArrayList<RoutePlanResultUnit>> response1 = new Response<>(null, null, routePlanResultUnits);
        ResponseEntity<Response<ArrayList<RoutePlanResultUnit>>> re1 = new ResponseEntity<>(response1, HttpStatus.OK);

        //response for transferStationIdToStationName()
        List<String> list = new ArrayList<>();
        Response<List<String>> response2 = new Response<>(null, null, list);
        ResponseEntity<Response<List<String>>> re2 = new ResponseEntity<>(response2, HttpStatus.OK);

        //response for queryForStationId()
        Response<String> response3 = new Response<>(null, null, "");
        ResponseEntity<Response<String>> re3 = new ResponseEntity<>(response3, HttpStatus.OK);

        //response for getRestTicketNumber()
        Response<Integer> response4 = new Response<>(null, null, 0);
        ResponseEntity<Response<Integer>> re4 = new ResponseEntity<>(response4, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re1)
                .thenReturn(re2)
                .thenReturn(re3).thenReturn(re3).thenReturn(re4)
                .thenReturn(re3).thenReturn(re3).thenReturn(re4);

        TrainType trainType = new TrainType();
        Response response = new Response<>(1, "", trainType);
        ResponseEntity<Response> res = new ResponseEntity<>(response, HttpStatus.OK);
        HttpEntity requestEntity = new HttpEntity(null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-train-service/api/v1/trainservice/trains/byName/type_id",
                        HttpMethod.GET,
                        requestEntity,
                        Response.class))
                .thenReturn(res);

        Seat seatRequest = new Seat();
        seatRequest.setDestStation("from_station");
        seatRequest.setStartStation("to_station");
        seatRequest.setTrainNumber("trip_id");
        seatRequest.setTravelDate("");
        seatRequest.setSeatType(2);
        seatRequest.setStations(new ArrayList<>());
        seatRequest.setTotalNum(0);
        HttpEntity requestEntity2 = new HttpEntity(seatRequest, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity2,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        RoutePlanInfo routePlanInfo = new RoutePlanInfo();
        routePlanInfo.setNum(5);
        routePlanInfo.setStartStation("start_station");
        routePlanInfo.setEndStation("end_station");
        routePlanInfo.setTravelDate("");
        HttpEntity requestEntity3 = new HttpEntity(routePlanInfo, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-route-plan-service/api/v1/routeplanservice/routePlan/quickestRoute",
                        HttpMethod.POST,
                        requestEntity3,
                        new ParameterizedTypeReference<Response<ArrayList<RoutePlanResultUnit>>>() {
                        }))
                .thenReturn(re1);

        HttpEntity requestEntity4 = new HttpEntity(seatRequest, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity4,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        Seat seatRequest2 = new Seat();
        seatRequest2.setDestStation("from_station");
        seatRequest2.setStartStation("to_station");
        seatRequest2.setTrainNumber("trip_id");
        seatRequest2.setTravelDate("");
        seatRequest2.setSeatType(3);
        seatRequest2.setStations(new ArrayList<>());
        seatRequest2.setTotalNum(0);
        HttpEntity requestEntity5 = new HttpEntity(seatRequest2, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity5,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        Response result = travelPlanServiceImpl.getQuickest(info, headers);
        Assert.assertEquals("Success", result.getMsg());
    }

    @Test
    public void testGetMinStation() {
        TripInfo info = new TripInfo("start_station", "end_station", "");

        //response for getRoutePlanResultMinStation()
        RoutePlanResultUnit rpru = new RoutePlanResultUnit("trip_id", "type_id", "from_station", "to_station", new ArrayList<>(), "1.0", "2.0", "", "");
        ArrayList<RoutePlanResultUnit> routePlanResultUnits = new ArrayList<RoutePlanResultUnit>(){{ add(rpru); }};
        Response<ArrayList<RoutePlanResultUnit>> response1 = new Response<>(null, null, routePlanResultUnits);
        ResponseEntity<Response<ArrayList<RoutePlanResultUnit>>> re1 = new ResponseEntity<>(response1, HttpStatus.OK);

        //response for transferStationIdToStationName()
        List<String> list = new ArrayList<>();
        Response<List<String>> response2 = new Response<>(null, null, list);
        ResponseEntity<Response<List<String>>> re2 = new ResponseEntity<>(response2, HttpStatus.OK);

        //response for queryForStationId()
        Response<String> response3 = new Response<>(null, null, "");
        ResponseEntity<Response<String>> re3 = new ResponseEntity<>(response3, HttpStatus.OK);

        //response for getRestTicketNumber()
        Response<Integer> response4 = new Response<>(null, null, 0);
        ResponseEntity<Response<Integer>> re4 = new ResponseEntity<>(response4, HttpStatus.OK);
        Mockito.when(restTemplate.exchange(
                Mockito.anyString(),
                Mockito.any(HttpMethod.class),
                Mockito.any(HttpEntity.class),
                Mockito.any(ParameterizedTypeReference.class)))
                .thenReturn(re1)
                .thenReturn(re2)
                .thenReturn(re3).thenReturn(re3).thenReturn(re4)
                .thenReturn(re3).thenReturn(re3).thenReturn(re4);

        TrainType trainType = new TrainType();
        Response response = new Response<>(1, "", trainType);
        ResponseEntity<Response> res = new ResponseEntity<>(response, HttpStatus.OK);
        HttpEntity requestEntity = new HttpEntity(null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-train-service/api/v1/trainservice/trains/byName/type_id",
                        HttpMethod.GET,
                        requestEntity,
                        Response.class))
                .thenReturn(res);

        Seat seatRequest = new Seat();
        seatRequest.setDestStation("from_station");
        seatRequest.setStartStation("to_station");
        seatRequest.setTrainNumber("trip_id");
        seatRequest.setTravelDate("");
        seatRequest.setSeatType(2);
        seatRequest.setStations(new ArrayList<>());
        seatRequest.setTotalNum(0);
        HttpEntity requestEntity2 = new HttpEntity(seatRequest, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity2,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        RoutePlanInfo routePlanInfo = new RoutePlanInfo();
        routePlanInfo.setNum(5);
        routePlanInfo.setStartStation("start_station");
        routePlanInfo.setEndStation("end_station");
        routePlanInfo.setTravelDate("");
        HttpEntity requestEntity3 = new HttpEntity(routePlanInfo, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-route-plan-service/api/v1/routeplanservice/routePlan/minStopStations",
                        HttpMethod.POST,
                        requestEntity3,
                        new ParameterizedTypeReference<Response<ArrayList<RoutePlanResultUnit>>>() {
                        }))
                .thenReturn(re1);

        HttpEntity requestEntity4 = new HttpEntity(seatRequest, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity4,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        Seat seatRequest2 = new Seat();
        seatRequest2.setDestStation("from_station");
        seatRequest2.setStartStation("to_station");
        seatRequest2.setTrainNumber("trip_id");
        seatRequest2.setTravelDate("");
        seatRequest2.setSeatType(3);
        seatRequest2.setStations(new ArrayList<>());
        seatRequest2.setTotalNum(0);
        HttpEntity requestEntity5 = new HttpEntity(seatRequest2, null);
        Mockito.when(restTemplate.exchange(
                        "http://ts-seat-service/api/v1/seatservice/seats/left_tickets",
                        HttpMethod.POST,
                        requestEntity5,
                        new ParameterizedTypeReference<Response<Integer>>() {
                        }))
                .thenReturn(re4);

        Response result = travelPlanServiceImpl.getMinStation(info, headers);
        Assert.assertEquals("Success", result.getMsg());
    }

}
