package com.example.tv360.Service;

import com.example.tv360.DTO.CastDTO;
import com.example.tv360.Entity.Cast;
import com.example.tv360.Repository.CastRepository;
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
public class CastService {
    private final CastRepository castRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    @Autowired
    public CastService(CastRepository castRepository,ModelToDtoConverter modelToDtoConverter ) {
        this.castRepository = castRepository;
        this.modelToDtoConverter = modelToDtoConverter;
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
        Cast cast = new Cast();
        cast.setFullName(cast.getFullName());
        cast.setType(castDTO.getType());
        cast.setStatus(1);
        return castRepository.save(cast);
    }

    public Cast updateCast(CastDTO castDTO){
        try {
            Cast cast = castRepository.getById(castDTO.getId());
            cast.setFullName(castDTO.getFullName());
            cast.setType(castDTO.getType());
            cast.setStatus(castDTO.getStatus());
            cast.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            return castRepository.save(cast);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void softDeleteCast(Long id){
        Optional<Cast> optionalCast = castRepository.findById(id);
        if (optionalCast.isPresent()) {
            Cast cast = optionalCast.get();
            cast.setStatus(0);
            castRepository.save(cast);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }
}
