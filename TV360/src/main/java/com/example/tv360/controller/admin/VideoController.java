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
public class VideoController {

    @Value("${page.size}")
    private int pageSize;
    private final MediaService mediaService;
    private final CountryService countryService;
    private final CategoryService categoryService;
    private final CastService castService;
    private final MediaRepository mediaRepository;

    public VideoController(MediaService mediaService, CountryService countryService, CategoryService categoryService,
                           CastService castService, MediaRepository mediaRepository,
                           MediaDetailRepository mediaDetailRepository) {
        this.mediaService = mediaService;
        this.countryService = countryService;
        this.categoryService = categoryService;
        this.castService = castService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping({"/video/form", "/video/form/{id}"})
    public String showVideoForm(@PathVariable(required = false) Long id, Model model) {
        if (id != null) {
            MediaDTO videoDTO = mediaService.getMediaById(id);
            model.addAttribute("videoDTO", videoDTO);
        } else {
            model.addAttribute("videoDTO", new MediaDTO());
        }

        List<CategoryDTO> listCategories = categoryService.getCategoriesForVideo();
        model.addAttribute("listCategories", listCategories);

        List<CountryDTO> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);

        return "admin_video_form";
    }

    @PostMapping("/video/save")
    public String createOrUpdateVideo(@ModelAttribute("videoDTO") MediaDTO videoDTO,
                              @RequestParam("logo") MultipartFile logo,
                              @RequestParam("categories") HashSet<Long> categories,
                              RedirectAttributes redirectAttributes) {
        try {
            if (videoDTO.getId() == null) {
                mediaService.createMedia(videoDTO, logo, categories, null, 3);
                redirectAttributes.addFlashAttribute("success", "Create successfully!");
            } else {
                mediaService.updateMedia(videoDTO.getId(), videoDTO, logo, categories, null, 3);
                redirectAttributes.addFlashAttribute("success", "Update Successfully!");
            }
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed!");
        }
        return "redirect:/admin/videos";
    }

    @GetMapping("/video/delete/{id}")
    public ResponseEntity<String> softDeleteVideo(@PathVariable Long id){
        try {
            mediaService.softDeleteMedia(id);
            return ResponseEntity.ok("Delete video successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }

    @GetMapping("/videos")
    public String getAllVideos(Model model,
                              @RequestParam(name = "title", required = false) String title,
                               @RequestParam(name = "status", required = false) Integer status) {
        model.addAttribute("title", "videos");
        return findPaginated(1, model, title, 3, status);
    }

    @GetMapping("/videos/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "status", required = false) Integer status
    ) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Media> result = mediaRepository.searchVideo(title, 3, status, pageable);
        Page<Media> page = new PageImpl<>(result, pageable, mediaRepository.searchVideo1(title, 3, status).size());

        List<Media> videos = page.getContent();

        //Lấy danh sách MediaDetail cho mỗi Media
        Map<Long, List<MediaDetail>> videoDetailMap = new HashMap<>();
        for (Media video : videos) {
            List<MediaDetail> mediaDetails = mediaService.getMediaDetails(video.getId());
            videoDetailMap.put(video.getId(), mediaDetails);
        }

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("videos", videos);
        model.addAttribute("videoDetailMap", videoDetailMap);
        model.addAttribute("title", title);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        return "admin_videos";
    }

}
