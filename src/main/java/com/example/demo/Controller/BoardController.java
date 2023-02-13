package com.example.demo.Controller;

import com.example.demo.Service.BoardsService;
import com.example.demo.Service.CustomerService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.domain.Boards;
import com.example.demo.domain.Customer;
import com.example.demo.dto.BoardsDto;
import com.example.demo.dto.CommentResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class BoardController {
    private final BoardsService boardsService;
    private final CustomerService customerService;
    private String currentUserId;
    @GetMapping("/board")
    public String showBoard(Model model, @LoginUser SessionUser user ){
        model.addAttribute("userId",currentUserId=customerService.findById(user.getId()));
        model.addAttribute("boards",boardsService.findAll());
        return "fastfood/board";
    }

    @GetMapping("/addPost")
    public String addBoardContent(Model model, @LoginUser SessionUser user ){
        model.addAttribute("userId",customerService.findById(user.getId()));
        return "fastfood/addPost";
    }

    @GetMapping("/boardContent")
    public String showDetailContent(Model model, @RequestParam("id") long id,@LoginUser SessionUser user ){
        //댓글 처리
        boardsService.viewsCount(id);

        BoardsDto dto=boardsService.findById(id);
        List<CommentResponseDto> commentResponseDtoList= dto.getComments();

        if(commentResponseDtoList!=null && !commentResponseDtoList.isEmpty()){
            model.addAttribute("comments",commentResponseDtoList);
        }

        model.addAttribute("boards",dto);
        model.addAttribute("checkedMe",customerService.compareWriter(dto.getUserId(),user.getUserId()));

        return "fastfood/boardContent";
    }



}
