package com.example.tv360.controller.admin;

import com.example.tv360.dto.CastDTO;
import com.example.tv360.entity.Cast;
import com.example.tv360.repository.CastRepository;
import com.example.tv360.service.CastService;
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
        return "admin_cast_form";
    }

    @PostMapping("/cast/create/save")
    public String createCast(@ModelAttribute CastDTO castDTO, RedirectAttributes redirectAttributes) {
        try {
            castService.createCast(castDTO);
            redirectAttributes.addFlashAttribute("success", "Create successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to create!");
        }
        return "redirect:/admin/casts";
    }

    @GetMapping("/cast/update/{id}")
    public String showUpdateCast(@PathVariable Long id, Model model){
        CastDTO castDTO = castService.getCastById(id);
        if (castDTO == null){
            return "redirect:/admin/cast";
        }

        model.addAttribute("castDTO", castDTO);
        return "admin_cast_form";
    }

    @PostMapping("/cast/update/{id}")
    public String updateCast(@PathVariable Long id, @ModelAttribute("castDTO") CastDTO castDTO, RedirectAttributes attributes){
        try {
            castService.updateCast(id ,castDTO);
            attributes.addFlashAttribute("success", "Update Successfully!");
        }catch (Exception e){
            e.printStackTrace();
            attributes.addFlashAttribute("error", "Failed to update");
        }
        return "redirect:/admin/casts";
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

    @GetMapping("/casts")
    public String getAllCasts(Model model,
                              @RequestParam(name = "fullName", required = false) String fullName,
                              @RequestParam(name = "type", required = false) Integer type,
                              @RequestParam(name = "status", required = false) Integer status
    ) {
        model.addAttribute("title", "Cast");
        return findPaginated(1, model, fullName,type,status);
    }

    @GetMapping("/casts/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo,
                                Model model,
                                @RequestParam(name = "fullName", required = false) String fullName,
                                @RequestParam(name = "type", required = false) Integer type,
                                @RequestParam(name = "status", required = false) Integer status
    ) {
        int pageSize = 6;

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        List<Cast> result = castRepository.searchCasts(fullName,type,status, pageable);
        Page<Cast> page = new PageImpl<>(result, pageable,castRepository.searchCasts1(fullName,type, status).size());
        List<Cast> casts = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("casts", casts);
        model.addAttribute("fullName", fullName);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        return "admin_cast";
    }
}
