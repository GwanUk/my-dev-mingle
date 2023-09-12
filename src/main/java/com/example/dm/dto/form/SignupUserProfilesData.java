package com.example.dm.dto.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
@AllArgsConstructor
public class SignupUserProfilesData {
    private String nickname;
    private String city;
    private String state;
    private String street;
    private String introduce;
    private String url;
    private String url_name;
}
