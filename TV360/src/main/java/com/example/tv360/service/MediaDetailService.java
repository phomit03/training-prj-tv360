package com.example.tv360.service;

import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.utils.DtoToModelConverter;
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
public class MediaDetailService {
    private final MediaDetailRepository mediaDetailRepository;
    private final MediaRepository mediaRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;
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

//    public MediaDetail createMediaDetail(MediaDetailDTO mediaDetailDTO) throws IOException{
//        MediaDetail mediaDetail = new MediaDetail();
//        mediaDetail.setSourceUrl(mediaDetailDTO.getSourceUrl());
//        mediaDetail.setTypeUrl(mediaDetailDTO.getTypeUrl());
//        mediaDetail.setRate(mediaDetailDTO.getRate());
//        mediaDetail.setDuration(mediaDetailDTO.getDuration());
//        mediaDetail.setQuality(mediaDetailDTO.getQuality());
//        mediaDetail.setMedia(mediaRepository.findById(mediaDetailDTO.getMediaId()).get());
//        mediaDetail.setStatus(1);
//
//        return mediaDetailRepository.save(mediaDetail);
//    }
    public MediaDetail createMediaDetail(MediaDetailDTO mediaDetailDTO) throws IOException {
        MediaDetail mediaDetail = dtoToModelConverter.convertToModel(mediaDetailDTO, MediaDetail.class);
        mediaDetail.setStatus(1);
        return mediaDetailRepository.save(mediaDetail);
    }

        public MediaDetail updateMediaDetail(Long id,MediaDetailDTO mediaDetailDTO){
        try {
            MediaDetail mediaDetail = mediaDetailRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
            MediaDetail updateMediaDetail1 = dtoToModelConverter.convertToModel(mediaDetailDTO, MediaDetail.class);
//            mediaDetail.setSourceUrl(mediaDetailDTO.getSourceUrl());
//            mediaDetail.setRate(mediaDetailDTO.getRate());
//            mediaDetail.setDuration(mediaDetailDTO.getDuration());
//            mediaDetail.setQuality(mediaDetailDTO.getQuality());
//            mediaDetail.setTypeUrl(mediaDetailDTO.getTypeUrl());
//            mediaDetail.setMedia(mediaDetailDTO.getMedia());
//            mediaDetail.setStatus(mediaDetailDTO.getStatus());
            mediaDetail.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            mediaDetail = updateMediaDetail1;
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

}
