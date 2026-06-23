/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package dev.turma151.demoAPI.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.turma151.demoAPI.models.CountryModel;

public interface CountryRepository extends JpaRepository<CountryModel, String>{
}
