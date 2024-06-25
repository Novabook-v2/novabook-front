package com.nhnacademy.novabook_front.api.member.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nhnacademy.novabook_front.api.member.dto.CreateMemberRequest;
import com.nhnacademy.novabook_front.api.member.dto.CreateMemberResponse;
import com.nhnacademy.novabook_front.api.member.dto.LoginMemberRequest;
import com.nhnacademy.novabook_front.api.member.dto.LoginMemberResponse;

public interface MemberService {
	CreateMemberResponse createMember(CreateMemberRequest createMemberRequest);

	LoginMemberResponse login(LoginMemberRequest loginMemberRequest);
}
