package com.epam.aws.mentoring.repository;

import com.epam.aws.mentoring.domain.Office;
import java.time.LocalDate;
import java.util.List;

public interface OfficeRepository {

	Office getOffice(String country, String city);

	void saveOffice(Office office);

	void updateOffice(String country, String city, Office office);

	void deleteOffice(String country, String city);

	List<Office> getOfficesOpenAfterDateInCountry(String country, LocalDate startDate);

	List<Office> getOfficesInRegionWithAddressFiltering(String region, String address);
}
