package com.xedlab.usersService.domain.hardSkill;

import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;

import java.util.List;

public interface HardSkillService {

  List<HardSkillReadDto> getAllByUserId(long userId);

  void addToUser(long userId, String hardSkillName);

  void removeFromUser(long userId, String hardSkillName);

}
