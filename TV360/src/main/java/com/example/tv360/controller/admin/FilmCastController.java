package com.example.tv360.controller.admin;

import com.example.tv360.dto.*;
import com.example.tv360.repository.FilmCastRepository;
import com.example.tv360.repository.MediaCategoryRepository;
import com.example.tv360.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class FilmCastController {
    private final FilmCastService filmCastService;
    private final MediaService mediaService;
    private final CastService castService;
    private final FilmCastRepository filmCastRepository;
    @Autowired
    public FilmCastController(FilmCastService filmCastService, MediaService mediaService, CastService castService, FilmCastRepository filmCastRepository) {
        this.filmCastService = filmCastService;
        this.mediaService = mediaService;
        this.castService = castService;
        this.filmCastRepository = filmCastRepository;
    }

    @GetMapping("/film-casts")
    public String getAllFilmCasts(Model model) {
        model.addAttribute("title", "Film Casts");
        List<FilmCastDTO> fcDTO = filmCastService.getAllFilmCasts();
        model.addAttribute("fcDTO", fcDTO);
        return "admin_film_cast";
    }

    @GetMapping("/film-cast/create")
    public String showCreateFilmCast(Model model){
        model.addAttribute("fcDTO", new FilmCastDTO());
        List<MediaDTO> medias = mediaService.getAllMedias();
        List<CastDTO> casts = castService.getAllCasts();
        model.addAttribute("medias", medias);
        model.addAttribute("casts", casts);
        return "admin_film_cast_create";
    }

    @PostMapping("/film-cast/create/save")
    public String createFilmCast(@ModelAttribute FilmCastDTO fcDTO, RedirectAttributes redirectAttributes) {
        try {
            filmCastService.createFilmCast(fcDTO);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/film-casts";
    }

    @GetMapping("/film-cast/update/{id}")
    public String showUpdateFilmCast(@PathVariable Long id, Model model){
        List<MediaDTO> media = mediaService.getAllMedias();
        List<CastDTO> casts = castService.getAllCasts();
        FilmCastDTO fcDTO = filmCastService.getFilmCastById(id);
        if (fcDTO == null){
            return "redirect:/admin/film-casts";
        }
        model.addAttribute("medias", media);
        model.addAttribute("casts", casts);
        model.addAttribute("fcDTO", fcDTO);
        return "admin_film_cast_update";
    }

    @PostMapping("/film-cast/update/{id}")
    public String updateFilmCast(@PathVariable Long id, @ModelAttribute("mcDTO") FilmCastDTO fcDTO, RedirectAttributes attributes){
        try {
            filmCastService.updateFilmCast(id, fcDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/film-casts";
    }

    @GetMapping("/film-cast/delete/{id}")
    public ResponseEntity<String> deleteFilmCast(@PathVariable Long id){
        try {
            filmCastService.softDeleteFilmCast(id);
            return ResponseEntity.ok("Delete film cast successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }
}
