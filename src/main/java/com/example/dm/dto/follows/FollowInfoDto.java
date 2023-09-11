package com.example.dm.dto.follows;

import com.example.dm.entity.Follows;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Builder
public class FollowInfoDto {

    private Long id; // 팔로우 id

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt; // 팔로우 생성 시간

    private TargetUserInfoDto targetUserInfo; // 팔로우 대상 유저 정보

    // Follows -> FollowInfoDto
    public static FollowInfoDto convertFollows(Follows follows) {
        return FollowInfoDto.builder()
                .id(follows.getId())
                .createdAt(follows.getCreatedAt())
                .targetUserInfo(TargetUserInfoDto.convertFollows(follows))
                .build();
    }
}
