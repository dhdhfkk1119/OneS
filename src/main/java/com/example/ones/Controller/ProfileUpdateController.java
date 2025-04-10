package com.example.ones.Controller;

import com.example.ones.Entity.Member;
import com.example.ones.Repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ProfileUpdateController {

    private final MemberRepository memberRepository;

    public String uploadURL = "C:/Project/OneS/src/main/resources/static/profile-images";

    @PostMapping("/profile/update")
    public ResponseEntity<Map<String,Object>> updateProfile(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam("userName") String userName,
            Principal principal) throws IOException {

        Map<String,Object> response = new HashMap<>();
        String username = principal.getName();

        Member member = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

        try {
            if (file != null && !file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                Path filePath = Paths.get(uploadURL, fileName);
                Files.write(filePath, file.getBytes());
                member.setUserImage(fileName);
            }

            member.setUserName(userName);
            memberRepository.save(member);

            response.put("status", "success");
            response.put("message", "프로필이 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            response.put("status", "fail");
            response.put("message", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }


}
