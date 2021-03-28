package kr.co.crevill.member;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MemberMapper {
	public int checkExistCellPhone(MemberDto memberDto);
	public MemberVo selectMemberInfo(MemberDto memberDto);
	public int updateMemberParent(MemberDto memberDto);
}
