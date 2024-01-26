package com.example.tv360.service;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.dto.CountryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Country;
import com.example.tv360.entity.Media;
import com.example.tv360.repository.CountryRepository;
import com.example.tv360.service.exception.AssociationException;
import com.example.tv360.service.redis.RedisCacheService;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final RedisCacheService redisCacheService;
    private final CacheConfig cacheConfig;

    @Autowired
    public CountryService(CountryRepository countryRepository, ModelToDtoConverter modelToDtoConverter , DtoToModelConverter dtoToModelConverter, RedisCacheService redisCacheService, CacheConfig cacheConfig) {
        this.countryRepository = countryRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;

        this.redisCacheService = redisCacheService;
        this.cacheConfig = cacheConfig;
    }

    public List<CountryDTO> getAllCountries(){
        List<Country> countries = countryRepository.findAll();
        return countries.stream().map(countries1 -> modelToDtoConverter.convertToDto(countries1, CountryDTO.class)).collect(Collectors.toList());
    }

    public CountryDTO getCountryById(Long id) {
        if (redisCacheService.checkExistsKey(cacheConfig.keyCountryPrefix+id)) {
            // data redis
            return (CountryDTO) redisCacheService.getValue(cacheConfig.keyCountryPrefix+id);
        } else {
            // data in database
            Country country = countryRepository.findById(id).orElse(null);
            // save data for cache
            if (country != null) {
                redisCacheService.setValue(cacheConfig.keyCountryPrefix+id, country);
            }
            return modelToDtoConverter.convertToDto(country, CountryDTO.class);
        }
    }

    public Country createCountry(CountryDTO countryDTO) throws IOException {
        Country country = dtoToModelConverter.convertToModel(countryDTO, Country.class);
        country.setStatus(1);
        return countryRepository.save(country);
    }

    public Country updateCountry(Long id, CountryDTO countryDTO){
        Country country = countryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        Country updateCountry1 = dtoToModelConverter.convertToModel(countryDTO, Country.class);
        country.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        country = updateCountry1;

        if (updateCountry1.getStatus() == 0 && isCountryUsedInMedia(updateCountry1)) {
            throw new IllegalStateException("Cannot update country status to in-active when it is associated with media.");
        }

        return countryRepository.save(country);
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

    public Page<MediaDTO> findPaginatedListMediaByCountry(int pageNo, int pageSize, List<MediaDTO> mediaList) {
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, mediaList.size());

        List<MediaDTO> pagedMediaList = mediaList.subList(startIndex, endIndex);
        return new PageImpl<>(pagedMediaList, PageRequest.of(pageNo - 1, pageSize), mediaList.size());
    }

    //get list Media by countryId (check media-details)
    public List<MediaDTO> getMediaByCountryId(Long countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new EntityNotFoundException("Country with id " + countryId + " not found."));

        List<MediaDTO> mediaList = country.getMedia().stream()
                .filter(media -> hasMediaDetail(media) && media.getStatus() == 1)
                .map(media -> modelToDtoConverter.convertToDto(media, MediaDTO.class))
                .collect(Collectors.toList());

        return mediaList;
    }

    //check media if any media-details
    private boolean hasMediaDetail(Media media) {
        return media.getMediaDetails() != null && !media.getMediaDetails().isEmpty();
    }
}
