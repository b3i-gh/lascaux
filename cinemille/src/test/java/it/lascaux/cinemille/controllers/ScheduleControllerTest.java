package it.lascaux.cinemille.controllers;

import it.lascaux.cinemille.domains.Movie;
import it.lascaux.cinemille.domains.Room;
import it.lascaux.cinemille.domains.Schedule;
import it.lascaux.cinemille.repositories.MovieRepository;
import it.lascaux.cinemille.repositories.RoomRepository;
import it.lascaux.cinemille.repositories.ScheduleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;


import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;



@SpringBootTest
@AutoConfigureMockMvc
public class ScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    public void setup() {
        scheduleRepository.deleteAll();
        // Insert mock movie, room and schedule
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Mock title 1");
        movieRepository.save(movie);

        Room room = new Room();
        room.setId(1L);
        room.setRoomNumber("Room 1");
        room.setCapacity(150);
        room.setIMAX(true);
        roomRepository.save(room);

        Schedule schedule = new Schedule();
        schedule.setId(1L);
        schedule.setMovie(movie);
        schedule.setRoom(room);
        schedule.setStartDate(LocalDate.of(2024, 9, 30));
        schedule.setEndDate(LocalDate.of(2024, 10, 3));
        scheduleRepository.save(schedule);
    }

    @Test
    public void testGetSchedulesWithDateRange() throws Exception {
        mockMvc.perform(get("/api/schedules")
                        .param("startDate", "2024-09-30")
                        .param("endDate", "2024-10-03"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].movie.title").value("Mock title 1"))
                .andExpect(jsonPath("$.data[0].room.roomNumber").value("Room 1"))
                .andExpect(jsonPath("$.data[0].startDate").value("2024-09-30"))
                .andExpect(jsonPath("$.data[0].endDate").value("2024-10-03"));
    }
}

