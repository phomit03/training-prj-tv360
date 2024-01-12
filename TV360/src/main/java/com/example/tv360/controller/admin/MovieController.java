package com.example.tv360.controller.admin;

import com.example.tv360.dto.CastDTO;
import com.example.tv360.dto.CategoryDTO;
import com.example.tv360.dto.CountryDTO;
import com.example.tv360.dto.MediaDTO;
import com.example.tv360.entity.Media;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.CastService;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.CountryService;
import com.example.tv360.service.MediaService;
import com.example.tv360.service.exception.AssociationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class MovieController {
    private final MediaService mediaService;
    private final CountryService countryService;
    private final CategoryService categoryService;
    private final CastService castService;
    private final MediaRepository mediaRepository;

    public MovieController(MediaService mediaService, CountryService countryService,
                           CategoryService categoryService, CastService castService,
                           MediaRepository mediaRepository, MediaDetailRepository mediaDetailRepository) {
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
                                      @RequestParam("selectedCategories") Long[] selectedCategories,
                                      @RequestParam("selectedCast") Long[] selectedCast,
                                      RedirectAttributes redirectAttributes) {
        try {
            if (movieDTO.getId() == null) {
                mediaService.createMovie(movieDTO, logo, selectedCategories, selectedCast);
                redirectAttributes.addFlashAttribute("success", "Create successfully!");
            } else {
                mediaService.updateMovie(movieDTO.getId(), movieDTO, logo, selectedCategories, selectedCast);
                redirectAttributes.addFlashAttribute("success", "Update Successfully!");
            }
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed!");
        }
        return "redirect:/admin/movies";
    }

    @GetMapping("/movie/delete/{id}")
    public ResponseEntity<String> deleteMovie(@PathVariable Long id){
        try {
            mediaService.softDeleteMedia(id);
            return ResponseEntity.ok("Delete movie successfully");
        } catch (AssociationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
        return findPaginated(1, model, title, type, status);
    }

    @GetMapping("/movies/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "status", required = false) Integer status
    ) {
        int pageSize = 6;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Media> result = mediaRepository.searchMedia(title, type, status, pageable);
        Page<Media> page = new PageImpl<>(result, pageable,mediaRepository.searchMedia1(title, type, status).size());
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
