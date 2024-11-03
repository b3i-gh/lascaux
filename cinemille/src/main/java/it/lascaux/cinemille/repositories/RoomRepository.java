package it.lascaux.cinemille.repositories;

import it.lascaux.cinemille.domains.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}
