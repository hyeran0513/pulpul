package dongduk.cs.pulpul.dao.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import dongduk.cs.pulpul.dao.MemberDao;
import dongduk.cs.pulpul.dao.mybatis.mapper.MemberMapper;
import dongduk.cs.pulpul.domain.Member;

@Repository
public class MybatisMemberDao implements MemberDao {

	@Autowired
    private MemberMapper memberMapper;
	
	@Override
	public Member findMember(String memberId) {
		return memberMapper.selectMemberById(memberId);
	}

	@Override
	public boolean createMember(Member member) {
		if (memberMapper.insertMember(member) > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean changeMemberInfo(Member member) {
		if (memberMapper.updateMember(member) > 0) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean changePoint(String memberId, int status, int point) {
		int ck = memberMapper.updatePoint(memberId, status, point);
		if (ck > 0) return true;
		return false;
	}

}
