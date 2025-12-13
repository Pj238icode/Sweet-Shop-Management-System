package com.boot.backend.Sweet.Shop.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sweets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String category;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;


    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;






}
