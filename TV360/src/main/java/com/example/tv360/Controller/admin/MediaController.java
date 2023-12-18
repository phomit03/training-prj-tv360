package com.example.tv360.Controller.admin;

import com.example.tv360.DTO.CountryDTO;
import com.example.tv360.DTO.MediaDTO;
import com.example.tv360.Entity.Country;
import com.example.tv360.Repository.MediaRepository;
import com.example.tv360.Service.CountryService;
import com.example.tv360.Service.MediaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class MediaController {
    private final MediaService mediaService;
    private final CountryService countryService;
    private final MediaRepository mediaRepository;

    public MediaController(MediaService mediaService,CountryService countryService ,MediaRepository mediaRepository) {
        this.mediaService = mediaService;
        this.countryService = countryService;
        this.mediaRepository = mediaRepository;
    }

    @GetMapping("/medias")
    public String getAllMedia(Model model) {
        model.addAttribute("title", "Media");
        List<MediaDTO> media = mediaService.getAllMedias();
        model.addAttribute("media1", media);
        return "admin_media";
    }

    @GetMapping("/media/create")
    public String showCreateMedia(Model model){
        model.addAttribute("mediaDTO", new MediaDTO());
        List<CountryDTO> countries = countryService.getAllCountries();
        model.addAttribute("countries", countries);
        return "admin_media_create";
    }

    @PostMapping("/media/create/save")
    public String createMedia(@ModelAttribute MediaDTO mediaDTO, @RequestParam("logo") MultipartFile logo, RedirectAttributes redirectAttributes) {
        try {
            mediaService.createMedia(mediaDTO, logo);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/medias";
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
    public String updateMedia(@PathVariable Long id, @ModelAttribute("mediaDTO") MediaDTO mediaDTO,@RequestParam(value = "logo", required = false) MultipartFile logo, RedirectAttributes attributes){
        try {
            mediaService.updateMedia(mediaDTO,logo);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/medias";
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
