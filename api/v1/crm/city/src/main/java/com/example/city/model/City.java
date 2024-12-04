package com.example.city.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cities")
public class City {
    @Id
    @Column(name = "id", nullable = false, length = 40)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "int_id", nullable = false)
    private Long intId;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "region_id", nullable = false, length = 40)
    private String regionId;

    @Column(name = "country_id", nullable = false, length = 40)
    private String countryId;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "zoom", columnDefinition = "integer default 10")
    private Integer zoom;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
