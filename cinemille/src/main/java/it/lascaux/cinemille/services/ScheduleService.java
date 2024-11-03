package it.lascaux.cinemille.services;

import it.lascaux.cinemille.domains.ApiResponse;
import it.lascaux.cinemille.domains.Schedule;
import it.lascaux.cinemille.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ScheduleService {
    @Autowired
    private ScheduleRepository scheduleRepository;

    public ApiResponse<List<Schedule>> getSchedules(LocalDate startDate, LocalDate endDate) {
        ApiResponse<List<Schedule>> response;
        if(startDate != null && endDate != null){
            response = new ApiResponse<>(scheduleRepository.findByDateRange(startDate, endDate));
        } else
            response = new ApiResponse<>(scheduleRepository.findAll());

        return response;
    }
}

