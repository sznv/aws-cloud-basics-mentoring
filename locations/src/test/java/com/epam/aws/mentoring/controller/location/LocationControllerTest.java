package com.epam.aws.mentoring.controller.location;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.aws.mentoring.domain.Location;
import com.epam.aws.mentoring.domain.LocationId;
import com.epam.aws.mentoring.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(LocationController.class)
public class LocationControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private LocationService service;

	@Test
	public void getLocationsTest() throws Exception {
		LocationId locationId = new LocationId("40.661", "-73.944");
		Location location = new Location(locationId, "United States", "New York", "New York City",
			"USD");

		given(service.getLocations()).willReturn(Collections.singletonList(location));

		mvc.perform(get("/api/v1/locations")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$", hasSize(1)))
			.andExpect(jsonPath("$[0].city", is(location.getCity())));
	}

	@Test
	public void getLocationTest() throws Exception {
		LocationId locationId = new LocationId("40.661", "-73.944");
		Location location = new Location(locationId, "United States", "New York", "New York City",
			"USD");

		given(service.getLocation(locationId)).willReturn(location);

		mvc.perform(get("/api/v1/locations")
			.param("latitude", "40.661")
			.param("longitude", "-73.944")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.city", is(location.getCity())));
	}

	@Test
	public void createLocationTest() throws Exception {
		LocationId locationId = new LocationId("40.661", "-73.944");
		Location location = new Location(locationId, "United States", "New York", "New York City",
			"USD");

		given(service.createLocation(location)).willReturn(location);

		mvc.perform(post("/api/v1/locations")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(location)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.city", is(location.getCity())));
	}

	@Test
	public void updateLocationTest() throws Exception {
		LocationId locationId = new LocationId("40.661", "-73.944");
		Location location = new Location(locationId, "United States", "New York", "New York City",
			"USD");

		given(service.updateLocation(locationId, location)).willReturn(location);

		mvc.perform(put("/api/v1/locations")
			.param("latitude", "40.661")
			.param("longitude", "-73.944")
			.contentType(MediaType.APPLICATION_JSON)
			.content(new ObjectMapper().writeValueAsString(location)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.city", is(location.getCity())));
	}

	@Test
	public void deleteLocationTest() throws Exception {
		LocationId locationId = new LocationId("40.661", "-73.944");

		doNothing().when(service).deleteLocation(locationId);

		mvc.perform(delete("/api/v1/locations")
			.param("latitude", "40.661")
			.param("longitude", "-73.944")
			.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());
	}
}