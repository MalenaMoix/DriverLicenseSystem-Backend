package com.tp.driverlicensesystem.repository;

import com.tp.driverlicensesystem.model.Owner;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IOwnerRepo extends JpaRepository<Owner, Integer> {

}
