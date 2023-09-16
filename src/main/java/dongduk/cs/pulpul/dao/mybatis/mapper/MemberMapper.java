package dongduk.cs.pulpul.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import dongduk.cs.pulpul.domain.Member;

@Mapper
public interface MemberMapper {
	
	Member selectMemberById(String memberId);
	
	int insertMember(Member member);
	
	int updateMember(Member member);
	
	int updatePoint(@Param("id") String memberId, 
			@Param("status") int status, 
			@Param("point") int point);
	
}
