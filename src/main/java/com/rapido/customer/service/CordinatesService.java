package com.rapido.customer.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.rapido.customer.dto.ResponseSelectDto;
import com.rapido.customer.dto.SelectRideDto;
import com.rapido.customer.entity.Cordinates;
import com.rapido.customer.repository.CordinatesRepository;

import tools.jackson.databind.JsonNode;

@Service
public class CordinatesService {
	@Autowired
	private CordinatesRepository cordinaterepository;
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private RedisService redisservice;
	public ResponseSelectDto selectrideservice(int id, SelectRideDto selectridedto) {
		double sourceLat = selectridedto.getSourceloc().getLatitude();
		double sourceLon = selectridedto.getSourceloc().getLongitude();
		
		
		Cordinates source=new Cordinates(sourceLat,sourceLon);
		Cordinates savedSource = cordinaterepository.save(source);
		int sourceid=savedSource.getId();
		
		double destinationLat = selectridedto.getDestinationloc().getLatitude();
		double destinationLon = selectridedto.getDestinationloc().getLongitude();
		Cordinates destination=new Cordinates(destinationLat,destinationLon);
		Cordinates savedDestination = cordinaterepository.save(destination);
		int destinationid=savedDestination.getId();
		
		String pickup = getAddress(sourceLat, sourceLon);
		String drop = getAddress(destinationLat, destinationLon);
		
		String token = "pk.ed0ebe59f7a6d7e4937a2d6c6c0e697e";
		String url = "https://us1.locationiq.com/v1/directions/driving/" + sourceLon + "," + sourceLat + ";"
				+ destinationLon + "," + destinationLat + "?key=" + token + "&overview=false";
		JsonNode response = restTemplate.getForObject(url, JsonNode.class);
		double distanceInMeters = response.get("routes").get(0).get("distance").asDouble();
		double distance = distanceInMeters / 1000.0;
		double bikeFare = distance * 10;
		double autoFare = distance * 20;
		double cabFare = distance * 30;
		Map<String, Double> fare = new HashMap<>();
		fare.put("bike", bikeFare);
		fare.put("auto", autoFare);
		fare.put("cab", cabFare);
		ResponseSelectDto result = new ResponseSelectDto();
		result.setSource(pickup);
		result.setDestination(drop);
		result.setDistance(distance);
		result.setFare(fare);
		
		redisservice.saveData(id, sourceLat, sourceLon, destinationLat, destinationLon, distance, fare,pickup,drop,sourceid,destinationid);

		return result;
	}
	private String getAddress(double latitude, double longitude) {

	    String token = "pk.ed0ebe59f7a6d7e4937a2d6c6c0e697e";

	    String url = "https://us1.locationiq.com/v1/reverse"
	            + "?key=" + token
	            + "&lat=" + latitude
	            + "&lon=" + longitude
	            + "&format=json"
	            + "&addressdetails=1";

	    JsonNode response = restTemplate.getForObject(url, JsonNode.class);

	    if (response == null || response.get("display_name") == null) {
	        throw new RuntimeException("Unable to find address for coordinates");
	    }

	    return response.get("display_name").asText();
	}
}
