package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.dto.SubredditDto;
import com.domgarr.RedditClone.model.Subreddit;
import com.domgarr.RedditClone.repository.SubredditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {
    private final SubredditRepository subredditRepository;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit = mapSubredditDtoToSubreddit(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subredditDto;

    }

    private Subreddit mapSubredditDtoToSubreddit(SubredditDto subredditDto) {
        //Builder pattern is used (Lombok implements), later will use MapStruct.
        return Subreddit.builder().name(subredditDto.getName())
                .description(subredditDto.getDescription())
                .build();
    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll().stream().map(this::mapSubredditToSubredditDto).collect(Collectors.toList());
    }

    private SubredditDto mapSubredditToSubredditDto(Subreddit subreddit){
        return SubredditDto.builder().name(subreddit.getName())
                .id(subreddit.getId())
                .description(subreddit.getDescription())
                .numberOfPosts(subreddit.getPosts().size())
                .build();
    }
}
