package dongduk.cs.pulpul.service;

import dongduk.cs.pulpul.domain.Member;
import dongduk.cs.pulpul.service.exception.LoginException;

public interface MemberService {
	
	// 로그인
	Member login(Member member) throws LoginException;
	
	// 회원가입
	boolean register(Member member);
	
	// 회원 정보 조회
	Member getMember(String memberId);
	
	// 회원 정보 수정
	void changeMemberInfo(Member member);
	
	// 포인트 수정
	void changePoint(Member member, int status, int point);
	
}
