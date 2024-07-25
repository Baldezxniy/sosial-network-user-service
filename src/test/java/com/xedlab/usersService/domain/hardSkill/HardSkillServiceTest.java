package com.xedlab.usersService.domain.hardSkill;

import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;
import com.xedlab.usersService.domain.users.CheckExistsUserService;
import com.xedlab.usersService.exception.HardSkillNotFoundException;
import com.xedlab.usersService.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HardSkillServiceTest {

  @InjectMocks
  private DefaultHardSkillService hardSkillService;

  @Mock
  private HardSkillRepository hardSkillRepository;
  @Mock
  private UsersHardSkillsRepository usersHardSkillsRepository;
  @Mock
  private CheckExistsUserService checkExistsUserService;
  @Mock
  private HardSkillMapper hardSkillMapper;

  @Test
  void getAllByUserId_shouldGetUserHardSkillList_whenUserExists() {
    // Arrange
    long userId = 1L;
    doNothing().when(checkExistsUserService).existUserById(userId);
    HardSkillsEntity hardSkillEntity = new HardSkillsEntity(1L, "SkillName-1");
    List<HardSkillsEntity> hardSkillsEntities = List.of(hardSkillEntity);
    HardSkillReadDto hardSkillDto = new HardSkillReadDto(1L, "SkillName-1");

    when(hardSkillRepository.findAllByUserId(userId))
            .thenReturn(hardSkillsEntities);

    when(hardSkillMapper.toDto(hardSkillEntity)).thenReturn(hardSkillDto);

    // Act
    List<HardSkillReadDto> result = hardSkillService.getAllByUserId(userId);

    // Asserts
    assertThat(result.size()).isEqualTo(hardSkillsEntities.size());
    assertThat(result.get(0).id()).isEqualTo(hardSkillDto.id());
  }

  @Test
  void getAllByUserId_shouldThrowException_whenUserNotFound() {
    // Arrange
    long userId = 1L;
    String errorMessage = "User was not found.";
    doThrow(new UserNotFoundException(errorMessage)).when(checkExistsUserService).existUserById(userId);

    // Act
    UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
      hardSkillService.getAllByUserId(userId);
    });

    // Asserts
    assertThat(exception.getMessage()).isEqualTo(errorMessage);
  }

  @Test
  void addToUser_shouldAddSkillToUser_whenSkillExists() {
    // Arrange
    long userId = 1L;
    String skillName = "SkillName";
    HardSkillsEntity hardSkillEntity = new HardSkillsEntity(skillName);

    when(hardSkillRepository.findBySkillName(skillName)).thenReturn(Optional.of(hardSkillEntity));

    when(usersHardSkillsRepository.save(any(UsersHardSkillsEntity.class)))
            .thenReturn(any(UsersHardSkillsEntity.class));

    // Act
    hardSkillService.addToUser(userId, skillName);

    // Asserts
    verify(hardSkillRepository, times(1)).findBySkillName(skillName);
    verify(usersHardSkillsRepository, times(1)).save(any(UsersHardSkillsEntity.class));
  }

  @Test
  void addToUser_shouldAddSkillToUser_whenSkillNotExists() {
    // Arrange
    long userId = 1L;
    long hardSkillId = 1L;
    String skillName = "SkillName";

    when(hardSkillRepository.findBySkillName(skillName)).thenReturn(Optional.empty());

    when(hardSkillRepository.save(any(HardSkillsEntity.class)))
            .thenReturn(new HardSkillsEntity(hardSkillId, skillName));

    when(usersHardSkillsRepository.save(any(UsersHardSkillsEntity.class)))
            .thenReturn(any(UsersHardSkillsEntity.class));

    // Act
    hardSkillService.addToUser(userId, skillName);

    // Asserts
    verify(hardSkillRepository, times(1)).findBySkillName(skillName);
    verify(usersHardSkillsRepository, times(1)).save(any(UsersHardSkillsEntity.class));
  }

  @Test
  void removeFromUser_shouldRemoveSkillFromUser_whenSkillExistsAndUserNotLastOwnerOfSkill() {
    // Arrange
    long userId = 1L;
    long hardSkillId = 1L;
    String skillName = "SkillName";
    HardSkillsEntity foundEntity = new HardSkillsEntity(hardSkillId, skillName);

    when(hardSkillRepository.findBySkillName(skillName)).thenReturn(Optional.of(foundEntity));

    doNothing().when(usersHardSkillsRepository)
            .deleteById(new UserHardSkillsId(userId, hardSkillId));

    when(usersHardSkillsRepository.countByHardSkills_Id(hardSkillId)).thenReturn(100);

    // Act
    hardSkillService.removeFromUser(userId, skillName);

    // Asserts
    verify(hardSkillRepository, times(1)).findBySkillName(skillName);
    verify(usersHardSkillsRepository, times(1)).deleteById(any(UserHardSkillsId.class));
    verify(usersHardSkillsRepository, times(1)).countByHardSkills_Id(hardSkillId);
  }

  @Test
  void removeFromUser_shouldRemoveSkillFromUser_whenSkillExistsAndUserLastOwnerOfSkill() {
    // Arrange
    long userId = 1L;
    long hardSkillId = 1L;
    String skillName = "SkillName";
    HardSkillsEntity foundEntity = new HardSkillsEntity(hardSkillId, skillName);

    when(hardSkillRepository.findBySkillName(skillName)).thenReturn(Optional.of(foundEntity));

    doNothing().when(usersHardSkillsRepository)
            .deleteById(new UserHardSkillsId(userId, hardSkillId));

    when(usersHardSkillsRepository.countByHardSkills_Id(hardSkillId)).thenReturn(0);

    doNothing().when(hardSkillRepository).deleteBySkillName(skillName);

    // Act
    hardSkillService.removeFromUser(userId, skillName);

    // Asserts
    verify(hardSkillRepository, times(1)).findBySkillName(skillName);
    verify(usersHardSkillsRepository, times(1)).deleteById(any(UserHardSkillsId.class));
    verify(usersHardSkillsRepository, times(1)).countByHardSkills_Id(hardSkillId);
    verify(hardSkillRepository, times(1)).deleteBySkillName(skillName);
  }

  @Test
  void removeFromUser_shouldRemoveSkillFromUser_whenSkillNotFound() {
    // Arrange
    long userId = 1L;
    String skillName = "SkillName";

    when(hardSkillRepository.findBySkillName(skillName)).thenReturn(Optional.empty());

    // Act
    HardSkillNotFoundException exception = assertThrows(HardSkillNotFoundException.class, () -> {
      hardSkillService.removeFromUser(userId, skillName);
    });

    // Asserts
    assertThat(exception.getMessage()).isEqualTo("Hard skill with name = %s was not found.".formatted(skillName));
    verify(hardSkillRepository, times(1)).findBySkillName(skillName);
    verify(usersHardSkillsRepository, never()).deleteById(any());
    verify(usersHardSkillsRepository, never()).countByHardSkills_Id(anyLong());
    verify(hardSkillRepository, never()).deleteBySkillName(anyString());
  }
}