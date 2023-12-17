package com.example.tv360.Service;

import com.example.tv360.DTO.CastDTO;
import com.example.tv360.DTO.MediaCategoryDTO;
import com.example.tv360.DTO.MediaDetailDTO;
import com.example.tv360.Entity.Cast;
import com.example.tv360.Entity.Media;
import com.example.tv360.Entity.MediaCategory;
import com.example.tv360.Repository.CastRepository;
import com.example.tv360.Repository.MediaCategoryRepository;
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
public class MediaCategoryService {
    private final MediaCategoryRepository mediaCategoryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    @Autowired
    public MediaCategoryService(MediaCategoryRepository mediaCategoryRepository, ModelToDtoConverter modelToDtoConverter ) {
        this.mediaCategoryRepository = mediaCategoryRepository;
        this.modelToDtoConverter = modelToDtoConverter;
    }

    public List<MediaCategory> getAllMediaCategories(){
        List<MediaCategory> mediaCategories = mediaCategoryRepository.findAll();
        return mediaCategories; //mediaCategories.stream().map(mediaCategories1 -> modelToDtoConverter.convertToDto(mediaCategories1, MediaCategoryDTO.class)).collect(Collectors.toList());
    }

    public MediaCategoryDTO getMediaCategoryById(Long id) {
        MediaCategory mediaCategory = mediaCategoryRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(mediaCategory, MediaCategoryDTO.class);
    }

    public MediaCategory createMediaCategory(MediaCategoryDTO mediaCategoryDTO) throws IOException{
        MediaCategory mediaCategory = new MediaCategory();
        mediaCategory.setMedia_id(mediaCategoryDTO.getMedia_id());
        mediaCategory.setCategory_id(mediaCategoryDTO.getCategory_id());
        mediaCategory.setStatus(1);
        return mediaCategoryRepository.save(mediaCategory);
    }

    public MediaCategory updateMediaCategory(MediaCategoryDTO mediaCategoryDTO){
        try {
            MediaCategory mediaCategory = mediaCategoryRepository.getById(mediaCategoryDTO.getId());
            mediaCategory.setMedia_id(mediaCategoryDTO.getMedia_id());
            mediaCategory.setCategory_id(mediaCategoryDTO.getCategory_id());
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
