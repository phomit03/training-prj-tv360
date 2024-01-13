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
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public MediaDetail createMediaDetail(MediaDetailDTO mediaDetailDTO) throws IOException {
        MediaDetail mediaDetail = dtoToModelConverter.convertToModel(mediaDetailDTO, MediaDetail.class);
        mediaDetail.setStatus(1);
        return mediaDetailRepository.save(mediaDetail);
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

    public List<MediaDetail> getNewRelease() {
        Pageable pageable = PageRequest.of(0, 15, Sort.by("createdAt").descending());
        return mediaDetailRepository.findNewRelease(pageable);
    }

    public List<MediaDetail> getTopRated() {
        Pageable pageable = PageRequest.of(0, 12, Sort.by("createdAt").descending());
        return mediaDetailRepository.findTopRated(pageable);
    }

    // phan media detail
    public List<MediaDetailResponse> getMediaDetailsClient() {
        try {

            return mediaDetailRepository.getMediaDetails();
        }
        catch (Exception e){
            return null;
        }
    }

    // lay ra tat ca categoryname theo mediaDetailID
    public List<String> getCategoryNamesByMediaDetailId(Long mediaId) {
        try {
            return mediaDetailRepository.getCategoryNamesByMediaDetailId(mediaId);
        } catch (Exception e) {
            return null;
        }
    }

    // lay ra tat ca tap phim theo mediaDetailID
    public List<Integer> getEpisodesByMediaDetailId(Long mediaId) {
        try {
            return mediaDetailRepository.getEpisodesByMediaDetailId(mediaId);
        } catch (Exception e) {
            return null;
        }
    }

    // lay ra tat ca castfullname theo mediaDetailID
    public List<String> getCastFullNamesByMediaDetailId(Long mediaId) {
        try {
            return mediaDetailRepository.getCastFullNamesByMediaDetailId(mediaId);
        } catch (Exception e) {
            return null;
        }
    }

    // get by id

    public MediaDetailResponse getMediaDetailClientById(Long mediaId) {
        try {
            List<MediaDetailResponse> mediaDetailResponseList = mediaDetailRepository.getMediaDetailById(mediaId);

            if (mediaDetailResponseList != null && !mediaDetailResponseList.isEmpty()) {
                MediaDetailResponse mediaDetailResponse = mediaDetailResponseList.get(0);
//                mediaDetailResponse.setMediaDetailId(mediaDetailRepository.getIdMediaDetailByMediaId(mediaId));
                mediaDetailResponse.setListCastTypes(mediaDetailRepository.getCastTypesByMediaDetailId(mediaId));
                mediaDetailResponse.setListCategoryNames(mediaDetailRepository.getCategoryNamesByMediaDetailId(mediaId));
//                mediaDetailResponse.setEpisodes(mediaDetailRepository.getEpisodesByMediaDetailId(mediaId));
                mediaDetailResponse.setListCastFullNames(mediaDetailRepository.getCastFullNamesByMediaDetailId(mediaId));

                return mediaDetailResponse;
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }


//    public MediaDetailResponse getMediaDetailClientById1(Long mediaId, List<Long> mdIdList) {
//        try {
//            List<MediaDetailResponse> mediaDetailResponseList = mediaDetailRepository.getMediaDetailById(mediaId);
//
//            if (mediaDetailResponseList != null && !mediaDetailResponseList.isEmpty()) {
//                MediaDetailResponse mediaDetailResponse = mediaDetailResponseList.get(0);
//
//                for (Long mdId : mdIdList) {
//                    mediaDetailResponse.setMediaDetailId(mediaDetailResponse.getMediaDetailId());
//                    mediaDetailResponse.setCategoryNames(mediaDetailRepository.getCategoryNamesByMediaDetailId(mdId));
//                    mediaDetailResponse.setEpisodes(mediaDetailRepository.getEpisodesByMediaDetailId(mdId));
//                    mediaDetailResponse.setCastFullNames(mediaDetailRepository.getCastFullNamesByMediaDetailId(mdId));
//                }
//
//                return mediaDetailResponse;
//            }
//            return null;
//        } catch (Exception e) {
//            return null;
//        }
//    }


    // get theo categoy name
    public List<MediaDetailResponse> getMediaDetailsByCategoryName(String categoryName) {
        try {
            return mediaDetailRepository.getMediaDetailsByCategoryName(categoryName);
        } catch (Exception e) {
            return null;
        }
    }


    // phan trang
    public Page<MediaDetail> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.mediaDetailRepository.findAll(pageable);
    }
    // loc

    public void createListMediaDetail(List<MediaDetail> mediaDetails) {
        mediaDetailRepository.saveAll(mediaDetails);
    }

    public int getMaxEpisodeByMediaId(Long mediaId) {
        Integer maxEpisode = mediaDetailRepository.findMaxEpisodeByMediaId(mediaId);
        return (maxEpisode != null) ? maxEpisode : 0;
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


}
