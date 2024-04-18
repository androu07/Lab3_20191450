package com.example.lab03.repository;

import com.example.lab03.entity.Veterinarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinariosRepository extends JpaRepository<Veterinarios, Integer> {
}
