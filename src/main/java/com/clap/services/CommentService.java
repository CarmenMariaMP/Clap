package com.clap.services;

import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.model.Comment;
import com.clap.model.User;
import com.clap.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArtisticContentService artisticContentService;

    public List<Comment> getsCommentsByContentId(String artistic_content_id) {
        return commentRepository.findCommentsByContentId(artistic_content_id);
    }

    public Comment getCommentById(String id) {
        return commentRepository.findCommentById(id);
    }

    public void deleteCommentsByContentId(String artistic_content_id) {
        commentRepository.deleteCommentsByContentId(artistic_content_id);;
    }

    public void addComment(Comment comment, String artistic_content_id, String  username) {
        ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
        User user = userService.getUserByUsername(username).orElse(null);
        comment.setArtisticContent(artisticContent);
        comment.setUser(user);
        comment.setDate(Date.from(Instant.now()));
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(comment.getDate());
        Integer day= cal.get(Calendar.DAY_OF_MONTH);
        Integer month= cal.get(Calendar.MONTH);
        if(month==0){
            month=1;
        }else{
            month+=1;
        }
        Integer year = cal.get(Calendar.YEAR);
        comment.setDateString(String.format("%d/%d/%d", month,day,year));
        commentRepository.save(comment);
    }
}
