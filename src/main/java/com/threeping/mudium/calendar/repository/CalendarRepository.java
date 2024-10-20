package com.threeping.mudium.calendar.repository;

import com.threeping.mudium.calendar.aggregate.entity.CalendarTheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarTheme, Long> {
}
