package com.manajemen.asset.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.manajemen.asset.entity.Maintenance;

@Repository
public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    @Query("SELECT a FROM Maintenance a WHERE a.asset.id = :assetId")
    List<Maintenance> findByAssetId(@Param("assetId") Long assetId);
}
