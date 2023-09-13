package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.follows.FollowAddDto;
import com.example.dm.dto.follows.FollowInfoDto;
import com.example.dm.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/follows")
public class FollowController extends BaseController {

    private final FollowService followService;

    /**
     * 팔로우 등록 API
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse> addFollow(@RequestBody FollowAddDto inputDto) {
        FollowInfoDto followInfoDto = followService.addFollows(inputDto, getCurrentUserProfiles());
        return responseBuilder(followInfoDto, HttpStatus.CREATED);
    }

    /**
     * 팔로우 리스트 조회 API
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse> showFollowList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<FollowInfoDto> followInfoDtoList = followService.showFollowList(getCurrentUserProfiles(), pageable);
        return responseBuilder(followInfoDtoList, HttpStatus.OK);
    }

}
