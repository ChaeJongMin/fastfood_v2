package com.example.demo.Controller;

import com.example.demo.Service.CommentService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.dto.CommentRequestDto;
import com.example.demo.dto.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/api/post/{id}/comment")
    public Long save(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                     @LoginUser SessionUser user){
        return commentService.save(requestDto, user.getUserId(),id);
    }

    @PutMapping("/api/update/{postId}/comment/{commentId}")
    public Long update(@PathVariable Long postId, @PathVariable Long commentId,
                       @RequestBody CommentUpdateRequestDto requestDto){
        return commentService.update(commentId,requestDto);
    }
    @DeleteMapping("/api/post/delete/comment/{id}")
    public Long delete(@PathVariable Long id){
        commentService.delete(id);
        return id;
    }
}
