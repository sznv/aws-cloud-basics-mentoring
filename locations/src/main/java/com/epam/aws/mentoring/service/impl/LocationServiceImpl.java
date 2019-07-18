package com.epam.aws.mentoring.service.impl;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import com.epam.aws.mentoring.exception.EntityNotFoundException;
import com.epam.aws.mentoring.service.LocationService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

	private static final Map<LocationId, Location> locationMap = new ConcurrentHashMap<>();

	static {
		LocationId id1 = new LocationId("40.661", "-73.944");
		Location location1 = new Location(id1, "United States", "New York", "New York City", "USD");
		locationMap.put(id1, location1);

		LocationId id2 = new LocationId("51.507222", "-0.1275");
		Location location2 = new Location(id2, "United Kingdom", "England", "London", "GBP");
		locationMap.put(id2, location2);

	}

	@Override
	public List<Location> getLocations() {
		return new ArrayList<>(locationMap.values());
	}

	@Override
	public Location getLocation(LocationId locationId) {
		return locationMap.computeIfAbsent(locationId, key -> {
			throw new EntityNotFoundException(String.format("%s not found", key));
		});
	}

	@Override
	public Location createLocation(Location location) {
		locationMap.put(location.getId(), location);

		return location;
	}

	@Override
	public Location updateLocation(LocationId locationId, Location location) {
		if (!locationMap.containsKey(locationId)) {
			throw new EntityNotFoundException(String.format("%s not found", locationId));
		}

		locationMap.put(locationId, location);

		return location;
	}

	@Override
	public void deleteLocation(LocationId locationId) {
		locationMap.remove(locationId);
	}
}
