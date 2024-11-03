package it.lascaux.cinemille.repositories;

import it.lascaux.cinemille.domains.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {}


