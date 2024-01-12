package com.example.tv360.service;

import com.example.tv360.dto.CountryDTO;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.Country;
import com.example.tv360.repository.CountryRepository;
import com.example.tv360.service.exception.AssociationException;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;

    @Autowired
    public CountryService(CountryRepository countryRepository, ModelToDtoConverter modelToDtoConverter ,DtoToModelConverter dtoToModelConverter ) {
        this.countryRepository = countryRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;

    }

    public List<CountryDTO> getAllCountries(){
        List<Country> countries = countryRepository.findAll();
        return countries.stream().map(countries1 -> modelToDtoConverter.convertToDto(countries1, CountryDTO.class)).collect(Collectors.toList());
    }

    public CountryDTO getCountryById(Long id) {
        Country countries = countryRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(countries, CountryDTO.class);
    }

    public Country createCountry(CountryDTO countryDTO) throws IOException {
        Country country = dtoToModelConverter.convertToModel(countryDTO, Country.class);
        country.setStatus(1);
        return countryRepository.save(country);
    }

    public Country updateCountry(Long id, CountryDTO countryDTO){
        try {
            Country country = countryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
            Country updateCountry1 = dtoToModelConverter.convertToModel(countryDTO, Country.class);
            country.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            country = updateCountry1;
            return countryRepository.save(country);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void softDeleteCountry(Long id){
        Optional<Country> optionalCountry = countryRepository.findById(id);
        if (optionalCountry.isPresent()) {
            Country country = optionalCountry.get();

            if (isCountryUsedInMedia(country)) {
                throw new AssociationException("Cannot delete country as it is associated with media.");
            }

            country.setStatus(0);
            countryRepository.save(country);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }

    private boolean isCountryUsedInMedia(Country country) {
        int mediaCount = countryRepository.countMediaByCountryId(country.getId());
        return mediaCount > 0;
    }

    //phan trang
    public Page<Country> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.countryRepository.findAll(pageable);
    }
}
