package com.perfumestore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "contents")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType type;

    @ManyToMany(mappedBy = "contents", fetch = FetchType.LAZY)
    private List<Perfume> perfumes;

    public enum ContentType {
        MEYVE, BAHARAT, ODUNSU, FRESH, AMBER
    }
}
