package kr.co.crevill.staff;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StaffMapper {
	public int selectStaffCount(StaffDto staffDto);
	public List<StaffVo> selectStaffList(StaffDto staffDto);
	public int insertStaffInfo(StaffDto staffDto);
	public int checkExistTelNo(InstructorDto instructorDto);
	public StaffVo selectStaffInfo(StaffDto staffDto);
	public int selectInstructorCount(InstructorDto instructorDto);
	public List<InstructorVo> selectInstructorList(InstructorDto instructorDto);
	public InstructorVo selectInstructorInfo(InstructorDto instructorDto);
	public int insertInstructorInfo(InstructorDto instructorDto);
	public int updateStaffInfo(StaffDto staffDto);
	public int updateInstructorInfo(InstructorDto instructorDto);
	public int deleteStaffInfo(StaffDto staffDto);
	public int deleteInstructorInfo(InstructorDto instructorDto);
}