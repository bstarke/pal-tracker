package test.pivotal.pal.tracker;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.MockClock;
import io.micrometer.core.instrument.simple.SimpleConfig;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryController;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class TimeEntryControllerTest {
    private TimeEntryRepository timeEntryRepository;
    private TimeEntryController controller;

    @Before
    public void setUp() throws Exception {
        MeterRegistry meterRegistry = new SimpleMeterRegistry(SimpleConfig.DEFAULT, new MockClock());
        timeEntryRepository = mock(TimeEntryRepository.class);
        controller = new TimeEntryController(timeEntryRepository, meterRegistry);
    }

    @Test
    public void testCreate() throws Exception {
        TimeEntry timeEntryToCreate = new TimeEntry(123L, 456L, LocalDate.parse("2017-01-08"), 8);
        TimeEntry expectedResult = new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8);
        doReturn(expectedResult)
            .when(timeEntryRepository)
            .save(any(TimeEntry.class));


        ResponseEntity response = controller.create(timeEntryToCreate);


        verify(timeEntryRepository).save(timeEntryToCreate);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(expectedResult);
    }

    @Test
    public void testRead() throws Exception {
        Optional<TimeEntry> returned = Optional.of(new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8));
        doReturn(returned)
            .when(timeEntryRepository)
            .findById(1L);
        TimeEntry expected = returned.get();
        ResponseEntity<TimeEntry> response = controller.read(1L);

        verify(timeEntryRepository).findById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testRead_NotFound() throws Exception {
        doReturn(Optional.empty())
            .when(timeEntryRepository)
            .findById(1L);

        ResponseEntity<TimeEntry> response = controller.read(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testList() throws Exception {
        List<TimeEntry> expected = asList(
            new TimeEntry(1L, 123L, 456L, LocalDate.parse("2017-01-08"), 8),
            new TimeEntry(2L, 789L, 321L, LocalDate.parse("2017-01-07"), 4)
        );
        doReturn(expected).when(timeEntryRepository).findAll();

        ResponseEntity<List<TimeEntry>> response = controller.list();

        verify(timeEntryRepository).findAll();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate() throws Exception {
        TimeEntry expected = new TimeEntry(1L, 987L, 654L, LocalDate.parse("2017-01-07"), 4);
        doReturn(expected)
            .when(timeEntryRepository)
            .save(any(TimeEntry.class));

        ResponseEntity response = controller.update(1L, expected);

        verify(timeEntryRepository).save(expected);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(expected);
    }

    @Test
    public void testUpdate_NotFound() throws Exception {
        doReturn(null)
            .when(timeEntryRepository)
            .save(any(TimeEntry.class));

        ResponseEntity response = controller.update(1L, new TimeEntry());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDelete() throws Exception {
        ResponseEntity<TimeEntry> response = controller.delete(1L);
        verify(timeEntryRepository).deleteById(1L);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
