package com.example.cargo.repository;

import com.example.cargo.model.BlogPost;
import com.example.cargo.model.Clients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientsRepository extends JpaRepository<Clients, Long> {
    @Query("SELECT p FROM Clients p WHERE "
            + "(COALESCE(:FIO, '') = '' OR LOWER(p.FIO) LIKE LOWER(CONCAT('%', :FIO, '%'))) AND "
            + "(COALESCE(:BrandNumberAuto, '') = '' OR LOWER(CAST(p.BrandNumberAuto AS string)) LIKE LOWER(CONCAT('%', :BrandNumberAuto, '%'))) AND "
            + "(COALESCE(:Number, '') = '' OR LOWER(p.Number) LIKE LOWER(CONCAT('%', :Number, '%')))")
    List<Clients> searchByQuery(@Param("FIO") String FIO,
                                @Param("BrandNumberAuto") String BrandNumberAuto,
                                @Param("Number") String Number);
}
