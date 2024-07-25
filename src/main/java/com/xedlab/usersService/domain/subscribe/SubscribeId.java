package com.xedlab.usersService.domain.subscribe;

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
public class SubscribeId implements Serializable {

  @Serial
  private static final long serialVersionUID = -769273548172639481L;

  @Column(name = "creator_id")
  private Long creatorId;

  @Column(name = "subscriber_id")
  private Long subscriberId;

}
