package com.epam.aws.mentoring.service;

import com.epam.aws.mentoring.domain.Office;
import java.time.LocalDate;
import java.util.List;

public interface OfficeService {

	Office getOffice(String country, String city);

	void createOffice(Office location);

	void updateOffice(String country, String city, Office office);

	void deleteOffice(String country, String city);

	List<Office> getOfficesOpenAfterDateInCountry(String country, LocalDate startDate);

	List<Office> getOfficesInRegionWithAddressFiltering(String region, String address);
}
