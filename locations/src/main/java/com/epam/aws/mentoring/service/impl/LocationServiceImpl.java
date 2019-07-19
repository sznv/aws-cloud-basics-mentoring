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
			.map(key -> locationRepository.getLocation(key))
			.collect(Collectors.toList());
	}

	@Override
	public Location getLocation(LocationId id) {
		String key = keyComposer.composeLocationKey(id);
		return locationRepository.getLocation(key);
	}

	@Override
	public void createLocation(Location location) {
		locationRepository.createLocation(location);
	}

	@Override
	public void updateLocation(LocationId id, Location location) {
		checkIfLocationExists(id);
		locationRepository.updateLocation(id, location);
	}

	@Override
	public void deleteLocation(LocationId id) {
		checkIfLocationExists(id);
		locationRepository.deleteLocation(id);
	}

	private void checkIfLocationExists(LocationId id) {
		Location location = locationRepository.getLocation(keyComposer.composeLocationKey(id));

		if (location == null) {
			throw new EntityNotFoundException(
				String.format("Location associated with id=%s does not exist", id));
		}
	}
}
