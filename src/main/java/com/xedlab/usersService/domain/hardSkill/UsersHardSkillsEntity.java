package com.xedlab.usersService.domain.hardSkill;

import com.xedlab.usersService.domain.users.entity.UserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_hard_skills", schema = "schema_users")
public class UsersHardSkillsEntity {

  @EmbeddedId
  private UserHardSkillsId pk;

  @ManyToOne
  @JoinColumn(name = "user_id", insertable = false, updatable = false)
  private UserEntity user;

  @ManyToOne
  @JoinColumn(name = "skill_id", insertable = false, updatable = false)
  private HardSkillsEntity hardSkills;

}
