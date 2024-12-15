package com.example.cargo.controller;

import com.example.cargo.model.CargoModel;
import com.example.cargo.model.User;
import com.example.cargo.service.CargoService;
import com.example.cargo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping("/moderate")
public class CargoController {

    private final CargoService cargoService;
    private final UserService userService;

    @Autowired
    public CargoController(CargoService cargoService,UserService userService) {
        this.cargoService = cargoService;
        this.userService = userService;
    }

    // Показываем форму для добавления грузов
    @GetMapping("/moderate/add")
    public String showAddCargoForm(Model model) {
        model.addAttribute("cargo", new CargoModel());
        return "redirect:/moderate";
    }

    // Добавляем новый груз в базу данных
    @PostMapping("/moderate/add")
    public String addCargo(@ModelAttribute CargoModel cargoModel, RedirectAttributes redirectAttributes) {
        cargoService.saveCargo(cargoModel);
        redirectAttributes.addFlashAttribute("message", "Представление успешно добавлена!");
        return "redirect:/moderate"; // Редирект на страницу со всеми грузами
    }

    // Показываем форму для редактирования груза
    @GetMapping("/moderate/edit/{id}")
    public String showEditCargoForm(@PathVariable Long id, Model model) {
        CargoModel cargoModel = cargoService.findCargoById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cargo Id: " + id));
        model.addAttribute("cargo", cargoModel);
        return "redirect:/moderate";
    }

    // Редактируем груз
    @PostMapping("/moderate/edit")
    public String editCargo(@ModelAttribute CargoModel cargoModel) {

        cargoService.saveCargo(cargoModel);
        return "redirect:/moderate"; // Возвращаемся к списку грузов
    }

    // Удаляем груз по ID
    @PostMapping("/moderate/delete/{id}")
    public String deleteCargo(@PathVariable Long id) {

        cargoService.deleteCargo(id);
        return "redirect:/moderate";
    }

    // Отображение гистограммы
    @GetMapping("/moderate/histogram")
    public String showHistogram(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        Map<LocalDate, Long> histogramData = cargoService.getCargoCountByDay();
        model.addAttribute("histogramData", histogramData);
        return "moderator/histogram";
    }

    // Поиск груза по информации
    @GetMapping("/moderate/filter")
    public String searchCargos(@RequestParam String searchInput,
                               @RequestParam(required = false) String cargoName,
                               @RequestParam(required = false) String cargoContents,
                               @RequestParam(required = false) String departureCity,
                               @RequestParam(required = false) String departureDate,
                               @RequestParam(required = false) String arrivalCity,
                               @RequestParam(required = false) String arrivalDate,
                               Model model) {
        String[] params = searchInput.split("\\s*,\\s*");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        if (params.length == 1) {
            cargoName = params[0];
        } else if (params.length == 2) {
            cargoName = params[0];
            cargoContents = params[1];
        } else if (params.length == 3) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
        } else if (params.length == 4) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
            departureDate = params[3];
        } else if (params.length == 5) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
            departureDate = params[3];
            arrivalCity = params[4];
        } else if (params.length == 6) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
            departureDate = params[3];
            arrivalCity = params[4];
            arrivalDate = params[5];
        }

        List<CargoModel> cargos = cargoService.searchCargos(cargoName, cargoContents, departureCity, departureDate, arrivalCity, arrivalDate);
        model.addAttribute("cargos", cargos);
        return "moderator/moderate";
    }

    // Сортировка даты по прибытию груза в город
    @GetMapping("/moderate/sort")
    public String sortCargosByDate(@RequestParam(defaultValue = "asc") String order, Model model) {
        List<CargoModel> cargoModels;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        if ("desc".equalsIgnoreCase(order)) {
            cargoModels = cargoService.findAllCargosSortedByDateTimeDesc();
        } else {
            cargoModels = cargoService.findAllCargosSortedByDateTime();
        }
        model.addAttribute("cargos", cargoModels);
        model.addAttribute("order", order);
        return "moderator/moderate";
    }


// для юзера фильтровка и поиск

    @RequestMapping("/cargolist/filter")
    public String filterCargos(@RequestParam String searchInput,
                               @RequestParam(required = false) String cargoName,
                               @RequestParam(required = false) String cargoContents,
                               @RequestParam(required = false) String departureCity,
                               @RequestParam(required = false) String departureDate,
                               @RequestParam(required = false) String arrivalCity,
                               @RequestParam(required = false) String arrivalDate,
                               Model model) {
        String[] params = searchInput.split("\\s*,\\s*");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }

        if (params.length == 1) {
            cargoName = params[0];
        } else if (params.length == 2) {
            cargoName = params[0];
            cargoContents = params[1];
        } else if (params.length == 3) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
        } else if (params.length == 4) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
            departureDate = params[3];
        } else if (params.length == 5) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
            departureDate = params[3];
            arrivalCity = params[4];
        } else if (params.length == 6) {
            cargoName = params[0];
            cargoContents = params[1];
            departureCity = params[2];
            departureDate = params[3];
            arrivalCity = params[4];
            arrivalDate = params[5];
        }

        List<CargoModel> cargos = cargoService.searchCargos(cargoName, cargoContents, departureCity, departureDate, arrivalCity, arrivalDate);
        model.addAttribute("cargos", cargos);
        return "cargolist";
    }

    // Сортировка даты по прибытию груза в город
    @GetMapping("/cargolist/sort")
    public String sortCargosByDateUser(@RequestParam(defaultValue = "asc") String order, Model model) {
        List<CargoModel> cargoModels;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findUserByUserName(username).orElse(null);
            if (user != null) {
                model.addAttribute("username", user.getUserName());
            }
        }
        if ("desc".equalsIgnoreCase(order)) {
            cargoModels = cargoService.findAllCargosSortedByDateTimeDesc();
        } else {
            cargoModels = cargoService.findAllCargosSortedByDateTime();
        }
        model.addAttribute("cargos", cargoModels);
        model.addAttribute("order", order);
        return "cargolist";
    }
}

