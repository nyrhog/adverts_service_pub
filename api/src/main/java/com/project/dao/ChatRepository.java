package com.project.dao;

import com.project.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(value = """
            SELECT DISTINCT id, created
            FROM chats
                     JOIN profiles_chats pc on chats.id = pc.chat_id
            where chat_id IN (select chat_id
                              from ((SELECT profile_id, chat_id
                                     from chats
                                              join profiles_chats pc on chats.id = pc.chat_id
                                     where pc.profile_id = ?1)
                                    UNION
                                    (SELECT profile_id, chat_id
                                     from chats
                                              join profiles_chats pc on chats.id = pc.chat_id
                                     where pc.profile_id = ?2)) as c
                              GROUP BY chat_id
                              HAVING COUNT(chat_id) = 2);
            """,
            nativeQuery = true)
    Chat getChatByProfilesIdIn(Long creator, Long recipient);

}
