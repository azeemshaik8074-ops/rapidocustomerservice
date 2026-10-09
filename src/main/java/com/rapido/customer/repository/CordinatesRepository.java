package com.rapido.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.rapido.customer.entity.Cordinates;
@Repository
public interface CordinatesRepository extends JpaRepository<Cordinates, Integer>{

}
