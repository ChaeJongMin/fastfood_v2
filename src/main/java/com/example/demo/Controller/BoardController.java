package com.example.demo.Controller;

import com.example.demo.Service.BoardsService;
import com.example.demo.Service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class BoardController {
    private final BoardsService boardsService;
    private final CustomerService customerService;
    @GetMapping("/board")
    public String showBoard(Model model, @RequestParam("id") int id){
        model.addAttribute("userId",customerService.findById(id));
        model.addAttribute("boards",boardsService.findAll());
        return "fastfood/board";
    }

    @GetMapping("/addPost")
    public String addBoardContent(Model model, @RequestParam("id") int id){
        model.addAttribute("userId",customerService.findById(id));
        return "fastfood/addPost";
    }
}
