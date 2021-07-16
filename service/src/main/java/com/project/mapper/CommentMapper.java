package com.project.mapper;

import com.project.dto.CommentDto;
import com.project.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface CommentMapper {

    @Mapping(target = "creatorProfileId", source = "comment.profile.id")
    CommentDto commentToCommentDto(Comment comment);

}
