package com.epam.aws.mentoring.service.impl;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import com.epam.aws.mentoring.exception.EntityNotFoundException;
import com.epam.aws.mentoring.repository.LocationRepository;
import com.epam.aws.mentoring.service.LocationService;
import com.epam.aws.mentoring.util.EntityKeyComposer;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {

	private LocationRepository locationRepository;
	private EntityKeyComposer keyComposer;

	@Autowired
	public LocationServiceImpl(LocationRepository locationRepository,
		EntityKeyComposer keyComposer) {
		this.locationRepository = locationRepository;
		this.keyComposer = keyComposer;
	}

	@Override
	public List<Location> getLocations() {
		return locationRepository.getLocationKeys().stream()
			.map(key -> locationRepository.getLocation(key)
				.orElseThrow(EntityNotFoundException::new))
			.collect(Collectors.toList());
	}

	@Override
	public Location getLocation(LocationId id) {
		String key = keyComposer.composeLocationKey(id);
		return locationRepository.getLocation(key).orElseThrow(EntityNotFoundException::new);
	}

	@Override
	public void createLocation(Location location) {
		locationRepository.createLocation(location);
	}

	@Override
	public void updateLocation(LocationId id, Location location) {
		String key = keyComposer.composeLocationKey(id);
		locationRepository.getLocation(key).orElseThrow(EntityNotFoundException::new);
		locationRepository.updateLocation(id, location);
	}

	@Override
	public void deleteLocation(LocationId id) {
		String key = keyComposer.composeLocationKey(id);
		locationRepository.getLocation(key).orElseThrow(EntityNotFoundException::new);
		locationRepository.deleteLocation(id);
	}
}
