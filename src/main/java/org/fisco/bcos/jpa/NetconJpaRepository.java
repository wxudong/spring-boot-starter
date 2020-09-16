package org.fisco.bcos.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetconJpaRepository extends JpaRepository<NetconData, String> {

}
