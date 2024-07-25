package com.xedlab.usersService.domain.hardSkill;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersHardSkillsRepository extends JpaRepository<UsersHardSkillsEntity, UserHardSkillsId> {

  int countByHardSkills_Id(long hardSkillId);
}
