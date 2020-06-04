package ru.megafon.request.handler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.megafon.request.handler.model.Request;

@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

}
