package com.epam.aws.mentoring.service;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import java.util.List;

public interface LocationService {

	List<Location> getLocations();

	Location getLocation(LocationId id);

	void createLocation(Location location);

	void updateLocation(LocationId id, Location location);

	void deleteLocation(LocationId id);
}
