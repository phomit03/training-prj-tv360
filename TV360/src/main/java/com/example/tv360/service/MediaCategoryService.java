package com.example.tv360.service;

import com.example.tv360.dto.MediaCategoryDTO;
import com.example.tv360.entity.MediaCategory;
import com.example.tv360.repository.MediaCategoryRepository;
import com.example.tv360.utils.ModelToDtoConverter;
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
public class MediaCategoryService {
    private final MediaCategoryRepository mediaCategoryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    @Autowired
    public MediaCategoryService(MediaCategoryRepository mediaCategoryRepository, ModelToDtoConverter modelToDtoConverter ) {
        this.mediaCategoryRepository = mediaCategoryRepository;
        this.modelToDtoConverter = modelToDtoConverter;
    }

    public List<MediaCategoryDTO> getAllMediaCategories(){
        List<MediaCategory> mediaCategories = mediaCategoryRepository.findAll();
        return mediaCategories.stream().map(mc -> modelToDtoConverter.convertToDto(mc, MediaCategoryDTO.class)).collect(Collectors.toList());
    }

    public MediaCategoryDTO getMediaCategoryById(Long id) {
        MediaCategory mediaCategory = mediaCategoryRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(mediaCategory, MediaCategoryDTO.class);
    }

    public MediaCategory createMediaCategory(MediaCategoryDTO mediaCategoryDTO) throws IOException{
        MediaCategory mediaCategory = new MediaCategory();
        mediaCategory.setMedia(mediaCategoryDTO.getMedia());
        mediaCategory.setCategory(mediaCategoryDTO.getCategory());
        mediaCategory.setStatus(1);
        return mediaCategoryRepository.save(mediaCategory);
    }

    public MediaCategory updateMediaCategory(MediaCategoryDTO mediaCategoryDTO){
        try {
            MediaCategory mediaCategory = mediaCategoryRepository.getById(mediaCategoryDTO.getId());
            mediaCategory.setMedia(mediaCategoryDTO.getMedia());
            mediaCategory.setCategory(mediaCategoryDTO.getCategory());
            mediaCategory.setStatus(mediaCategoryDTO.getStatus());
            mediaCategory.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return mediaCategoryRepository.save(mediaCategory);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void softDeleteMediaCategory(Long id){
        Optional<MediaCategory> optionalMCate = mediaCategoryRepository.findById(id);
        if (optionalMCate.isPresent()) {
            MediaCategory mediaCategory = optionalMCate.get();
            mediaCategory.setStatus(0);
            mediaCategoryRepository.save(mediaCategory);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }
}