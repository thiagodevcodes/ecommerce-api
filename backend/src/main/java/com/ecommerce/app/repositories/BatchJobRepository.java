package com.ecommerce.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.app.models.BatchJob;

@Repository
public interface BatchJobRepository extends JpaRepository<BatchJob, Long> {

}
