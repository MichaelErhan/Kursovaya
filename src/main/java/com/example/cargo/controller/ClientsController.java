package com.example.cargo.controller;

import com.example.cargo.model.BlogPost;
import com.example.cargo.model.Clients;
import com.example.cargo.model.User;
import com.example.cargo.service.ClientsService;
import com.example.cargo.service.UserService;
import com.example.cargo.util.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientsController {

    private final ClientsService clientsService;
    private final UserService userService;

    public ClientsController(ClientsService clientsService, UserService userService) {
        this.clientsService = clientsService;
        this.userService = userService;
    }

    @GetMapping("/clients")
    public String getClients(Model model) {
        List<Clients> clients = clientsService.findAllClients();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        model.addAttribute("clients", clients);
        return "clients/clients";
    }

    @GetMapping("/add")
    public String showAddClientsForm(Model model) {
        model.addAttribute("clients", new Clients());
        return "redirect:/clients/clients";
    }

    @PostMapping("/add")
    public String addClients(@ModelAttribute Clients clients) {
        clientsService.saveClients(clients);
        return "redirect:/clients/clients";
    }

    @GetMapping("/edit/{id}")
    public String showEditClientsForm(@PathVariable Long id, Model model) {
        Clients clients = clientsService.findClientsById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid clients Id: " + id));
        model.addAttribute("clients", clients);
        return "redirect:/clients/clients";
    }

    @PostMapping("/edit")
    public String editClients(@ModelAttribute Clients clients) {
        clientsService.saveClients(clients);
        return "redirect:/clients/clients";
    }

    @PostMapping("/delete/{id}")
    public String deleteClients(@PathVariable Long id) {
        clientsService.deleteClients(id);
        return "redirect:/clients/clients";
    }

    @GetMapping("/admin")
    public String showAdminPanel(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        List<Clients> clients = clientsService.findAllClients();
        model.addAttribute("clients", clients);
        return "clients/clients";
    }

    @GetMapping("/")
    public String showBlogHome(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null && user.getRole().equals(Role.ADMIN) || user.getRole().equals(Role.MODERATOR)) {
                return "redirect:/clients/clients";
            }
            model.addAttribute("username", user.getUserName());
        }
        List<Clients> clients = clientsService.findAllClients();
        model.addAttribute("clients", clients);
        return "clients/clients";
    }
}
