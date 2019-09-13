package com.epam.aws.mentoring.controller.office;

import com.epam.aws.mentoring.domain.Office;
import com.epam.aws.mentoring.service.OfficeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
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
@RequestMapping(value = "/api/v1/offices")
@Api(tags = {"Office"})
public class OfficeController {

	private OfficeService officeService;

	@Autowired
	public OfficeController(OfficeService officeService) {
		this.officeService = officeService;
	}

	@GetMapping(params = {"country", "city"})
	@ApiOperation(value = "Retrieve specific office")
	public Office getOffice(
		@ApiParam(value = "country of office to retrieve", required = true) @RequestParam String country,
		@ApiParam(value = "city of office to retrieve", required = true) @RequestParam String city) {
		return officeService.getOffice(country, city);
	}

	@GetMapping(params = {"country", "startDate"})
	@ApiOperation(value = "Retrieve all offices open after specified date in the specified country")
	public List<Office> getOfficesOpenAfterDateInCountry(
		@ApiParam(value = "country of office", required = true) @RequestParam String country,
		@ApiParam(value = "start office opening date", required = true) @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate startDate) {
		return officeService.getOfficesOpenAfterDateInCountry(country, startDate);
	}

	@GetMapping(params = {"region", "address"})
	@ApiOperation(value = "Retrieve all offices with filtering by address in the specified region")
	public List<Office> getOfficesInRegionWithAddressFiltering(
		@ApiParam(value = "region of office", required = true) @RequestParam String region,
		@ApiParam(value = "address of office", required = true) @RequestParam String address) {
		return officeService.getOfficesInRegionWithAddressFiltering(region, address);
	}

	@PostMapping
	@ApiOperation(value = "Create an office")
	public void createLocation(@RequestBody Office office) {
		officeService.createOffice(office);
	}

	@CrossOrigin
	@PutMapping
	@ApiOperation(value = "Update specific office")
	public void updateOffice(
		@ApiParam(value = "country of office to update", required = true) @RequestParam String country,
		@ApiParam(value = "city of office to update", required = true) @RequestParam String city,
		@ApiParam(value = "office payload to update", required = true) @RequestBody Office office) {
		officeService.updateOffice(country, city, office);
	}

	@CrossOrigin
	@DeleteMapping
	@ApiOperation(value = "Delete specific office")
	public void deleteOffice(
		@ApiParam(value = "country of office to remove", required = true) @RequestParam String country,
		@ApiParam(value = "city of office to remove", required = true) @RequestParam String city) {
		officeService.deleteOffice(country, city);
	}
}
