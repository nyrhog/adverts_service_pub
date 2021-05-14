package com.project.dao;

import com.project.entity.Message;
import com.project.entity.MessageId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, MessageId> {
}
