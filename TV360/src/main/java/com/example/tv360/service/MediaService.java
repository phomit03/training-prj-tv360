package com.example.tv360.service;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.Country;
import com.example.tv360.entity.Media;
import com.example.tv360.repository.CastRepository;
import com.example.tv360.repository.CategoryRepository;
import com.example.tv360.repository.CountryRepository;
import com.example.tv360.repository.MediaRepository;
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
    private final CountryRepository countryRepository;
    private final CastRepository castRepository;
    private final CategoryRepository categoryRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;

    @Autowired
    public MediaService(MediaRepository mediaRepository, ModelToDtoConverter modelToDtoConverter, Helper helper,
                        CountryRepository countryRepository, CategoryService categoryService,
                        CastRepository castRepository, CategoryRepository categoryRepository, DtoToModelConverter dtoToModelConverter) {
        this.mediaRepository = mediaRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.helper = helper;
        this.castRepository = castRepository;
        this.categoryRepository = categoryRepository;
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

    public Media createMedia(MediaDTO mediaDTO, MultipartFile logo,
                             Long[] selectedCategories, Long[] selectedCast
                             ) throws IOException {
        Media media = dtoToModelConverter.convertToModel(mediaDTO, Media.class);

        Set<Category> categories = new LinkedHashSet<>();
        for (Long categoryId : selectedCategories) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            if (category != null) {
                categories.add(category);
            }
        }
        media.setCategories(categories);

        if (!logo.isEmpty()) {
            String thumbnail = helper.uploadImage(logo);
            media.setThumbnail(thumbnail);
        }

        Set<Cast> listCast = new LinkedHashSet<>();
        for (Long castId : selectedCast) {
            Cast cast = castRepository.findById(castId).orElse(null);
            if (cast != null) {
                listCast.add(cast);
            }
        }
        media.setCast(listCast);

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

    public List<MediaDTO> getMediaWithCategories() {
        List<Media> mediaList = mediaRepository.findAll();

        List<MediaDTO> mediaDTOList = new ArrayList<>();

        for (Media media : mediaList) {
            MediaDTO mediaDTO = new MediaDTO();
            Set<CategoryDTO> categoryDTOList = new HashSet<>();
            for (Category category : media.getCategories()) {
                if (category != null){
                    CategoryDTO categoryDTO = new CategoryDTO();
                    categoryDTOList.add(categoryDTO);
                }
            }

            mediaDTO.setCategories(categoryDTOList);
            mediaDTOList.add(mediaDTO);
        }

        return mediaDTOList;
    }

    //phan trang
    public Page<Country> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.countryRepository.findAll(pageable);
    }
}
