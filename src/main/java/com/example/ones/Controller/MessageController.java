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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                .orElseThrow(() -> new RuntimeException("해당 유저를 찾을 수 없습니다")); // 상

        List<Message> messageList = messageRepository.findMessagesBetweenUsers(loginUser.getIdx(), targetUser.getIdx());


        // 내가 팔로우 하고있는 나를 팔로우 하고 있는 유저들의 리스트
        Set<Long> followIds = new HashSet<>();
        List<Member> followListMap = new ArrayList<>();

        // 제일 최근 메세지 들고오기
        Map<Long, Message> RecentlyMessage = new HashMap<>();

        // 내가 메세지를 보낸 유저의 정보를 가져옴
        Set<Long> addedIds = new HashSet<>();
        List<Member> SendUserList = new ArrayList<>();

        // 나에게만 메세지를 보낸 메세지 내용
        Map<Long, Integer> unreadMessageCountMap = new HashMap<>();


        List<Message> senderUser = messageRepository.findBySenderIdx(loginUser.getIdx());
        for (Message message : senderUser) {
            if (!addedIds.contains(message.getReceiverIdx())) {
                Member member = memberRepository.findByIdx(message.getReceiverIdx())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                if (keyword == null || member.getUserName().contains(keyword)) {
                    SendUserList.add(member);
                    addedIds.add(message.getReceiverIdx());

                    // 제일 최근 메세지 들고오기
                    Optional<Message> recently = messageRepository
                            .findTopBySenderIdxAndReceiverIdxOrderBySendAtDesc(loginUser.getIdx(), message.getReceiverIdx());

                    recently.ifPresent(msg -> RecentlyMessage.put(message.getReceiverIdx(), msg));
                }
            }
        }

        List<Message> receiverUser = messageRepository.findByReceiverIdx(loginUser.getIdx()); // 나에게 메세지를 보낸 유저
        for (Message message : receiverUser) {
            if (!addedIds.contains(message.getSenderIdx())) {
                Member member = memberRepository.findByIdx(message.getSenderIdx())
                        .orElseThrow(() -> new RuntimeException("User not found"));
                if (keyword == null || member.getUserName().contains(keyword)) {
                    SendUserList.add(member);
                    addedIds.add(message.getSenderIdx());

                    // 제일 최근 메세지 들고오기
                    Optional<Message> recently = messageRepository
                            .findTopBySenderIdxAndReceiverIdxOrderBySendAtDesc(message.getSenderIdx(), loginUser.getIdx());
                    recently.ifPresent(msg -> RecentlyMessage.put(message.getSenderIdx(), msg));
                }
            }

            // 상대가 나에게 보낸 메시지 중 안 읽은 것만 필터링
            List<Message> unreadMessages = messageRepository.findBySenderIdxAndReceiverIdxAndIsReadFalse(message.getSenderIdx(), loginUser.getIdx());

            if (!unreadMessages.isEmpty()) {
                unreadMessageCountMap.put(message.getSenderIdx(), unreadMessages.size());
            }
        }


        List<Follow> followerlist = followRepository.findByFollowLoginidx(loginUser.getIdx()); // 현재 로그인 한 유저의 팔로워 목록
        for (Follow follower : followerlist) {
            if (followIds.add(follower.getFollowUseridx())) {
                Member member = memberRepository.findByIdx(follower.getFollowUseridx()).orElseThrow(() -> new RuntimeException("User not found"));
                followListMap.add(member);
            }

        }

        List<Follow> followinglist = followRepository.findByFollowUseridx(loginUser.getIdx()); // 현재 로그인 한 유저의 팔로잉 목록
        for (Follow follower : followinglist) {
            if (followIds.add(follower.getFollowLoginidx())) {
                Member member = memberRepository.findByIdx(follower.getFollowLoginidx()).orElseThrow(() -> new RuntimeException("User not found"));
                followListMap.add(member);
            }
        }


        // 상대방이 나에게 보낸 메시지 중 안 읽은 거 읽음 처리
        messageService.markMessagesAsRead(targetUser.getIdx(), loginUser.getIdx());

        model.addAttribute("login", loginUser); // 현재 로그인 한 유저의 정보
        model.addAttribute("member", targetUser); // 메세지를 받는 상대 유저의 정보
        model.addAttribute("messages", messageList); // 메세지 리스트 가져오기
        model.addAttribute("sendUserList", SendUserList); // 내가 메세지를 보낸 유저의 리스트 가져오기
        model.addAttribute("recentlyMessage", RecentlyMessage); // 메세지를 나눴던 제이 최근 유저
        model.addAttribute("followList", followListMap); // 내가 팔로우 하고 있는 유저
        model.addAttribute("unreadMessageCount", unreadMessageCountMap); // 메세지를 읽지 않았으면 경고 표시및 갯수


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

        System.out.println("조회된 원시 결과:");
        for (Object[] row : results) {
            System.out.println("  memberIdx: " + row[0] + ", messageContent: " + row[1]);
        }

        List<MessageSearchDTO> resultDTOs = results.stream()
                .map(obj -> {
                    Long memberIdx = (Long) obj[0];
                    String messageContent = (String) obj[1];

                    Member member = memberRepository.findByIdx(memberIdx)
                            .orElseThrow(() -> new RuntimeException("유저 없음"));

                    return new MessageSearchDTO(memberIdx ,member.getUserId(), member.getUserName(), member.getUserImage(), messageContent);
                })
                .collect(Collectors.toList());

        System.out.println("최종 응답 DTO 목록:");
        resultDTOs.forEach(System.out::println); // MessageSearchDTO에 toString() 있으면 보기 좋음


        return resultDTOs;
    }


}
