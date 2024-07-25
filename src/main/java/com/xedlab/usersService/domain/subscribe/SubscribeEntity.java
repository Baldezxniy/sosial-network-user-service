package com.xedlab.usersService.domain.subscribe;

import com.xedlab.usersService.domain.users.entity.AvatarUrl;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription", schema = "schema_users")
public class SubscribeEntity {

  @EmbeddedId
  private SubscribeId pk;

  @ManyToOne
  @MapsId("creatorId")
  @JoinColumn(name = "creator_id", referencedColumnName = "id")
  private UserEntity creator;

  @ManyToOne
  @MapsId("subscriberId")
  @JoinColumn(name = "subscriber_id", referencedColumnName = "id")
  private UserEntity subscriber;

  private boolean isDeleted;

  public SubscribeEntity(
          long creatorId,
          String creatorFirstName,
          String creatorSecondName,
          String creatorLastName,
          AvatarUrl creatorAvatarUrl,
          boolean creatorIsDeleted,

          long subscriberId,

          boolean isDeleted
  ) {
    this.pk = new SubscribeId(creatorId, subscriberId);

    this.creator = UserEntity.builder()
            .id(creatorId)
            .firstName(creatorFirstName)
            .secondName(creatorSecondName)
            .lastName(creatorLastName)
            .avatarUrl(creatorAvatarUrl)
            .isDeleted(creatorIsDeleted)
            .build();

    this.subscriber = UserEntity.builder()
            .id(creatorId)
            .build();

    this.isDeleted = isDeleted;
  }

  public SubscribeEntity(
          long creatorId,

          long subscriberId,
          String subscriberFirstName,
          String subscriberSecondName,
          String subscriberLastName,
          AvatarUrl subscriberAvatarUrl,
          boolean subscriberIsDeleted,

          boolean isDeleted
  ) {
    this.pk = new SubscribeId(creatorId, subscriberId);

    this.creator = UserEntity.builder()
            .id(creatorId)
            .build();

    this.subscriber = UserEntity.builder()
            .id(creatorId)
            .firstName(subscriberFirstName)
            .secondName(subscriberSecondName)
            .lastName(subscriberLastName)
            .avatarUrl(subscriberAvatarUrl)
            .isDeleted(subscriberIsDeleted)
            .build();

    this.isDeleted = isDeleted;
  }
}
