package com.example.tv360.Service;

import com.example.tv360.DTO.MediaDetailDTO;
import com.example.tv360.Entity.MediaDetail;
import com.example.tv360.Repository.MediaDetailRepository;
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
public class MediaDetailService {
    private final MediaDetailRepository mediaDetailRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    @Autowired
    public MediaDetailService(MediaDetailRepository mediaDetailRepository, ModelToDtoConverter modelToDtoConverter ) {
        this.mediaDetailRepository = mediaDetailRepository;
        this.modelToDtoConverter = modelToDtoConverter;
    }

    public List<MediaDetailDTO> getAllMediaDetails(){
        List<MediaDetail> mediaDetails = mediaDetailRepository.findAll();
        return mediaDetails.stream().map(mediaDetails1 -> modelToDtoConverter.convertToDto(mediaDetails1, MediaDetailDTO.class)).collect(Collectors.toList());
    }

    public MediaDetailDTO getMediaDetailById(Long id) {
        MediaDetail mediaDetails = mediaDetailRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(mediaDetails, MediaDetailDTO.class);
    }

    public MediaDetail createMediaDetail(MediaDetailDTO mediaDetailDTO) throws IOException{
        MediaDetail mediaDetail = new MediaDetail();
        mediaDetail.setMediaUrl(mediaDetailDTO.getMediaUrl());
        mediaDetail.setType(mediaDetailDTO.getType());
        mediaDetail.setStatus(1);
        return mediaDetailRepository.save(mediaDetail);
    }

    public MediaDetail updateMediaDetail(MediaDetailDTO mediaDetailDTO){
        try {
            MediaDetail mediaDetail = mediaDetailRepository.getById(mediaDetailDTO.getId());
            mediaDetail.setMediaUrl(mediaDetailDTO.getMediaUrl());
            mediaDetail.setType(mediaDetailDTO.getType());
            mediaDetail.setStatus(mediaDetailDTO.getStatus());
            mediaDetail.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
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
