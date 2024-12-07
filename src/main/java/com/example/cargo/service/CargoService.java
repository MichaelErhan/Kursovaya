package com.example.cargo.service;

import com.example.cargo.model.CargoModel;
import com.example.cargo.repository.CargoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CargoService {

    private final CargoRepository cargoRepository;

    @Autowired
    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    // Сохраняем груз в базе данных
    public void saveCargo(CargoModel cargoModel) {
        cargoRepository.save(cargoModel);
    }

    // Находим груз по ID
    public Optional<CargoModel> findCargoById(Long id) {
        return cargoRepository.findById(id);
    }

    // Находим все грузы, отсортированные по дате
    public List<CargoModel> findAllCargosSortedByDateTime() {
        return cargoRepository.findAllByOrderByArrivalDateAsc(); // Сортировка по возрастанию
    }

    public List<CargoModel> findAllCargosSortedByDateTimeDesc() {
        return cargoRepository.findAllByOrderByArrivalDateDesc(); // Сортировка по убыванию
    }


    // Удалить груз из таблицы по ID
    public void deleteCargo(Long id) {
        cargoRepository.deleteById(id);
    }


    // Поиск груза
    public List<CargoModel> searchCargos(String cargoName, String cargoContents, String departureCity, String departureDate, String arrivalCity, String arrivalDate) {
        return cargoRepository.searchByQuery(cargoName, cargoContents, departureCity, departureDate, arrivalCity, arrivalDate);
    }

    // Счётчик грузов в день
    public Map<LocalDate, Long> getCargoCountByDay() {
        List<CargoModel> sessions = cargoRepository.findAll();
        return sessions.stream()
                .collect(Collectors.groupingBy(
                        session -> session.getDepartureDate(),
                        Collectors.counting()
                ));
    }
}
