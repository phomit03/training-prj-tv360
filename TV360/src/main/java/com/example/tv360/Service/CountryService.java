package com.example.tv360.Service;

import com.example.tv360.DTO.CastDTO;
import com.example.tv360.DTO.CountryDTO;
import com.example.tv360.Entity.Cast;
import com.example.tv360.Entity.Category;
import com.example.tv360.Entity.Country;
import com.example.tv360.Repository.CastRepository;
import com.example.tv360.Repository.CountryRepository;
import com.example.tv360.Utils.DtoToModelConverter;
import com.example.tv360.Utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Country updateCountry(Long id,CountryDTO countryDTO){
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
            country.setStatus(0);
            countryRepository.save(country);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }
}
