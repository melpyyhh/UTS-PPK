package com.manajemen.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.manajemen.asset.entity.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {
    @Query("SELECT a FROM Asset a WHERE a.place.id = :placeId")
    List<Asset> findByPlaceId(@Param("placeId") Long placeId);

    @Query("SELECT a FROM Asset a WHERE " +
            "a.nama LIKE %:keyword% OR " +
            "a.tipe LIKE %:keyword% OR " +
            "a.kondisi LIKE %:keyword%")
    List<Asset> findByKeyword(@Param("keyword") String keyword);
}
