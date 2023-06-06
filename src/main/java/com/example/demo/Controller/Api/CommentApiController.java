package com.example.demo.Controller.Api;

import com.example.demo.Service.CommentService;
import com.example.demo.config.auth.formLogin.LoginUser;
import com.example.demo.config.auth.formLogin.dto.SessionUser;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.dto.Request.CommentRequestDto;
import com.example.demo.dto.Request.CommentUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping("/api/post/{id}/comment")
    public Long save(@PathVariable Long id, @RequestBody CommentRequestDto requestDto,
                     @AuthenticationPrincipal CustomUserDetail customUser){
        return commentService.save(requestDto, customUser.getCustomer().getUserId(),id);
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
