package com.example.tv360.service;

import com.example.tv360.dto.MediaDTO;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.*;
import com.example.tv360.repository.*;
import com.example.tv360.service.exception.AssociationException;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.Helper;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaService {

    private Helper helper;
    private final MediaRepository mediaRepository;
    private final CastRepository castRepository;
    private final CategoryRepository categoryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;

    @Autowired
    public MediaService(MediaRepository mediaRepository, ModelToDtoConverter modelToDtoConverter, Helper helper,
                        CastRepository castRepository, CategoryRepository categoryRepository,
                        DtoToModelConverter dtoToModelConverter) {
        this.mediaRepository = mediaRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.helper = helper;
        this.castRepository = castRepository;
        this.categoryRepository = categoryRepository;
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

    public Media createMedia(MediaDTO mediaDTO, MultipartFile logo,
                             HashSet<Long> selectedCategories,
                             HashSet<Long> selectedCast,
                             Integer type
    ) throws IOException {
        Media media = dtoToModelConverter.convertToModel(mediaDTO, Media.class);

        if (!logo.isEmpty()) {
            String thumbnail = helper.uploadImage(logo);
            media.setThumbnail(thumbnail);
        }

        //create multiple select
        Set<Category> categories = new LinkedHashSet<>();
        for (Long categoryId : selectedCategories) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                categories.add(category);
            }
        }
        media.setCategories(categories);

        if (selectedCast == null) {
            media.setCast(null);
        } else {
            //create multiple select
            Set<Cast> listCast = new LinkedHashSet<>();
            for (Long castId : selectedCast) {
                Cast cast = castRepository.findById(castId).orElse(null);
                if (cast != null) {
                    listCast.add(cast);
                }
            }
            media.setCast(listCast);
        }

        media.setType(type);
        media.setStatus(1);

        return mediaRepository.save(media);
    }

    public Media updateMedia(Long id, MediaDTO mediaDTO,
                             MultipartFile logo,
                             HashSet<Long> selectedCategories,
                             HashSet<Long> selectedCast,
                             Integer type){
        try {
            Media existingMedia = mediaRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());

            Media media = dtoToModelConverter.convertToModel(mediaDTO, Media.class);

            media.setThumbnail(existingMedia.getThumbnail());

            BeanUtils.copyProperties(media, existingMedia);
            if (!logo.isEmpty()) {
                String thumbnail = helper.uploadImage(logo);
                existingMedia.setThumbnail(thumbnail);
            }

            //update multiple select
            List<Category> updateCategories = categoryRepository.findAllById(selectedCategories);
            existingMedia.setCategories(new HashSet<>(updateCategories));

            if (selectedCast == null) {
                existingMedia.setCast(null);
            } else {
                //update multiple select
                List<Cast> updateCast = castRepository.findAllById(selectedCast);
                existingMedia.setCast(new HashSet<>(updateCast));
            }

            existingMedia.setType(type);
            existingMedia.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

            return mediaRepository.save(existingMedia);
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

            if (isMediaUsedInMedia(media)) {
                throw new AssociationException("Cannot delete media as it is associated with media.");
            }

            media.setStatus(0);
            mediaRepository.save(media);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }

    private boolean isMediaUsedInMedia(Media media) {
        int mediaCount = mediaRepository.countMediaDetailByMediaId(media.getId());
        return mediaCount > 0;
    }


    //phan trang
    public Page<Media> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.mediaRepository.findAll(pageable);
    }

    public List<MediaDetail> getMediaDetails(Long mediaId) {
        return mediaRepository.findMediaDetailsByMediaId(mediaId);
    }

}
