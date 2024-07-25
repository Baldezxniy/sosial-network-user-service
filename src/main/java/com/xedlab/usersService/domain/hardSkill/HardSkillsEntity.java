package com.xedlab.usersService.domain.hardSkill;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hard_skills", schema = "schema_users")
public class HardSkillsEntity {
  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "skill_name")
  private String skillName;

  public HardSkillsEntity(String skillName) {
    this.skillName = skillName;
  }
}
