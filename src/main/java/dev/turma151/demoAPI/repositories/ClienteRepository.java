package dev.turma151.demoAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.turma151.demoAPI.models.ClienteModel;

public interface ClienteRepository extends JpaRepository<ClienteModel, Integer> {
}
