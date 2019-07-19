package com.epam.aws.mentoring.service;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import java.util.List;

public interface LocationService {

	List<Location> getLocations();

	Location getLocation(LocationId locationId);

	Location createLocation(Location location);

	Location updateLocation(LocationId locationId, Location location);

	void deleteLocation(LocationId locationId);
}
