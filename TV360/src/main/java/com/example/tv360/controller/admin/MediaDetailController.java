package com.example.tv360.controller.admin;

import com.example.tv360.dto.MediaDTO;
import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.dto.response.MediaDetailResponse;
import com.example.tv360.entity.MediaDetail;
import com.example.tv360.repository.MediaDetailRepository;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.MediaDetailService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MediaDetailController {
    private final MediaDetailService mediaDetailService;
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;
    private final MediaDetailRepository mediaDetailRepository;

    public MediaDetailController(MediaDetailRepository mediaDetailRepository ,MediaDetailService mediaDetailService, MediaService mediaService, MediaRepository mediaRepository) {
        this.mediaDetailRepository = mediaDetailRepository;
        this.mediaDetailService = mediaDetailService;
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

//    @GetMapping("/media-details")
//    public String getAllMediaDetails(Model model) {
//        model.addAttribute("title", "Media Detail");
//        List<MediaDetailDTO> mediaDetails = mediaDetailService.getAllMediaDetails();
//        model.addAttribute("mediaDetails", mediaDetails);
//        return "admin_media_detail";
//    }

    @GetMapping("/media-detail/create")
    public String showCreateMediaDetail(Model model){
        List<MediaDTO> mediaList = mediaService.getAllMedias();
        model.addAttribute("mediaList", mediaList);

        model.addAttribute("mediaDetailDTO", new MediaDetailDTO());
        return "admin_media_detail_create";
    }

    @PostMapping("/media-detail/create/save")
    public String createMediaDetail(@ModelAttribute MediaDetailDTO mediaDetailDTO, RedirectAttributes redirectAttributes) {
        try {
            mediaDetailService.createMediaDetail(mediaDetailDTO);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/media-details";
    }

    @GetMapping("/media-detail/update/{id}")
    public String showUpdateMediaDetail(@PathVariable Long id, Model model){
        MediaDetailDTO mediaDetailDTO = mediaDetailService.getMediaDetailById(id);
        model.addAttribute("mediaDetailDTO", mediaDetailDTO);

        List<MediaDTO> mediaList = mediaService.getAllMedias();
        model.addAttribute("mediaList", mediaList);

        if (mediaDetailDTO == null){
            return "redirect:/admin/media-details";
        }

        return "admin_media_detail_update";
    }

    @PostMapping("/media-detail/update/{id}")
    public String updateMediaDetail(@PathVariable Long id, @ModelAttribute("mediaDetailDTO") MediaDetailDTO mediaDetailDTO, RedirectAttributes attributes){
        try {
            mediaDetailService.updateMediaDetail(id, mediaDetailDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/media-details";
    }

    @GetMapping("/media-detail/delete/{id}")
    public ResponseEntity<String> deleteMediaDetail(@PathVariable Long id){
        try {
            mediaDetailService.softDeleteMediaDetail(id);
            return ResponseEntity.ok("Delete media detail successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }




    @GetMapping("/test")
    public ResponseEntity<List<MediaDetailResponse>> getMediaDetailsClient() {
        try {
            List<MediaDetailResponse> mediaDetails = mediaDetailService.getMediaDetailsClient();
            return ResponseEntity.ok(mediaDetails);
        } catch (Exception e) {
            // Handle exceptions or return an appropriate response
            return ResponseEntity.status(500).body(null);
        }
    }
    //phan trang
    @GetMapping("/media-details")
    public String getAllPlayers(Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "quality", required = false) String quality,
                                @RequestParam(name = "episode", required = false) Integer episode,
                                @RequestParam(name = "status", required = false) Integer  status
    ) {
//        List<MediaDetailDTO> mediaDetails = mediaDetailService.getAllMediaDetails();
//
//        model.addAttribute("mediaDetails", mediaDetails);
        model.addAttribute("title", "Media Details");
        return findPaginated(1, model, title, quality,episode,status);
    }

    @GetMapping("/media-details/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "title", required = false) String title,
                                @RequestParam(name = "quality", required = false) String quality,
                                @RequestParam(name = "episode", required = false) Integer episode,
                                @RequestParam(name = "status", required = false) Integer  status
    ) {
        int pageSize = 11;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<MediaDetail> result = mediaDetailRepository.searchMediaDetails(title, quality,episode,status, pageable);
        Page<MediaDetail> page = new PageImpl<>(result, pageable,mediaDetailRepository.searchMediaDetails1(title, quality,episode,status).size());
        List<MediaDetail> mediaDetails = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("mediaDetails", mediaDetails);
        model.addAttribute("title", title);
        model.addAttribute("quality", quality);
        model.addAttribute("episode", episode);
        model.addAttribute("status", status);
        return "admin_media_detail";
    }

}
