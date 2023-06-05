package com.example.demo.Controller.Page;

import com.example.demo.Service.ChatService;
import com.example.demo.config.auth.jwt.UserData.CustomUserDetail;
import com.example.demo.persistence.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping(path = "/fastfood")
public class ChatController {
private final ChatService chatService;
    @GetMapping("/Chat")
    public String chatView(@AuthenticationPrincipal CustomUserDetail customUser, Model model){
        model.addAttribute("myId",customUser.getId());
        model.addAttribute("userId",customUser.getCustomer().getUserId());
        model.addAttribute("ids",customUser.getCustomer().getId());
        //필요없는 방 삭제
        chatService.deleteEmptyChatRoom();
        return "fastfood/customer/chat/Chat";
    }

}
