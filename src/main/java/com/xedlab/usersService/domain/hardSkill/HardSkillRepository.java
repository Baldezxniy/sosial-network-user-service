package com.xedlab.usersService.domain.hardSkill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HardSkillRepository extends JpaRepository<HardSkillsEntity, Long> {

  @Query(value = """
          SELECT hs FROM HardSkillsEntity hs
          JOIN UsersHardSkillsEntity  uhs ON hs.id = uhs.hardSkills.id
          WHERE uhs.user.id = :userId AND
          uhs.user.isDeleted = false
          """)
  List<HardSkillsEntity> findAllByUserId(@Param("userId") long userId);

  void deleteBySkillName(String skillName);

  Optional<HardSkillsEntity> findBySkillName(String skillName);
}
