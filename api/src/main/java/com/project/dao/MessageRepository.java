package com.project.dao;

import com.project.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    @Query(value =
            """
            SELECT username FROM users
            join profiles p on p.id = users.profile_id
            join messages m on p.id = m.profile_id
            where m.id = ?1;
            """,
            nativeQuery = true)
    String getMessageSenderUsername(Long id);
}
