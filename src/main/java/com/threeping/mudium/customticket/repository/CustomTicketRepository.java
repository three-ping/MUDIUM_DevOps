package com.threeping.mudium.customticket.repository;

import com.threeping.mudium.customticket.aggregate.entity.CustomTicketEntity;
import com.threeping.mudium.user.aggregate.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomTicketRepository extends JpaRepository<CustomTicketEntity, Long> {
    List<CustomTicketEntity> findAllByUser(UserEntity user);
}
