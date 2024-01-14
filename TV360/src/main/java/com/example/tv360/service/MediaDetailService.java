package com.example.tv360.service;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MediaDetailService {
    private final MediaDetailRepository mediaDetailRepository;
    private final MediaRepository mediaRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    public MediaDetailService(MediaDetailRepository mediaDetailRepository, MediaRepository mediaRepository, ModelToDtoConverter modelToDtoConverter , DtoToModelConverter dtoToModelConverter ) {
        this.mediaDetailRepository = mediaDetailRepository;
        this.mediaRepository = mediaRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;

    }

    public List<MediaDetailDTO> getAllMediaDetails(){
        List<MediaDetail> mediaDetails = mediaDetailRepository.findAll();
        return mediaDetails.stream().map(mediaDetail -> modelToDtoConverter.convertToDto(mediaDetail, MediaDetailDTO.class)).collect(Collectors.toList());
    }

    public MediaDetailDTO getMediaDetailById(Long id) {
        MediaDetail mediaDetails = mediaDetailRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(mediaDetails, MediaDetailDTO.class);
    }

    public MediaDetail createMediaDetail(MediaDetailDTO mediaDetailDTO) {
        MediaDetail mediaDetail = dtoToModelConverter.convertToModel(mediaDetailDTO, MediaDetail.class);
        mediaDetail.setStatus(1);
        return mediaDetailRepository.save(mediaDetail);
    }

    public void createListMediaDetail(MediaDetailDTO mediaDetailDTO, HttpServletRequest request) {
        int maxEpisode = getMaxEpisodeByMediaId(mediaDetailDTO.getMedia().getId());

        String[] sourceUrls = request.getParameterValues("sourceUrl");
        String[] durations = request.getParameterValues("duration");
        String[] rates = request.getParameterValues("rate");
        String[] qualities = request.getParameterValues("quality");

        Set<MediaDetail> mediaDetails = new LinkedHashSet<>();

        for (int i = 0; i < sourceUrls.length; i++) {
            MediaDetail mediaDetail = dtoToModelConverter.convertToModel(mediaDetailDTO, MediaDetail.class);

            mediaDetail.setSourceUrl(sourceUrls[i]);
            mediaDetail.setEpisode(maxEpisode + 1 + i);
            mediaDetail.setDuration(durations[i]);
            mediaDetail.setRate(Integer.parseInt(rates[i]));
            mediaDetail.setQuality(qualities[i]);
            mediaDetail.setStatus(1);
            mediaDetail.setMedia(mediaRepository.findById(mediaDetailDTO.getMedia().getId()).orElse(null));

            mediaDetails.add(mediaDetail);
        }

        mediaDetailRepository.saveAll(mediaDetails);
    }

    //Episode ascending
    public int getMaxEpisodeByMediaId(Long mediaId) {
        Integer maxEpisode = mediaDetailRepository.findMaxEpisodeByMediaId(mediaId);
        return (maxEpisode != null) ? maxEpisode : 0;
    }

    public MediaDetail updateMediaDetail(Long id, MediaDetailDTO mediaDetailDTO){
        try {
            MediaDetail mediaDetail = mediaDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
            MediaDetail updateMediaDetail = dtoToModelConverter.convertToModel(mediaDetailDTO, MediaDetail.class);
            mediaDetail.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            mediaDetail = updateMediaDetail;
            return mediaDetailRepository.save(mediaDetail);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void softDeleteMediaDetail(Long id){
        Optional<MediaDetail> optionalMediaDetail = mediaDetailRepository.findById(id);
        if (optionalMediaDetail.isPresent()) {
            MediaDetail mediaDetail = optionalMediaDetail.get();
            mediaDetail.setStatus(0);
            mediaDetailRepository.save(mediaDetail);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }


    //user homepage
    public List<MediaDetail> getNewRelease() {
        Pageable pageable = PageRequest.of(0, 15, Sort.by("createdAt").descending());
        return mediaDetailRepository.findNewRelease(pageable);
    }

    public List<MediaDetail> getTopRated() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return mediaDetailRepository.findTopRated(pageable);
    }

    // phan trang
    public Page<MediaDetail> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.mediaDetailRepository.findAll(pageable);
    }

    public List<CategoryDTO> getCategoriesByMediaDetailId(Long mediaId) {
        List<String> categoryNames = mediaDetailRepository.getCategoryNamesByMediaDetailId(mediaId);

        String jpql = "SELECT c FROM Category c LEFT JOIN FETCH c.media m WHERE m IS NOT NULL AND c.name IN :categoryNames ORDER BY m.createdAt DESC";

        TypedQuery<Category> query = entityManager.createQuery(jpql, Category.class);
        query.setParameter("categoryNames", categoryNames);
        query.setMaxResults(15);

        List<Category> categories = query.getResultList();

        return categories.stream()
                .map(category -> modelToDtoConverter.convertToDto(category, CategoryDTO.class))
                .collect(Collectors.toList());
    }

    // get theo categoy name
    public List<MediaDetailResponse> getMediaDetailsByCategoryName(String categoryName) {
        try {
            return mediaDetailRepository.getMediaDetailsByCategoryName(categoryName);
        } catch (Exception e) {
            return null;
        }
    }

    //service details (le-cuong)
    public List<MediaDetailResponse> getMediaDetailByIdASCEpisodes(Long mediaId) {
        try {
            List<MediaDetailResponse> mediaDetailResponseList = mediaDetailRepository.getMediaDetailByIdASCEpisodes(mediaId);

            for (MediaDetailResponse mediaDetailResponse : mediaDetailResponseList) {
                updateMediaDetailResponse(mediaDetailResponse, mediaId);
            }

            return mediaDetailResponseList;
        } catch (Exception e) {
            // Log lỗi hoặc xử lý lỗi theo nhu cầu của bạn
            return Collections.emptyList();
        }
    }

    private void updateMediaDetailResponse(MediaDetailResponse mediaDetailResponse, Long mediaId) {
        mediaDetailResponse.setCategoryList(mediaDetailRepository.getCategoryByMediaDetailId(mediaId));
        mediaDetailResponse.setCastList(mediaDetailRepository.getCastByMediaDetailId(mediaId));
        mediaDetailResponse.setListCategoryNames(mediaDetailRepository.getCategoryNamesByMediaDetailId(mediaId));
    }

}
