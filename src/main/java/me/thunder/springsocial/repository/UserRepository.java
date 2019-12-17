package me.thunder.springsocial.repository;

import me.thunder.springsocial.model.User;

import java.util.Optional;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);
}