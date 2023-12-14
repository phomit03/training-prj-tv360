package com.example.tv360.Controller.admin;

import com.example.tv360.DTO.CastDTO;
import com.example.tv360.Repository.CastRepository;
import com.example.tv360.Service.CastService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.EntityNotFoundException;

@Controller
@RequestMapping("/admin")
public class CastController {
    private final CastService castService;
    private final CastRepository castRepository;

    public CastController(CastService castService, CastRepository castRepository) {
        this.castService = castService;
        this.castRepository = castRepository;
    }

    @GetMapping("/cast/create")
    public String showCreateCast(Model model){
        model.addAttribute("castDTO", new CastDTO());
        return "admin_cast_create";
    }

    @PostMapping("/cast/create/save")
    public String createCast(@ModelAttribute CastDTO castDTO){
        try {
            castService.createCast(castDTO);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/admin/cast";
    }

    @GetMapping("/cast/update/{id}")
    public String showUpdateCast(@PathVariable Long id, Model model){
        CastDTO castDTO = castService.getCastById(id);
        if (castDTO == null){
            return "redirect:/admin/cast";
        }

        model.addAttribute("castDTO", castDTO);
        return "admin_cast_update";
    }

    @PostMapping("/cast/update/{id}")
    public String updateCast(@PathVariable Long id, @ModelAttribute("castDTO") CastDTO castDTO, RedirectAttributes attributes){
        try {
            castService.updateCast(castDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/cast";
    }

    @GetMapping("/cast/delete/{id}")
    public ResponseEntity<String> deleteCast(@PathVariable Long id){
        try {
            castService.softDeleteCast(id);
            return ResponseEntity.ok("Delete cast successfully");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error!");
        }
    }
}
