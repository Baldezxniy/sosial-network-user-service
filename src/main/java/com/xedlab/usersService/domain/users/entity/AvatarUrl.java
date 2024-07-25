package com.xedlab.usersService.domain.users.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "value")
public class AvatarUrl {
  private String value;
}
