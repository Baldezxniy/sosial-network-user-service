package com.xedlab.usersService.domain.users.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "schema_users")
public class UserEntity {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Embedded
//  @Column(name = "username")
  @AttributeOverride(name = "value", column = @Column(name = "username"))
  private Username username;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "email"))
  private Email email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "second_name")
  private String secondName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "sex")
  @Enumerated(EnumType.STRING)
  private Sex sex;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "mobile_phone"))
  private MobilePhone mobilePhone;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "city"))
  private City city;

  @Column(name = "birthday_at")
  private LocalDate birthdayAt;

  @Embedded
  @AttributeOverride(name = "value", column = @Column(name = "avatar_url"))
  private AvatarUrl avatarUrl;

  @Column(name = "bio")
  private String bio;

  @Column(name = "is_deleted")
  private boolean isDeleted;
}
