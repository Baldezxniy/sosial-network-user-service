package com.xedlab.usersService.domain.hardSkill;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Embeddable
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserHardSkillsId implements Serializable {

  @Serial
  private static final long serialVersionUID = -2056566883243065714L;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "skill_id")
  private Long hardSkillId;

  public UserHardSkillsId(long userId, long hardSkillId){
    this.userId = userId;
    this.hardSkillId = hardSkillId;
  }
}
