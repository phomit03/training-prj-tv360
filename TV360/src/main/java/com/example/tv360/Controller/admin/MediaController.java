package com.example.tv360.Controller.admin;

import com.example.tv360.DTO.MediaDTO;
import com.example.tv360.Repository.MediaRepository;
import com.example.tv360.Service.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/admin")
public class MediaController {
    private final MediaService mediaService;
    private final MediaRepository mediaRepository;

    public MediaController(MediaService mediaService, MediaRepository mediaRepository) {
        this.mediaService = mediaService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping("/media/create")
    public String showCreateMedia(Model model){
        model.addAttribute("mediaDTO", new MediaDTO());
        return "admin_media_create";
    }

    @PostMapping("/media/create/save")
    public String createMedia(@ModelAttribute MediaDTO mediaDTO){
        try {
            mediaService.createMedia(mediaDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/media";
    }

    @GetMapping("/media/update/{id}")
    public String showUpdateMedia(@PathVariable Long id, Model model){
        MediaDTO mediaDTO = mediaService.getMediaById(id);
        if (mediaDTO == null){
            return "redirect:/admin/media";
        }

        model.addAttribute("mediaDTO", mediaDTO);
        return "admin_media_update";
    }

    @PostMapping("/media/update/{id}")
    public String updateMedia(@PathVariable Long id, @ModelAttribute("mediaDTO") MediaDTO mediaDTO, RedirectAttributes attributes){
        try {
            mediaService.updateMedia(mediaDTO);
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
}
