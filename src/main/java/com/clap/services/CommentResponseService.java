package com.clap.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.model.Comment;
import com.clap.model.CommentResponse;
import com.clap.model.User;
import com.clap.repository.CommentResponseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentResponseService {
    private final CommentResponseRepository commentResponseRepository;
    private final CommentService commentService;
    private final UserService userService;
    private final ArtisticContentService artisticContentService;

    public List<CommentResponse> getCommentResponsesByCommentId(String id) {
        return commentResponseRepository.findCommentResponsesByCommentId(id);
    }

    public void addCommentResponse(CommentResponse commentResponse, String comment_id,String username) {
        Comment comment = commentService.getCommentById(comment_id);
        User user_comment = userService.getUserByCommentId(comment.getId());
        ArtisticContent artisticContent = artisticContentService.getByCommentId(comment_id);
        comment.setUser(user_comment);
        comment.setArtisticContent(artisticContent);
        User user_comment_response = userService.getUserByUsername(username).orElse(null);
        commentResponse.setComment(comment);
        commentResponse.setUser(user_comment_response);
        commentResponseRepository.save(commentResponse);
    }
}
