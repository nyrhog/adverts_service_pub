package com.project.mapper;

import com.project.dto.CommentDto;
import com.project.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CommentMapper {

    @Mapping(target = "profileName", source = "comment.profile.name")
    CommentDto toCommentDto(Comment comment);

}
