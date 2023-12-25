package com.example.tv360.service;

import com.example.tv360.dto.FilmCastDTO;
import com.example.tv360.repository.*;
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
public class FilmCastService {
    private final FilmCastRepository filmCastRepository;
    private final ModelToDtoConverter modelToDtoConverter;
    private final DtoToModelConverter dtoToModelConverter;
    private final CastRepository castRepository;
    private final MediaRepository mediaRepository;

    @Autowired
    public FilmCastService(FilmCastRepository filmCastRepository, ModelToDtoConverter modelToDtoConverter, DtoToModelConverter dtoToModelConverter, MediaRepository mediaRepository, CastRepository castRepository ) {
        this.filmCastRepository = filmCastRepository;
        this.modelToDtoConverter = modelToDtoConverter;
        this.dtoToModelConverter = dtoToModelConverter;
        this.castRepository = castRepository;
        this.mediaRepository = mediaRepository;

    }

    public List<FilmCastDTO> getAllFilmCasts(){
        List<FilmCast> filmCasts = filmCastRepository.findAll();
        return filmCasts.stream().map(filmCasts1 -> modelToDtoConverter.convertToDto(filmCasts1, FilmCastDTO.class)).collect(Collectors.toList());
    }

    public FilmCastDTO getFilmCastById(Long id) {
        FilmCast filmCast = filmCastRepository.findById(id).orElse(null);
        return modelToDtoConverter.convertToDto(filmCast, FilmCastDTO.class);
    }

    public FilmCast createFilmCast(FilmCastDTO filmCastDTO) throws IOException{
        FilmCast filmCast = dtoToModelConverter.convertToModel(filmCastDTO, FilmCast.class);
        filmCast.setStatus(1);
        return filmCastRepository.save(filmCast);
    }

    public FilmCast updateFilmCast(Long id,FilmCastDTO filmCastDTO){
        try {
            FilmCast filmCast = filmCastRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
            FilmCast filmCast1 = dtoToModelConverter.convertToModel(filmCastDTO, FilmCast.class);
            filmCast.setMedia(mediaRepository.findById(filmCast.getMedia().getId()).get());
            filmCast.setCast(castRepository.findById(filmCast.getCast().getId()).get());
            filmCast.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));
            filmCast = filmCast1;
            return filmCastRepository.save(filmCast);
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void softDeleteFilmCast(Long id){
        Optional<FilmCast> optionalFilmCast = filmCastRepository.findById(id);
        if (optionalFilmCast.isPresent()) {
            FilmCast filmCast = optionalFilmCast.get();
            filmCast.setStatus(0);
            filmCastRepository.save(filmCast);
        } else {
            throw new EntityNotFoundException("Entity with id " + id + " not found.");
        }
    }
}
