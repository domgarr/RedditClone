package com.domgarr.RedditClone.service;

import com.domgarr.RedditClone.dto.VoteDto;
import com.domgarr.RedditClone.exception.SpringRedditException;
import com.domgarr.RedditClone.mapper.VoteMapper;
import com.domgarr.RedditClone.model.Post;
import com.domgarr.RedditClone.model.User;
import com.domgarr.RedditClone.model.Vote;
import com.domgarr.RedditClone.model.VoteType;
import com.domgarr.RedditClone.repository.PostRepository;
import com.domgarr.RedditClone.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final PostRepository postRepository;
    private final AuthService authService;
    private final VoteMapper voteMapper;

    @Transactional
    public void vote(VoteDto voteDto) {
        Post post = postRepository.findById(voteDto.getPostId()).orElseThrow(
                ()-> new SpringRedditException("Post not found with id of: " + voteDto.getPostId())
        ) ;

        User currentUser = authService.getCurrentUser();

        Optional<Vote> voteOptional = voteRepository.findTopByPostAndUserOrderByIdDesc(post, currentUser);

        if(voteOptional.isPresent() && voteOptional.get().getVoteType().equals(voteDto.getVoteType())){
            //TODO: To properly clone reddit, we should be able to click the same vote type and remove that vote.
            throw new SpringRedditException("You have already voted this type: " + voteDto.getVoteType() );
        }

        if(VoteType.UPVOTE.equals(voteDto.getVoteType())){
            post.setVoteCount(post.getVoteCount() + 1);
        }else{
            post.setVoteCount(post.getVoteCount() - 1);
        }

        voteRepository.save(voteMapper.mapVoteDtoToVote(voteDto, currentUser, post));
        postRepository.save(post);
    }
}
