package com.xedlab.usersService.domain.subscribe;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscribeRepository extends JpaRepository<SubscribeEntity, SubscribeId> {

  @Query("""
            SELECT count(s) FROM SubscribeEntity s
            WHERE
            s.isDeleted = false AND
            s.creator.isDeleted = false AND
            s.creator.id = :userId AND
            s.subscriber.isDeleted = false
          """)
  int findCountSubscribersByUserId(@Param("userId") long userId);

  @Query("""
            SELECT count(s) FROM SubscribeEntity s
            WHERE
            s.isDeleted = false AND
            s.subscriber.isDeleted = false AND
            s.subscriber.id = :userId AND
            s.creator.isDeleted = false
          """)
  int findCountSubscriptionByUserId(@Param("userId") long userId);

  @Modifying
  @Query("UPDATE SubscribeEntity s SET s.isDeleted = true WHERE s.creator.id = :creatorId")
  void softDeleteAllByCreatorId(@Param("creatorId") long creatorId);

  @Modifying
  @Query("UPDATE SubscribeEntity s SET s.isDeleted = true WHERE s.subscriber.id = :subscriberId")
  void softDeleteAllBySubscriberId(@Param("subscriberId") long subscriberId);

  @Query("""
          SELECT new com.xedlab.usersService.domain.subscribe.SubscribeEntity
          (
            s.creator.id,
            s.creator.firstName,
            s.creator.secondName,
            s.creator.lastName,
            s.creator.avatarUrl,
            s.creator.isDeleted,

            s.subscriber.id,

            s.isDeleted
          )
          FROM SubscribeEntity s
          WHERE
          s.isDeleted = false AND
          s.creator.isDeleted = false AND
          s.subscriber.id = :subscriberId AND
          s.subscriber.isDeleted = false
          """)
  List<SubscribeEntity> findAllCreatorsBySubscriberId(@Param("subscriberId") long subscriberId);

  @Query("""
          SELECT new com.xedlab.usersService.domain.subscribe.SubscribeEntity
          (
            s.creator.id,

            s.subscriber.id,
            s.subscriber.firstName,
            s.subscriber.secondName,
            s.subscriber.lastName,
            s.subscriber.avatarUrl,
            s.subscriber.isDeleted,

            s.isDeleted
          )
          FROM SubscribeEntity s
          WHERE
          s.isDeleted = false AND
          s.creator.isDeleted = false AND
          s.creator.id = :creatorId AND
          s.subscriber.isDeleted = false
          """)
  List<SubscribeEntity> findAllSubscribersByCreatorId(@Param("creatorId") long creatorId);
}
