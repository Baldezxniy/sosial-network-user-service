package com.xedlab.usersService.domain.hardSkill;

import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface HardSkillMapper {

  HardSkillReadDto toDto(HardSkillsEntity entity);

  HardSkillsEntity toEntity(HardSkillReadDto entity);
}
