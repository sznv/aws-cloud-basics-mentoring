package com.epam.aws.mentoring.service.impl;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import com.epam.aws.mentoring.repository.LocationRepository;
import com.epam.aws.mentoring.service.LocationService;
import com.epam.aws.mentoring.util.LocationComposer;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

	private final LocationRepository locationRepository;
	private final LocationComposer locationComposer;

	@Autowired
	public LocationServiceImpl(LocationRepository locationRepository,
		LocationComposer locationComposer) {
		this.locationRepository = locationRepository;
		this.locationComposer = locationComposer;
	}

	@Override
	public List<Location> getLocations() {
		return locationRepository.getLocationKeys().stream()
			.map(locationRepository::getLocation)
			.collect(Collectors.toList());
	}

	@Override
	public Location getLocation(LocationId id) {
		String key = locationComposer.composeLocationKey(id);
		return locationRepository.getLocation(key);
	}

	@Override
	public void createLocation(Location location) {
		String key = locationComposer.composeLocationKey(location.getId());
		locationRepository.saveLocation(key, location);
	}

	@Override
	public void updateLocation(LocationId id, Location location) {
		String key = locationComposer.composeLocationKey(id);
		locationRepository.getLocation(key);
		locationRepository.saveLocation(key, location);
	}

	@Override
	public void deleteLocation(LocationId id) {
		String key = locationComposer.composeLocationKey(id);
		locationRepository.getLocation(key);
		locationRepository.deleteLocation(key);
	}
}
