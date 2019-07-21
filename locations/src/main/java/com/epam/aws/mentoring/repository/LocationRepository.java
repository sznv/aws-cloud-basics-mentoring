package com.epam.aws.mentoring.repository;

import com.epam.aws.mentoring.domain.Location;
import java.util.Set;

public interface LocationRepository {

	Set<String> getLocationKeys();

	Location getLocation(String key);

	void saveLocation(String key, Location location);

	void deleteLocation(String key);
}
