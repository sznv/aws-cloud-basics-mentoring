package com.epam.aws.mentoring.service.impl;

import com.epam.aws.mentoring.domain.Office;
import com.epam.aws.mentoring.repository.OfficeRepository;
import com.epam.aws.mentoring.service.OfficeService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OfficeServiceImpl implements OfficeService {

	private OfficeRepository officeRepository;

	@Autowired
	public OfficeServiceImpl(OfficeRepository officeRepository) {
		this.officeRepository = officeRepository;
	}

	@Override
	public Office getOffice(String country, String city) {
		return officeRepository.getOffice(country, city);
	}

	@Override
	public void createOffice(Office office) {
		officeRepository.saveOffice(office);
	}

	@Override
	public void updateOffice(String country, String city, Office office) {
		officeRepository.getOffice(country, city);
		officeRepository.updateOffice(country, city, office);
	}

	@Override
	public void deleteOffice(String country, String city) {
		officeRepository.getOffice(country, city);
		officeRepository.deleteOffice(country, city);
	}

	@Override
	public List<Office> getOfficesOpenAfterDateInCountry(String country, LocalDate startDate) {
		return officeRepository.getOfficesOpenAfterDateInCountry(country, startDate);
	}

	@Override
	public List<Office> getOfficesInRegionWithAddressFiltering(String region, String address) {
		return officeRepository.getOfficesInRegionWithAddressFiltering(region, address);
	}
}
