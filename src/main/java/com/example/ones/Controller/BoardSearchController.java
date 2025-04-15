package com.example.ones.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BoardSearchController {
    @GetMapping("/search")
    public String search(){
        return "search";
    }
}
