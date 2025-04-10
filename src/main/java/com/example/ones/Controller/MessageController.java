package com.example.ones.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class MessageController {

    @GetMapping("/message/{memberIdx}")
    public String message(@PathVariable("memberIdx") Long memberIdx) {
        return "message";
    }
}
