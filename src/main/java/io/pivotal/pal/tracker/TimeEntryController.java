package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
    private TimeEntryRepository repository;
    private final Counter createCounter;
    private final Counter readCounter;
    private final Counter listCounter;
    private final Counter updateCounter;
    private final Counter deleteCounter;


    public TimeEntryController(TimeEntryRepository repository, MeterRegistry registry) {
        this.repository = repository;
        this.createCounter = registry.counter("TimeEntry.created");
        this.readCounter = registry.counter("TimeEntry.read");
        this.listCounter = registry.counter("TimeEntry.listed");
        this.updateCounter = registry.counter("TimeEntry.updated");
        this.deleteCounter = registry.counter("TimeEntry.deleted");
    }

    @PostMapping
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        createCounter.increment();
        return new ResponseEntity<>(repository.save(timeEntry), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") Long id) {
        Optional<TimeEntry> entry = repository.findById(id);
        if (entry.isPresent()) {
            readCounter.increment();
            return new ResponseEntity<>(entry.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        listCounter.increment();
        return new ResponseEntity<>(repository.findAll(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") Long id, @RequestBody TimeEntry entry) {
        entry.setId(id);
        TimeEntry timeEntry = repository.save(entry);
        if (timeEntry != null) {
            updateCounter.increment();
            return new ResponseEntity<>(timeEntry, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable("id") Long id) {
        repository.deleteById(id);
        deleteCounter.increment();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
