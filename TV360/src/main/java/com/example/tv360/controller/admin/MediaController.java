package com.example.tv360.controller.admin;

import com.example.tv360.dto.*;
import com.example.tv360.entity.Cast;
import com.example.tv360.entity.Country;
import com.example.tv360.entity.Media;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.CastService;
import com.example.tv360.service.CategoryService;
import com.example.tv360.service.CountryService;
import com.example.tv360.service.MediaService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MediaController {
    private final MediaService mediaService;
    private final CountryService countryService;
    private final CategoryService categoryService;
    private final CastService castService;
    private final MediaRepository mediaRepository;

    public MediaController(MediaService mediaService, CountryService countryService, CategoryService categoryService, CastService castService, MediaRepository mediaRepository, MediaDetailRepository mediaDetailRepository) {
        this.mediaService = mediaService;
        this.countryService = countryService;
        this.categoryService = categoryService;
        this.castService = castService;
        this.mediaRepository = mediaRepository;
    }

//    @GetMapping("/media")
//    public String getAllMedia(Model model) {
//        model.addAttribute("title", "Media");
//        List<MediaDTO> media = mediaService.getAllMedias();
//        model.addAttribute("media1", media);
//
//        return "admin_media";
//    }

    @GetMapping("/media/create")
    public String showCreateMedia(Model model){
        model.addAttribute("mediaDTO", new MediaDTO());

        List<CountryDTO> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);

        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<CastDTO> cast = castService.getAllCasts();
        model.addAttribute("listCast", cast);
        return "admin_media_create";
    }

    @PostMapping("/media/create/save")
    public String createMedia(@ModelAttribute MediaDTO mediaDTO,
                              @RequestParam("logo") MultipartFile logo,
                              @RequestParam("selectedCategories") Long[] selectedCategories,
                              @RequestParam("selectedCast") Long[] selectedCast,
                              RedirectAttributes redirectAttributes) {
        try {
            mediaService.createMedia(mediaDTO, logo, selectedCategories, selectedCast);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/media";

    }

    @GetMapping("/media/update/{id}")
    public String showUpdateMedia(@PathVariable Long id, Model model){
        MediaDTO mediaDTO = mediaService.getMediaById(id);
        model.addAttribute("mediaDTO", mediaDTO);

        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);

        List<CountryDTO> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);

        if (mediaDTO == null){
            return "redirect:/admin/media";
        }

        return "admin_media_update";
    }

    @PostMapping("/media/update/{id}")
    public String updateMedia(@PathVariable Long id, @ModelAttribute("mediaDTO") MediaDTO mediaDTO,
                              @RequestParam(value = "logo", required = false) MultipartFile logo, RedirectAttributes attributes){
        try {
            mediaService.updateMedia(id, mediaDTO, logo);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/media";
    }

    @GetMapping("/media/delete/{id}")
    public ResponseEntity<String> deleteMedia(@PathVariable Long id){
        try {
            mediaService.softDeleteMedia(id);
            return ResponseEntity.ok("Delete media successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }

    @GetMapping("/media")
    public String getAllMedia(Model model,
                              @RequestParam(name = "title", required = false) String title,
                              @RequestParam(name = "type", required = false) Integer type,
                              @RequestParam(name = "status", required = false) Integer status
    ) {
        model.addAttribute("title", "Media");
        return findPaginated(1, model, title,type,status);
    }

    @GetMapping("/media/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "status", required = false) Integer status
    ) {
        int pageSize = 6;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Media> result = mediaRepository.searchMedia(title,type,status, pageable);
        Page<Media> page = new PageImpl<>(result, pageable,mediaRepository.searchMedia1(title,type, status).size());
        List<Media> media = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("media1", media);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        return "admin_media";
    }
}
