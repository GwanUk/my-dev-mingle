package com.example.dm.controller;

import com.example.dm.dto.chats.ChatCreateDto;
import com.example.dm.dto.chats.ChatMessageGetDto;
import com.example.dm.dto.chats.ChatRoomCreateDto;
import com.example.dm.dto.chats.ChatRoomDetailDto;
import com.example.dm.entity.LoginUser;
import com.example.dm.service.ChatMessageService;
import com.example.dm.service.ChatRoomService;
import com.example.dm.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.dm.controller.ChatMessageController.SUBSCRIBE_URL;

@RestController
@RequestMapping("${api.path.default}/chats")
@RequiredArgsConstructor
public class ChatRoomController extends BaseController {

    private final ChatRoomService chatRoomService;

    private final ChatMessageService chatMessageService;

    private final SimpMessageSendingOperations template;

    @PostMapping
    public ResponseEntity<ApiResponse> createRoom(@RequestBody ChatRoomCreateDto chatRoomCreateDto,
                                                  @AuthenticationPrincipal LoginUser user) {
        return responseBuilder(chatRoomService.createRoom(chatRoomCreateDto, user), HttpStatus.CREATED);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<ApiResponse> joinRoom(@PathVariable Long roomId,
                                                @AuthenticationPrincipal LoginUser user) {
        ChatRoomDetailDto chatRoomDetailDto = chatRoomService.enterRoomUser(roomId, user);

        String message = chatRoomDetailDto.getNickname() + "님이 입장하였습니다.";
        processMessage(roomId, chatRoomDetailDto, message);

        return responseBuilder(chatRoomDetailDto, HttpStatus.OK);
    }

    @GetMapping("/user/rooms")
    public ResponseEntity<ApiResponse> getRoomsByUserProfileId(@AuthenticationPrincipal LoginUser user) {
        return responseBuilder(chatRoomService.findRoomByUser(user.getId()), HttpStatus.OK);
    }

    // todo: 권한 설정
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse> getRoomMessage(@PathVariable Long roomId,
                                                      @AuthenticationPrincipal LoginUser user) {
        return responseBuilder(chatMessageService.findAllMessageByRoomId(roomId, user), HttpStatus.OK);
    }

    @DeleteMapping("/{roomId}/exit")
    public ResponseEntity<ApiResponse> exitRoom(@PathVariable Long roomId,
                                                @AuthenticationPrincipal LoginUser user) {
        ChatRoomDetailDto chatRoomDetailDto = chatRoomService.exitRoomUser(roomId, user);

        String message = chatRoomDetailDto.getNickname() + "님이 퇴장하였습니다.";
        processMessage(roomId, chatRoomDetailDto, message);

        return responseBuilder(chatRoomDetailDto, HttpStatus.OK);
    }
    @GetMapping("/{roomId}/userlist")
    public ResponseEntity<ApiResponse> getUserList(@PathVariable Long roomId) {
        return responseBuilder(chatRoomService.getRoomUserList(roomId), HttpStatus.OK);
    }

    private void processMessage(Long roomId, ChatRoomDetailDto chatRoomDetailDto, String message) {
        ChatCreateDto chatCreateDto = ChatCreateDto.from(chatRoomDetailDto, message);
        ChatMessageGetDto chatMessageGetDto = chatMessageService.saveMessage(chatCreateDto);
        template.convertAndSend(SUBSCRIBE_URL + roomId, chatMessageGetDto);
    }
}
