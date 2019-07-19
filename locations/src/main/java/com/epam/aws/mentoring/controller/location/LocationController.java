package com.epam.aws.mentoring.controller.location;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import com.epam.aws.mentoring.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/locations")
@Api(tags = {"Locations"})
public class LocationController {

	private LocationService locationService;

	@Autowired
	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping
	@ApiOperation(value = "Retrieve all locations")
	public List<Location> getLocations() {
		return locationService.getLocations();
	}

	@GetMapping(params = {"latitude", "longitude"})
	@ApiOperation(value = "Retrieve specific location")
	public Location getLocation(
		@ApiParam(value = "latitude of location to retrieve", required = true) @RequestParam String latitude,
		@ApiParam(value = "longitude of location to retrieve", required = true) @RequestParam String longitude) {
		return locationService.getLocation(new LocationId(latitude, longitude));
	}

	@PostMapping
	@ApiOperation(value = "Create location")
	public void createLocation(@RequestBody Location location) {
		locationService.createLocation(location);
	}

	@CrossOrigin
	@PutMapping
	@ApiOperation(value = "Update specific location")
	public void updateLocation(
		@ApiParam(value = "latitude of location to update", required = true) @RequestParam String latitude,
		@ApiParam(value = "longitude of location to update", required = true) @RequestParam String longitude,
		@ApiParam(value = "location payload to update", required = true) @RequestBody Location location) {
		locationService.updateLocation(new LocationId(latitude, longitude), location);
	}

	@CrossOrigin
	@DeleteMapping
	@ApiOperation(value = "Delete specific location")
	public void deleteLocation(
		@ApiParam(value = "latitude of location to remove", required = true) @RequestParam String latitude,
		@ApiParam(value = "longitude of location to remove", required = true) @RequestParam String longitude) {
		locationService.deleteLocation(new LocationId(latitude, longitude));
	}
}
