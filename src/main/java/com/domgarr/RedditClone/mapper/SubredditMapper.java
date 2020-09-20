package com.domgarr.RedditClone.mapper;

import com.domgarr.RedditClone.dto.SubredditDto;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.Subreddit;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel="spring")
public interface SubredditMapper {
    @Mapping(target="numberOfPosts", expression = "java(mapPosts(subreddit.getPosts()))")
    SubredditDto mapSubredditToSubredditDto(Subreddit subreddit);

    default Integer mapPosts(List<Post> numberOfPosts){
        return numberOfPosts.size();
    }

    @InheritInverseConfiguration
    @Mapping(target = "posts", ignore = true)
    Subreddit mapSubredditDtoToSubreddit(SubredditDto subredditDto);
}

