package com.example.tv360.service;

import com.example.tv360.config.CacheConfig;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.redis.RedisCacheService;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
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
    private final RedisCacheService redisCacheService;
    private final CacheConfig cacheConfig;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public MediaDetailService(MediaDetailRepository mediaDetailRepository, MediaRepository mediaRepository, ModelToDtoConverter modelToDtoConverter , DtoToModelConverter dtoToModelConverter, RedisCacheService redisCacheService, CacheConfig cacheConfig) {
        this.mediaDetailRepository = mediaDetailRepository;
        this.mediaRepository = mediaRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;
        this.redisCacheService = redisCacheService;
        this.cacheConfig = cacheConfig;
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
    public Page<Media> findPaginated1(int pageNo, int pageSize, List<Media> media) {
        // Tạo một danh sách Pageable từ danh sách các trận đấu
        List<Media> paginatedMedia = media.stream()
                .skip((long) (pageNo - 1) * pageSize)
                .limit(pageSize)
                .collect(Collectors.toList());

        return new PageImpl<>(paginatedMedia, PageRequest.of(pageNo - 1, pageSize), media.size());
    }

    //service details (le-cuong)
    public List<MediaDetailResponse> getMediaDetailByMediaId(Long mediaId) {    //list <categories, cast, episode>
        try {

            if (redisCacheService.checkExistsKey(cacheConfig.keyMediaEpisodePrefix+mediaId) ||
                    redisCacheService.checkExistsKey(cacheConfig.keyMediaCategoriesPrefix+mediaId) ||
                    redisCacheService.checkExistsKey(cacheConfig.keyMediaCastPrefix+mediaId)) {
                /*return ;*/
                //chưa biết trả về gì ở đây
            } else {
                Media media = mediaRepository.findById(mediaId).orElse(null);

                if (media != null && media.getType() == 3) {
                    List<MediaDetailResponse> videoDetailResponseList = mediaDetailRepository.getVideoDetail(mediaId);

                    for (MediaDetailResponse videoDetailResponse : videoDetailResponseList) {
                        videoDetailResponse.setCategoryList(mediaDetailRepository.getCategoryByMediaDetailId(mediaId));
                    }

                    redisCacheService.setValue(cacheConfig.keyMediaEpisodePrefix+mediaId, videoDetailResponseList);

                    return videoDetailResponseList;
                } else {
                    List<MediaDetailResponse> movieDetailResponseList = mediaDetailRepository.getMovieDetailByIdASCEpisodes(mediaId);

                    for (MediaDetailResponse movieDetailResponse : movieDetailResponseList) {
                        movieDetailResponse.setCategoryList(mediaDetailRepository.getCategoryByMediaDetailId(mediaId));
                        movieDetailResponse.setCastList(mediaDetailRepository.getCastByMediaDetailId(mediaId));
                    }

                    if (media != null) {
                        redisCacheService.setValue(cacheConfig.keyMediaEpisodePrefix+mediaId, movieDetailResponseList);
                    }

                    return movieDetailResponseList;
                }
            }
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public List<Media> getRelatedMediaWithoutCurrent(Media media) {     //list related
        if (redisCacheService.checkExistsKey(cacheConfig.keyMediaPrefix+media.getId())) {
            // Nếu tồn tại, lấy dữ liệu từ cache
            return (List<Media>) redisCacheService.getValue(cacheConfig.keyMediaPrefix+media.getId());
        } else {
            // Nếu không tồn tại, lấy từ db
            Set<MediaDetail> mediaDetails = media.getMediaDetails();

            // Tìm ra media liên quan từ danh sách media-details
            List<Media> relatedMedia = mediaDetails.stream()
                    .flatMap(mediaDetail -> mediaDetail.getMedia().getCategories().stream())
                    .flatMap(category -> category.getMedia().stream())
                    .filter(relatedMediaItem -> !relatedMediaItem.equals(media))
                    .filter(relatedMediaItem -> relatedMediaItem.getStatus() == 1)
                    .distinct()
                    .collect(Collectors.toList());

            // Lưu dữ liệu vào cache để sử dụng cho các lần truy vấn sau
            if (!relatedMedia.isEmpty()) {
                redisCacheService.setValue(cacheConfig.keyMediaPrefix+media.getId(), relatedMedia);
            }

            return relatedMedia;
        }
    }

}
