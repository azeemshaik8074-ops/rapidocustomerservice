package com.rapido.customer.service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.rapido.customer.dto.RiderBookingRequest;

import tools.jackson.databind.ObjectMapper;

@Service
public class RedisService {
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	public void saveData(int id, double sourceLat, double sourceLon, double destinationLat, double destinationLon,
			double distance, Map<String, Double> fare,String pickup,String drop,int sourceid,int destinationid) {
		String redisKey = "c_" + id;
		redisTemplate.opsForHash().put(redisKey,"sourceid", String.valueOf(sourceid));
		redisTemplate.opsForHash().put(redisKey,"destinationid", String.valueOf(destinationid));
		redisTemplate.opsForHash().put(redisKey, "sourceLat", String.valueOf(sourceLat));
		redisTemplate.opsForHash().put(redisKey, "sourceLon", String.valueOf(sourceLon));
		redisTemplate.opsForHash().put(redisKey, "destinationLat", String.valueOf(destinationLat));
		redisTemplate.opsForHash().put(redisKey, "destinationLon", String.valueOf(destinationLon));
		redisTemplate.opsForHash().put(redisKey, "distance", String.valueOf(distance));
		redisTemplate.opsForHash().put(redisKey, "pickup", pickup);
		redisTemplate.opsForHash().put(redisKey, "drop", drop);
		redisTemplate.opsForHash().put(redisKey, "bike", String.valueOf(fare.get("bike")));
		redisTemplate.opsForHash().put(redisKey, "auto", String.valueOf(fare.get("auto")));
		redisTemplate.opsForHash().put(redisKey, "cab", String.valueOf(fare.get("cab")));
		redisTemplate.expire(redisKey, Duration.ofDays(30));
	}

	public Map<Object, Object> getData(int id) {

		String redisKey = "c_" + id;

		return redisTemplate.opsForHash().entries(redisKey);
	}

	public void deleteData(int id) {

		String redisKey = "c_" + id;

		redisTemplate.delete(redisKey);
	}

	public List<String> findNearbyRiders(String vehicletype, double latitude, double longitude) {

		String key = vehicletype.toLowerCase();

		Point pickupPoint = new Point(longitude, latitude);

		Circle circle = new Circle(pickupPoint, new Distance(50, Metrics.KILOMETERS));

		GeoResults<RedisGeoCommands.GeoLocation<String>> results = redisTemplate.opsForGeo().radius(key, circle);

		List<String> riders = new ArrayList<>();

		if (results != null) {
			for (GeoResult<RedisGeoCommands.GeoLocation<String>> result : results) {
				riders.add(result.getContent().getName());
			}
		}

		return riders;
	}

	public void pushRideRequestToRider(String riderId, int bookingid, double fare, double distance) {

		try {

			RiderBookingRequest request = new RiderBookingRequest(bookingid, fare, distance);

			String json = objectMapper.writeValueAsString(request);

			String key = "rider:" + riderId + ":requests";

			redisTemplate.opsForList().rightPush(key, json);

			redisTemplate.expire(key, Duration.ofDays(30));

		} catch (Exception e) {

			throw new RuntimeException("Failed to push ride request to rider", e);
		}
	}
}
