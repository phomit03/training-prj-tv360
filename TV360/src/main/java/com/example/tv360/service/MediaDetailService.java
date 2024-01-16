package com.example.tv360.service;

import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.Category;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
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

    public Page<MediaDetailResponse> findPaginated1(int pageNo, int pageSize, List<MediaDetailResponse> mediaDetails) {
        // Tạo một danh sách Pageable từ danh sách các trận đấu
        List<MediaDetailResponse> paginatedMDResponse = mediaDetails.stream()
                .skip((long) (pageNo - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return new PageImpl<>(paginatedMDResponse, PageRequest.of(pageNo - 1, pageSize), mediaDetails.size());
    }
    public Page<Media> findPaginated2(int pageNo, int pageSize, List<Media> media) {
        // Tạo một danh sách Pageable từ danh sách các trận đấu
        List<Media> paginatedMedia = media.stream()
                .skip((long) (pageNo - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return new PageImpl<>(paginatedMedia, PageRequest.of(pageNo - 1, pageSize), media.size());
    }

    //service details (le-cuong)
    public List<MediaDetailResponse> getMediaDetailByMediaId(Long mediaId) {
        try {
            Media media = mediaRepository.findById(mediaId).orElse(null);

            if (media != null && media.getType() == 3) {
                List<MediaDetailResponse> videoDetailResponseList = mediaDetailRepository.getVideoDetail(mediaId);

                for (MediaDetailResponse videoDetailResponse : videoDetailResponseList) {
                    videoDetailResponse.setCategoryList(mediaDetailRepository.getCategoryByMediaDetailId(mediaId));
                }

                return videoDetailResponseList;
            } else {
                List<MediaDetailResponse> mediaDetailResponseList = mediaDetailRepository.getMovieDetailByIdASCEpisodes(mediaId);

                for (MediaDetailResponse mediaDetailResponse : mediaDetailResponseList) {
                    mediaDetailResponse.setCategoryList(mediaDetailRepository.getCategoryByMediaDetailId(mediaId));
                    mediaDetailResponse.setCastList(mediaDetailRepository.getCastByMediaDetailId(mediaId));
                }

                return mediaDetailResponseList;
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Media> getRelatedMediaWithoutCurrent(Media media) {

        Set<MediaDetail> mediaDetails = media.getMediaDetails();

        // Tìm ra media liên quan từ danh sách media-details
        List<Media> relatedMedia = mediaDetails.stream()
                .flatMap(mediaDetail -> mediaDetail.getMedia().getCategories().stream())
                .flatMap(category -> category.getMedia().stream())
                .filter(relatedMediaItem -> !relatedMediaItem.equals(media))
                .filter(relatedMediaItem -> relatedMediaItem.getStatus() == 1)
                .distinct()
                .collect(Collectors.toList());

        return relatedMedia;
    }

}
