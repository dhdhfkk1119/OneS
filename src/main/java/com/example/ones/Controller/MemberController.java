package com.example.ones.Controller;

import com.example.ones.DTO.MemberDTO;
import com.example.ones.Service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // th 모델 객체 가져오기
    @GetMapping("/sign")
    public String sign(Model model){

        model.addAttribute("member", new MemberDTO());
        return "sign";
    }

    // 중복확인 유무
    @GetMapping("/check-userId")
    @ResponseBody
    public Map<String, Boolean> checkUsername(@RequestParam String userId) {
        boolean exists = memberService.isIdCheck(userId);
        Map<String, Boolean> map = new HashMap<>();
        map.put("exists", exists);
        return map;
    }
    
    // 회원 가입 등록하기
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("member") MemberDTO member,
                           BindingResult bindingResult,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        // 입력값 검증 후 오류가 있으면 다시 회원가입 페이지로 이동
        if (bindingResult.hasErrors()) {
            return "sign";  // sign.html로 다시 이동
        }

        // 비밀번호 확인
        if (!member.getUserPassword().equals(member.getUserRePassword())) {
            bindingResult.rejectValue("userRePassword", "passwordInCorrect", "비밀번호가 일치하지 않습니다");
            return "sign";
        }

        try {
            memberService.register(member);
            redirectAttributes.addFlashAttribute("message", "회원가입이 완료되었습니다");
            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage()); // 잘못된 인자가 메서드로 전달되면 발생 비밀번호가 너무 짧거나, 잘못된 데이터 입력
            return "sign";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("error", "이미 등록된 사용자입니다"); // 데이터 무결성 체크 	데이터베이스 중복 오류 (ex: 아이디 중복)
            return "sign";
        } catch (Exception e) {
            model.addAttribute("error", "회원가입 중 오류가 발생했습니다"); // 위 2개 에러 외에 것을 잡음
            return "sign";
        }
    }

}
