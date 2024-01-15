package com.example.tv360.controller.admin;

import com.example.tv360.dto.*;
import com.example.tv360.entity.*;
import com.example.tv360.repository.*;
import com.example.tv360.service.*;
import com.example.tv360.service.exception.AssociationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class MovieController {

    @Value("${page.size}")
    private int pageSize;
    private final MediaService mediaService;
    private final CountryService countryService;
    private final CategoryService categoryService;
    private final CastService castService;
    private final MediaRepository mediaRepository;

    public MovieController(MediaService mediaService, CountryService countryService,
                           CategoryService categoryService, CastService castService,
                           MediaRepository mediaRepository) {
        this.mediaService = mediaService;
        this.countryService = countryService;
        this.categoryService = categoryService;
        this.castService = castService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping({"/movie/form", "/movie/form/{id}"})
    public String showMovieForm(@PathVariable(required = false) Long id, Model model) {
        MediaDTO movieDTO = (id != null) ? mediaService.getMediaById(id) : new MediaDTO();
        model.addAttribute("movieDTO", movieDTO);

        List<CategoryDTO> listCategories = categoryService.getCategoriesForMovie();
        model.addAttribute("listCategories", listCategories);

        List<CountryDTO> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);

        List<CastDTO> cast = castService.getAllCasts();
        model.addAttribute("listCast", cast);

        return "admin_movie_form";
    }

    @PostMapping("/movie/save")
    public String createOrUpdateMovie(@ModelAttribute("movieDTO") MediaDTO movieDTO,
                                      @RequestParam("logo") MultipartFile logo,
                                      @RequestParam("categories") HashSet<Long> categories,
                                      @RequestParam("cast") HashSet<Long> cast,
                                      @RequestParam("type") Integer type,
                                      RedirectAttributes redirectAttributes) {
        try {
            if (movieDTO.getId() == null) {
                mediaService.createMedia(movieDTO, logo, categories, cast, type);
                redirectAttributes.addFlashAttribute("success", "Create successfully!");
            } else {
                mediaService.updateMedia(movieDTO.getId(), movieDTO, logo, categories, cast, type);
                redirectAttributes.addFlashAttribute("success", "Update Successfully!");
            }
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed!");
        }
        return "redirect:/admin/movies";
    }

    @GetMapping("/movie/delete/{id}")
    public ResponseEntity<String> softDeleteMovie(@PathVariable Long id){
        try {
            mediaService.softDeleteMedia(id);
            return ResponseEntity.ok("Delete movie successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }

    @GetMapping("/movies")
    public String getAllMovies(Model model,
                              @RequestParam(name = "title", required = false) String title,
                              @RequestParam(name = "type", required = false) Integer type,
                              @RequestParam(name = "status", required = false) Integer status
    ) {
        model.addAttribute("title", "Movies");
        return findPaginated(1, model, title, 1, status);
    }

    @GetMapping("/movies/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "status", required = false) Integer status
    ) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        List<Media> result = mediaRepository.searchMovie(title, 1, status, pageable);
        Page<Media> page = new PageImpl<>(result, pageable,mediaRepository.searchMovie1(title, 1, status).size());

        List<Media> movies = page.getContent();

        //Lấy danh sách MediaDetail cho mỗi Media
        Map<Long, List<MediaDetail>> movieDetailMap = new HashMap<>();
        for (Media movie : movies) {
            List<MediaDetail> mediaDetails = mediaService.getMediaDetails(movie.getId());
            movieDetailMap.put(movie.getId(), mediaDetails);
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("movies", movies);
        model.addAttribute("movieDetailMap", movieDetailMap);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        return "admin_movies";
    }


}
