package com.example.tv360.controller.admin;

import com.example.tv360.dto.MediaDTO;
import com.example.tv360.dto.MediaDetailDTO;
import com.example.tv360.repository.MediaRepository;
import com.example.tv360.service.MediaDetailService;
import com.example.tv360.service.MediaService;
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

    public MediaDetailController(MediaDetailService mediaDetailService, MediaService mediaService, MediaRepository mediaRepository) {
        this.mediaDetailService = mediaDetailService;
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping("/media-details")
    public String getAllMediaDetails(Model model) {
        model.addAttribute("title", "Media Detail");
        List<MediaDetailDTO> mediaDetails = mediaDetailService.getAllMediaDetails();
        model.addAttribute("mediaDetails", mediaDetails);
        return "admin_media_detail";
    }

    @GetMapping("/media-detail/create")
    public String showCreateMediaDetail(Model model){
        List<MediaDTO> mediaList = mediaService.getAllMedias();
        model.addAttribute("mediaList", mediaList);

        model.addAttribute("mediaDetailDTO", new MediaDetailDTO());
        return "admin_media";
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
        return "redirect:/admin/media";
    }

    @GetMapping("/media-detail/update/{id}")
    public String showUpdateMediaDetail(@PathVariable Long id, Model model){
        MediaDetailDTO mediaDetailDTO = mediaDetailService.getMediaDetailById(id);
        if (mediaDetailDTO == null){
            return "redirect:/admin/media-details";
        }

        model.addAttribute("mediaDetailDTO", mediaDetailDTO);
        return "admin_media_detail_update";
    }

    @PostMapping("/media-detail/update/{id}")
    public String updateMediaDetail(@PathVariable Long id, @ModelAttribute("mediaDetailDTO") MediaDetailDTO mediaDetailDTO, RedirectAttributes attributes){
        try {
            mediaDetailService.updateMediaDetail(mediaDetailDTO);
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
}
