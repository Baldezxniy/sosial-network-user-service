package com.xedlab.usersService.domain.users;

import com.xedlab.usersService.domain.users.entity.Email;
import com.xedlab.usersService.domain.users.entity.MobilePhone;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import com.xedlab.usersService.domain.users.entity.Username;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

  @Query("""
          SELECT u FROM UserEntity u
          WHERE u.id = :userId AND u.isDeleted = false
          """)
  Optional<UserEntity> findById(@Param("userId") long userId);

  @Modifying
  @Query("UPDATE UserEntity SET isDeleted = true WHERE id = :id")
  void softDeleteById(@Param("id") long id);

  @Query(value = """
          SELECT
              CASE WHEN COUNT(u) > 0 THEN true ELSE false END
              FROM UserEntity u
              WHERE u.id = :id AND u.isDeleted = false
          """)
  boolean existsById(@Param("id") long id);

  @Query(value = """
          SELECT
              CASE WHEN COUNT(u) > 0 THEN true ELSE false END
              FROM UserEntity u
              WHERE u.username = :username AND u.isDeleted = false
          """)
  boolean existsByUsername(@Param("username") Username username);

  @Query("""
            SELECT
              CASE WHEN COUNT(u) > 0 THEN true ELSE false END
              FROM UserEntity u
              WHERE u.email = :email AND u.isDeleted = false
          """)
  boolean existsByEmail(@Param("email") Email email);

  @Query("""
          SELECT
              CASE WHEN COUNT(u) > 0 THEN true ELSE false END
              FROM UserEntity u
              WHERE u.mobilePhone = :mobilePhone AND u.isDeleted = false
          """)
  boolean existsByMobilePhone(@Param("mobilePhone") MobilePhone mobilePhone);
}
