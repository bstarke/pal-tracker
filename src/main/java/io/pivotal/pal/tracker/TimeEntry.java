package io.pivotal.pal.tracker;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "time_entries")
public class TimeEntry {
    @Id
    @GeneratedValue
    private Long id;
    private Long projectId;
    private Long userId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Integer hours;

    public TimeEntry() {
        super();
    }

    public TimeEntry(long projectId, long userId, LocalDate date, int hours) {
        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
    }

    public TimeEntry(Long id, Long projectId, Long userId, LocalDate date, Integer hours) {
        this.id = id;
        this.projectId = projectId;
        this.userId = userId;
        this.date = date;
        this.hours = hours;
    }
}
