package com.example.dm.entity;

import com.example.dm.dto.chats.ChatRoomCreateDto;
import com.example.dm.dto.chats.ChatRoomPatchDto;
import com.example.dm.enums.ImageType;
import com.example.dm.exception.ApiResultStatus;
import com.example.dm.exception.BusinessException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRooms extends DeletedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int userCount;

    @Column
    private int capacity;

    @ManyToOne
    @JoinColumn(name = "admin_user")
    private UserProfiles adminUser;

    @Builder.Default
    @OneToMany(mappedBy = "chatRooms", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatMembers> chatMembers = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Images thumbnail;

    public void plusUserCount() {
        this.userCount++;
    }

    public void minusUserCount() {
        this.userCount--;
    }

    public void addUser(ChatMembers chatMember){
        chatMembers.add(chatMember);
    }

    public boolean removeUser(Long userProfileId) {
        return this.getChatMembers()
                .removeIf(chatRoomUserProfiles -> chatRoomUserProfiles.getUserProfiles().getId().equals(userProfileId));
    }

    public static ChatRooms from(ChatRoomCreateDto chatRoomCreateDto, UserProfiles userProfiles, Images images) {
        return ChatRooms.builder()
                .name(chatRoomCreateDto.getName())
                .adminUser(userProfiles)
                .userCount(1)
                .capacity(chatRoomCreateDto.getCapacity())
                .thumbnail(images)
                .build();
    }

    public void updateRoom(ChatRoomPatchDto chatRoomPatchDto) {
        this.name = chatRoomPatchDto.getName();
        this.thumbnail = Images.create(chatRoomPatchDto.getThumbnailUrl(), ImageType.Chats, null);
        if(chatRoomPatchDto.getCapacity() < this.userCount) throw new BusinessException(ApiResultStatus.INVALID_CAPACITY);
        this.capacity = chatRoomPatchDto.getCapacity();
    }

}
