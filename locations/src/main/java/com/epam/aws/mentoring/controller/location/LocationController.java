package com.epam.aws.mentoring.controller.location;

import com.epam.aws.mentoring.controller.documentation.DocumentationController;
import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import com.epam.aws.mentoring.service.LocationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping(value = "/api/v1")
@Api(tags = {"Locations"})
public class LocationController {

	private static Logger logger = LoggerFactory.getLogger(DocumentationController.class);

	private LocationService locationService;

	@Autowired
	public LocationController(LocationService locationService) {
		this.locationService = locationService;
	}

	@GetMapping(path = "/locations")
	@ApiOperation(value = "Retrieve all locations")
	public List<Location> getLocations() {
		return locationService.getLocations();
	}

	@GetMapping(path = "/location")
	@ApiOperation(value = "Retrieve specific location")
	public Location getLocation(
		@ApiParam(value = "latitude of location to retrieve", required = true) @RequestParam String latitude,
		@ApiParam(value = "longitude of location to retrieve", required = true) @RequestParam String longitude) {
		return locationService.getLocation(new LocationId(latitude, longitude));
	}

	@PostMapping(path = "/location/create")
	@ApiOperation(value = "Create location")
	public Location createLocation(@RequestBody Location location) {
		return locationService.createLocation(location);
	}

	@CrossOrigin
	@PutMapping(path = "/location/update")
	@ApiOperation(value = "Update specific location")
	public Location updateLocation(
		@ApiParam(value = "location payload to update", required = true) @RequestBody Location location) {
		return locationService.updateLocation(location);
	}

	@CrossOrigin
	@DeleteMapping(path = "/location/delete")
	@ApiOperation(value = "Delete specific location")
	public void deleteLocation(
		@ApiParam(value = "id of location to delete", required = true) @RequestBody LocationId locationId) {
		locationService.deleteLocation(locationId);
	}
}
