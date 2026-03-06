package com.robostore;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyUserRepository extends JpaRepository<myUser, Long> {

    Optional<myUser> findByEmail(String email);

    Optional<myUser> findByName(String name);

    List<myUser> findByPhonenumber(String phonenumber);

    boolean existsByLevel(int level);
}
