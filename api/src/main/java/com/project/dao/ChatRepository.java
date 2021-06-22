package com.project.dao;

import com.project.entity.Chat;
import com.project.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = """
            SELECT DISTINCT id, created
            FROM chat
                     JOIN profiles_chats pc on chat.id = pc.chat_id
            where chat_id IN (select chat_id
                              from ((SELECT profile_id, chat_id
                                     from chat
                                              join profiles_chats pc on chat.id = pc.chat_id
                                     where pc.profile_id = ?1)
                                    UNION
                                    (SELECT profile_id, chat_id
                                     from chat
                                              join profiles_chats pc on chat.id = pc.chat_id
                                     where pc.profile_id = ?2)) as c
                              GROUP BY chat_id
                              HAVING COUNT(chat_id) = 2);
            """,
            nativeQuery = true)
    Chat getChatByProfilesIdIn(Long creator, Long recipient);

}
