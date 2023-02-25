package com.example.demo.Controller;

import com.example.demo.Service.BoardsService;
import com.example.demo.Service.CustomerService;
import com.example.demo.config.auth.LoginUser;
import com.example.demo.config.auth.dto.SessionUser;
import com.example.demo.domain.Boards;
import com.example.demo.domain.Customer;
import com.example.demo.dto.BoardsDto;
import com.example.demo.dto.CommentResponseDto;
import com.example.demo.dto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
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
    public String showBoard(Model model, @LoginUser SessionUser user, @PageableDefault(size = 10) Pageable pageable
    ,String target ,String searchKeyword){

        Page<BoardsDto> boards=null;

        if(searchKeyword==null){
            boards=boardsService.findAll(pageable);
        } else
            boards=boardsService.boardSearchList(searchKeyword,pageable,target);

        PageDto pageDto=boardsService.makePageDto(boards.getPageable().getPageNumber()+1, boards.getTotalPages());

        int nextPage=pageDto.getCurrentPage()+9;
        if(nextPage>boards.getTotalPages()){
            nextPage=pageDto.getMostEndPage()-1;
        }
        model.addAttribute("nextPage", nextPage);
        System.out.println("페이지 위치: "+pageDto.getStartPage()+" "+pageDto.getEndPage());
        model.addAttribute("userId",currentUserId=customerService.findById(user.getId()).getUserId());
        model.addAttribute("pages",pageDto);
        model.addAttribute("boards",boards);


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
