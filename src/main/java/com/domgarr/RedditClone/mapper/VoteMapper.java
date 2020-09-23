package com.domgarr.RedditClone.mapper;

import com.domgarr.RedditClone.dto.VoteDto;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VoteMapper {


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "user")
    @Mapping(target = "post", source = "post")
    Vote mapVoteDtoToVote(VoteDto voteDto, User user, Post post);
}
