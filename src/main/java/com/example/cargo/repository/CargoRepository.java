package com.example.cargo.repository;

import com.example.cargo.model.CargoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<CargoModel, Long> {
    @Query("SELECT p FROM CargoModel p WHERE "
            + "(COALESCE(:cargoName, '') = '' OR LOWER(p.cargoName) LIKE LOWER(CONCAT('%', :cargoName, '%'))) AND "
            + "(COALESCE(:cargoContents, '') = '' OR LOWER(p.cargoContents) LIKE LOWER(CONCAT('%', :cargoContents, '%'))) AND "
            + "(COALESCE(:departureCity, '') = '' OR LOWER(p.departureCity) LIKE LOWER(CONCAT('%', :departureCity, '%'))) AND "
            + "(COALESCE(:departureDate, '') = '' OR LOWER(CAST(p.departureDate AS string)) LIKE LOWER(CONCAT('%', :departureDate, '%'))) AND "
            + "(COALESCE(:arrivalCity, '') = '' OR LOWER(p.arrivalCity) LIKE LOWER(CONCAT('%', :arrivalCity, '%'))) AND "
            + "(COALESCE(:arrivalDate, '') = '' OR LOWER(CAST(p.arrivalDate AS string)) LIKE LOWER(CONCAT('%', :arrivalDate, '%')))")
    List<CargoModel> searchByQuery(@Param("cargoName") String cargoName,
                                   @Param("cargoContents") String cargoContents,
                                   @Param("departureCity") String departureCity,
                                   @Param("departureDate") String departureDate,
                                   @Param("arrivalCity") String arrivalCity,
                                   @Param("arrivalDate") String arrivalDate);

    List<CargoModel> findAllByOrderByArrivalDateAsc();
    List<CargoModel> findAllByOrderByArrivalDateDesc();;
}
