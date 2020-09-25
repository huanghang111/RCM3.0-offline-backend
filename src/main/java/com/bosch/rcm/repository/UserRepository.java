package com.bosch.rcm.repository;

import com.bosch.rcm.domain.Authority;
import com.bosch.rcm.domain.User;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findOneByLoginName(String login);

    Page<User> findAllByAuthoritiesNotContains(Set<Authority> authorities, Pageable pageable);

    List<User> findAllByAuthoritiesContains(Set<Authority> authorities);

    Page<User> findAllByLoginNameNot(Pageable pageable, String login);
}
