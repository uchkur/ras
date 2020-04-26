package ru.sbrf.lab.repository;

import org.springframework.stereotype.Component;
import ru.sbrf.lab.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}


