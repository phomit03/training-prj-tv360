package com.example.tv360.Service;

import com.example.tv360.DTO.MediaDTO;
import com.example.tv360.Entity.Media;
import com.example.tv360.Repository.MediaRepository;
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
public class MediaService {
    private final MediaRepository mediaRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    @Autowired
    public MediaService(MediaRepository mediaRepository, ModelToDtoConverter modelToDtoConverter ) {
        this.mediaRepository = mediaRepository;
        this.modelToDtoConverter = modelToDtoConverter;
    }

    public List<MediaDTO> getAllMedias(){
        List<Media> media = mediaRepository.findAll();
        return media.stream().map(media1 -> modelToDtoConverter.convertToDto(media1, MediaDTO.class)).collect(Collectors.toList());
    }

    public MediaDTO getMediaById(Long id) {
        Media media = mediaRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(media, MediaDTO.class);
    }

    public Media createMedia(MediaDTO mediaDTO) throws IOException {
        Media media = new Media();
        media.setThumbnail(mediaDTO.getThumbnail());
        media.setTitle(mediaDTO.getTitle());
        media.setDescription(mediaDTO.getDescription());
        media.setType(mediaDTO.getType());
        media.setCountryId(mediaDTO.getCountryId());
        media.setStatus(1);

        return mediaRepository.save(media);
    }


    public Media updateMedia(MediaDTO mediaDTO){
        try {
            Media media = mediaRepository.getById(mediaDTO.getId());
            media.setThumbnail(mediaDTO.getThumbnail());
            media.setTitle(mediaDTO.getTitle());
            media.setDescription(mediaDTO.getDescription());
            media.setCountryId(mediaDTO.getCountryId());
            media.setType(mediaDTO.getType());
            media.setStatus(mediaDTO.getStatus());
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
