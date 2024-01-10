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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class VideoController {
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
        MediaDTO videoDTO = (id != null) ? mediaService.getMediaById(id) : new MediaDTO();
        model.addAttribute("videoDTO", videoDTO);

        List<CategoryDTO> listCategories = categoryService.getCategoriesForVideo();
        model.addAttribute("listCategories", listCategories);

        List<CountryDTO> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);

        List<CastDTO> cast = castService.getAllCasts();
        model.addAttribute("listCast", cast);

        return "admin_video_form";
    }

    @PostMapping("/video/save")
    public String createOrUpdateVideo(@ModelAttribute("videoDTO") MediaDTO videoDTO,
                              @RequestParam("logo") MultipartFile logo,
                              @RequestParam("selectedCategories") Long[] selectedCategories,
                              RedirectAttributes redirectAttributes) {
        try {
            if (videoDTO.getId() == null) {
                mediaService.createVideo(videoDTO, logo, selectedCategories);
                redirectAttributes.addFlashAttribute("success", "Create successfully!");
            } else {
                mediaService.updateVideo(videoDTO.getId(), videoDTO, logo, selectedCategories);
                redirectAttributes.addFlashAttribute("success", "Update Successfully!");
            }
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/videos";
    }

    @GetMapping("/video/delete/{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable Long id){
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
                              @RequestParam(name = "type", required = false) Integer type,
                              @RequestParam(name = "status", required = false) Integer status
    ) {
        model.addAttribute("title", "videos");
        return findPaginated(1, model, title,type,status);
    }

    @GetMapping("/videos/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "status", required = false) Integer status
    ) {
        int pageSize = 6;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Media> result = mediaRepository.searchMedia(title, 3, status, pageable);
        Page<Media> page = new PageImpl<>(result, pageable, mediaRepository.searchMedia1(title, 3, status).size());
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
