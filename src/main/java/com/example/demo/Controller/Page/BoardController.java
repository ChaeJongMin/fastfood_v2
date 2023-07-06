package com.example.demo.Controller.Page;

import com.example.demo.Service.BoardsService;
import com.example.demo.Service.CustomerService;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.dto.Response.BoardsDto;
import com.example.demo.dto.Response.CommentResponseDto;
import com.example.demo.dto.Response.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class BoardController {
    private final BoardsService boardsService;
    private final CustomerService customerService;
    private String currentUserId;
    @GetMapping("/board")
    public String showBoard(Model model, @AuthenticationPrincipal CustomUserDetail customUser, @PageableDefault(size = 10) Pageable pageable
    , String target , String searchKeyword){

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
        model.addAttribute("userId",currentUserId=customerService.findByidForUserId(customUser.getId()));
        model.addAttribute("pages",pageDto);
        model.addAttribute("boards",boards);


        return "fastfood/customer/board/board";
    }

    @GetMapping("/addPost")
    public String addBoardContent(Model model, @AuthenticationPrincipal CustomUserDetail customUser ){
        model.addAttribute("userId",customerService.findById(customUser.getId()));

        return "fastfood/customer/board/addPost";
    }

    @GetMapping("/boardContent")
    public String showDetailContent(Model model, @RequestParam("id") long id,@AuthenticationPrincipal CustomUserDetail customUser){
        //댓글 처리
        boardsService.viewsCount(id);

        BoardsDto dto=boardsService.findById(id);
        List<CommentResponseDto> commentResponseDtoList= dto.getComments();

        if(commentResponseDtoList!=null && !commentResponseDtoList.isEmpty()){
            model.addAttribute("comments",commentResponseDtoList);
        }
        String userId=customerService.findByidForUserId(customUser.getId());
        model.addAttribute("boards",dto);
        model.addAttribute("checkedMe",customerService.compareWriter(dto.getUserId(),userId));
        model.addAttribute("connectId",customUser.getCustomer().getUserId());
        return "fastfood/customer/board/boardContent";
    }



}
