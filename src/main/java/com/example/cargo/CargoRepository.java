package com.example.cargo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CargoRepository extends JpaRepository<Cargo, Long> {
    @Query("SELECT p FROM Cargo p WHERE CONCAT(p.cname, '', p.content, '', p.city1, '', p.city2, '', p.date1, '', p.date2, '') LIKE %?1%")
    List<Cargo> search(String keyword);

    @Query("SELECT p FROM Cargo p ORDER BY STR_TO_DATE(p.date2, '%d.%m.%Y')")
    List<Cargo> sortAll();

    @Query("SELECT COUNT(1) FROM Cargo p WHERE p.date2 = :keyword")
    int countByDay(@Param("keyword") String keyword);
}
