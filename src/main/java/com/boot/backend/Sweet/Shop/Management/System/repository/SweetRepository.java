package com.boot.backend.Sweet.Shop.Management.System.repository;

import com.boot.backend.Sweet.Shop.Management.System.entity.Sweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SweetRepository extends JpaRepository<Sweet,Long> {

    List<Sweet> findByNameContainingIgnoreCase(String name);

    List<Sweet> findByCategoryIgnoreCase(String category);

    List<Sweet> findByPriceBetween(double min, double max);

    @Query("SELECT COUNT(s) FROM Sweet s")
    long countTotalSweets();

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Sweet s")
    long sumTotalStock();

    @Query("SELECT COUNT(s) FROM Sweet s WHERE s.quantity < :threshold")
    long countLowStock(int threshold);




}
