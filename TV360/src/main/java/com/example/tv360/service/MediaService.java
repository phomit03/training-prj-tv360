package com.example.tv360.service;

import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Media;
import com.example.tv360.repository.CountryRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.Helper;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MediaService {

    private Helper helper;
    private final MediaRepository mediaRepository;
    private final CountryRepository countryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;

    @Autowired
    public MediaService(MediaRepository mediaRepository, ModelToDtoConverter modelToDtoConverter, Helper helper , CountryRepository countryRepository, DtoToModelConverter dtoToModelConverter ) {
        this.mediaRepository = mediaRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.helper = helper;
        this.countryRepository = countryRepository;
        this.dtoToModelConverter = dtoToModelConverter;
    }

    public List<MediaDTO> getAllMedias(){
        List<Media> media = mediaRepository.findAll();
        return media.stream().map(media1 -> modelToDtoConverter.convertToDto(media1, MediaDTO.class)).collect(Collectors.toList());
    }

    public MediaDTO getMediaById(Long id) {
        Media media = mediaRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(media, MediaDTO.class);
    }

    public Media createMedia(MediaDTO mediaDTO, MultipartFile logo) throws IOException {
        Media media = dtoToModelConverter.convertToModel(mediaDTO, Media.class);
        if (!logo.isEmpty()) {
            String thumbnail = helper.uploadImage(logo);
            media.setThumbnail(thumbnail);
        }
        media.setStatus(1);
        return mediaRepository.save(media);
    }


    public Media updateMedia(Long id,MediaDTO mediaDTO,MultipartFile logo){
        try {
            Media media = mediaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
            Media updateMedia1 = dtoToModelConverter.convertToModel(mediaDTO, Media.class);
            BeanUtils.copyProperties(updateMedia1, media);
            if (!logo.isEmpty()) {
                String thumbnail = helper.uploadImage(logo);
                media.setThumbnail(thumbnail);
            }
            media.setCountry(countryRepository.findById(media.getCountry().getId()).get());
            media.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return mediaRepository.save(media);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void softDeleteMedia(Long id){
        Optional<Media> optionalMedia = mediaRepository.findById(id);
        if (optionalMedia.isPresent()) {
            Media media = optionalMedia.get();
            media.setStatus(0);
            mediaRepository.save(media);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }

}
