package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.dto.SubredditDto;
import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.mapper.SubredditMapper;
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
    private final SubredditMapper subredditMapper;

    @Transactional
    public SubredditDto save(SubredditDto subredditDto){
        Subreddit subreddit = subredditMapper.mapSubredditDtoToSubreddit(subredditDto);
        Subreddit save = subredditRepository.save(subreddit);
        subredditDto.setId(save.getId());
        return subredditDto;

    }

    @Transactional(readOnly = true)
    public List<SubredditDto> getAll() {
        return subredditRepository.findAll()
                .stream()
                .map(subredditMapper::mapSubredditToSubredditDto)
                .collect(Collectors.toList());
    }


    public SubredditDto getSubreddit(Long id) {
        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()-> new SpringRedditException("No subreddit found with that id."));
        return subredditMapper.mapSubredditToSubredditDto(subreddit);
    }
}
