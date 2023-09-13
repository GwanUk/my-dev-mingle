package com.example.dm.controller;

import com.example.dm.dto.ApiResponse;
import com.example.dm.dto.commons.PreSignedUrlDto;
import com.example.dm.service.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.default}/commons")
public class CommonController extends BaseController {

    private final S3Service s3Service;

    /**
     * PreSigned URL 요청
     */
    @PostMapping("/pre-signed-url")
    public ResponseEntity<ApiResponse> getPreSignedUrl(@RequestBody PreSignedUrlDto.InputDto inputDto) {

        List<PreSignedUrlDto.OutputDto> outputDtoList = s3Service.getPreSignedUrls(inputDto);
        return responseBuilder(outputDtoList, HttpStatus.OK);
    }

}
