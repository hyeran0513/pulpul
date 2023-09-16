package dongduk.cs.pulpul.dao;

import dongduk.cs.pulpul.domain.Member;

public interface MemberDao {
	
	// 회원 id로 회원 정보 조회
	Member findMember(String memberId);
	
	// 회원 정보 생성
	boolean createMember(Member member);
	
	// 회원 정보 수정
	boolean changeMemberInfo(Member member);
	
	// 회원 포인트 값 변경
	boolean changePoint(String memberId, int status, int point);
	
}
