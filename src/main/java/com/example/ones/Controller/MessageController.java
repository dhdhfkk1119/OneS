package com.example.ones.Controller;

import com.example.ones.DTO.MemberDTO;
import com.example.ones.DTO.MessageSearchDTO;
import com.example.ones.Entity.Follow;
import com.example.ones.Entity.Member;
import com.example.ones.Entity.Message;
import com.example.ones.Repository.FollowRepository;
import com.example.ones.Repository.MemberRepository;
import com.example.ones.Repository.MessageRepository;
import com.example.ones.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MessageController {

    private final MemberRepository memberRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    private final FollowRepository followRepository;

    @GetMapping("/message/{idx}")
    public String message(@PathVariable("idx") Long idx, Model model, Principal principal,
                          @RequestParam(required = false) String keyword) {

        String username = principal.getName();
        Member loginUser = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Member targetUser = memberRepository.findByIdx(idx)
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다"));

        // 현재 사용자와 대상 사용자 간의 메시지 가져오기
        List<Message> messageList = messageRepository.findMessagesBetweenUsers(loginUser.getIdx(), targetUser.getIdx());

        // 메시지 이미지 리스트 맵핑
        Map<Long, List<String>> messageImageMap = new HashMap<>();
        for (Message message : messageList) {
            if (message.getImagesContent() != null && !message.getImagesContent().isEmpty()) {
                List<String> images = Arrays.asList(message.getImagesContent().split(","));
                messageImageMap.put(message.getMessageIdx(), images);
            }
        }

        // 쪽지 보낸/받은 유저 리스트
        Set<Long> addedIds = new HashSet<>();
        List<Member> sendUserList = new ArrayList<>();

        // 안 읽은 메시지 수 맵핑
        Map<Long, Integer> unreadMessageCountMap = new HashMap<>();

        // 최근 메시지 맵핑
        Map<Long, Message> recentlyMessageMap = new HashMap<>();

        // 1. 내가 보낸 유저 추가
        List<Message> senderMessages = messageRepository.findBySenderIdx(loginUser.getIdx());
        for (Message msg : senderMessages) {
            Long receiverIdx = msg.getReceiverIdx();
            if (addedIds.add(receiverIdx)) {
                Member member = memberRepository.findByIdx(receiverIdx)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                if (keyword == null || member.getUserName().contains(keyword)) {
                    sendUserList.add(member);
                }
            }
        }

        // 2. 나에게 보낸 유저 추가
        List<Message> receiverMessages = messageRepository.findByReceiverIdx(loginUser.getIdx());
        for (Message msg : receiverMessages) {
            Long senderIdx = msg.getSenderIdx();
            if (addedIds.add(senderIdx)) {
                Member member = memberRepository.findByIdx(senderIdx)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                if (keyword == null || member.getUserName().contains(keyword)) {
                    sendUserList.add(member);
                }
            }

            // 안 읽은 메시지 수 세기
            List<Message> unread = messageRepository.findBySenderIdxAndReceiverIdxAndIsReadFalse(senderIdx, loginUser.getIdx());
            if (!unread.isEmpty()) {
                unreadMessageCountMap.put(senderIdx, unread.size());
            }
        }

        // 3. 최근 메시지 맵핑
        for (Member user : sendUserList) {
            List<Message> betweenMessages = messageRepository.findMessagesBetweenUsers(loginUser.getIdx(), user.getIdx());
            if (!betweenMessages.isEmpty()) {
                betweenMessages.sort(Comparator.comparing(Message::getSendAt).reversed());
                recentlyMessageMap.put(user.getIdx(), betweenMessages.get(0));
            }
        }

        // 4. 팔로우 리스트 구성
        Set<Long> followIds = new HashSet<>();
        List<Member> followListMap = new ArrayList<>();

        List<Follow> followers = followRepository.findByFollowLoginidx(loginUser.getIdx());
        for (Follow follow : followers) {
            if (followIds.add(follow.getFollowUseridx())) {
                Member member = memberRepository.findByIdx(follow.getFollowUseridx())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                followListMap.add(member);
            }
        }

        List<Follow> followings = followRepository.findByFollowUseridx(loginUser.getIdx());
        for (Follow follow : followings) {
            if (followIds.add(follow.getFollowLoginidx())) {
                Member member = memberRepository.findByIdx(follow.getFollowLoginidx())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                followListMap.add(member);
            }
        }

        // 5. 안 읽은 메시지 읽음 처리
        messageService.markMessagesAsRead(targetUser.getIdx(), loginUser.getIdx());

        // 모델 속성 등록
        model.addAttribute("login", loginUser);
        model.addAttribute("member", targetUser);
        model.addAttribute("messages", messageList);
        model.addAttribute("ImageList", messageImageMap);
        model.addAttribute("sendUserList", sendUserList);
        model.addAttribute("recentlyMessage", recentlyMessageMap);
        model.addAttribute("followList", followListMap);
        model.addAttribute("unreadMessageCount", unreadMessageCountMap);

        return "message";
    }


    @PostMapping("/message/search")
    @ResponseBody
    public List<MessageSearchDTO> searchMessageUserList(@RequestBody Map<String, String> request, Principal principal) {
        String keyword = request.get("keyword");

        String username = principal.getName();
        Member loginUser = memberRepository.findByUserId(username)
                .orElseThrow(() -> new RuntimeException("로그인 유저 없음"));

        // 메시지 조인 쿼리로 검색된 Member + message 내용까지 가져오기
        List<Object[]> results = messageRepository.findRecentMessagesWithKeyword(loginUser.getIdx(), keyword);

        List<MessageSearchDTO> resultDTOs = results.stream()
                .map(obj -> {
                    Long memberIdx = (Long) obj[0];
                    String messageContent = (String) obj[1];

                    Member member = memberRepository.findByIdx(memberIdx)
                            .orElseThrow(() -> new RuntimeException("유저 없음"));

                    return new MessageSearchDTO(memberIdx, member.getUserId(), member.getUserName(), member.getUserImage(), messageContent, member.getUserStatus());
                })
                .collect(Collectors.toList());

        return resultDTOs;
    }

    @Value("${file.upload-dir}")
    private String uploadDir;

    // 메세지 에서 이미지만 따로 답는 API
    @PostMapping("/upload/message-image")
    public ResponseEntity<List<String>> uploadMessageImage(@RequestParam("image") MultipartFile[] files){

        List<String> fileNames = new ArrayList<>();
        try {

            for (MultipartFile file : files) {
                String originalFileName = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
                Path filePath = Paths.get(uploadDir, fileName);

                file.transferTo(filePath.toFile());
                fileNames.add(fileName);

            }
            return ResponseEntity.ok(fileNames);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
