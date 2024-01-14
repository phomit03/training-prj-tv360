package com.example.tv360.service;

import com.example.tv360.dto.CastDTO;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Category;
import com.example.tv360.repository.CastRepository;
import com.example.tv360.service.exception.AssociationException;
import com.example.tv360.utils.DtoToModelConverter;
import com.example.tv360.utils.ModelToDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CastService {
    private final CastRepository castRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;
    @Autowired
    public CastService(CastRepository castRepository,ModelToDtoConverter modelToDtoConverter,DtoToModelConverter dtoToModelConverter ) {
        this.castRepository = castRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;
    }

    public List<CastDTO> getAllCasts(){
        List<Cast> casts = castRepository.findAll();
        return casts.stream().map(cast -> modelToDtoConverter.convertToDto(cast, CastDTO.class)).collect(Collectors.toList());
    }

    public CastDTO getCastById(Long id) {
        Cast cast = castRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(cast, CastDTO.class);
    }

    public Cast createCast(CastDTO castDTO) throws IOException{
        Cast cast = dtoToModelConverter.convertToModel(castDTO, Cast.class);
        cast.setStatus(1);
        return castRepository.save(cast);
    }

    public Cast updateCast(Long id,CastDTO castDTO){
        Cast cast = castRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        Cast updateCast1 = dtoToModelConverter.convertToModel(castDTO, Cast.class);
        cast.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
        cast = updateCast1;

        if (updateCast1.getStatus() == 0 && isCastUsedInMedia(updateCast1)) {
            throw new IllegalStateException("Cannot update cast status to in-active when it is associated with media.");
        }

        return castRepository.save(cast);
    }

    public void softDeleteCast(Long id){
        Optional<Cast> optionalCast = castRepository.findById(id);
        if (optionalCast.isPresent()) {
            Cast cast = optionalCast.get();

            if (isCastUsedInMedia(cast)) {
                throw new AssociationException("Cannot delete cast as it is associated with media.");
            }

            cast.setStatus(0);
            castRepository.save(cast);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }

    private boolean isCastUsedInMedia(Cast cast) {
        int mediaCount = castRepository.countMediaByCastId(cast.getId());
        return mediaCount > 0;
    }

    //phan trang
    public Page<Cast> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return this.castRepository.findAll(pageable);
    }
}
