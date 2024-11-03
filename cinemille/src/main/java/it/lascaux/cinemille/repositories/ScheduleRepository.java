package it.lascaux.cinemille.repositories;

import it.lascaux.cinemille.domains.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    @Query("SELECT s FROM Schedule s WHERE s.startDate <= :endDate AND s.endDate >= :startDate")
    List<Schedule> findByDateRange(LocalDate startDate, LocalDate endDate);

}

