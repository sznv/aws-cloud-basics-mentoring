package com.epam.aws.mentoring.repository;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import java.util.Set;

public interface LocationRepository {

	Set<String> getLocationKeys();

	Location getLocation(String key);

	void createLocation(Location location);

	void updateLocation(LocationId id, Location location);

	void deleteLocation(LocationId id);
}
