package com.example.tv360.controller.user;

import com.example.tv360.entity.User;
import com.example.tv360.service.EmailSenderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmailController {
    private final EmailSenderService emailSenderService;

    public EmailController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/send-mail")
    public String RegisterReceiveInformation(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            emailSenderService.sendEmail(email, user);
            redirectAttributes.addFlashAttribute("success", "Mail sent successfully!");
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Mail sent failed...");
        }
        return "redirect:/home";
    }
}
