package com.boot.backend.Sweet.Shop.Management.System.repository;

import com.boot.backend.Sweet.Shop.Management.System.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{

    Optional<User> findByEmail(String email);


}
