package com.xedlab.usersService.domain.hardSkill;

import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;
import com.xedlab.usersService.domain.users.CheckExistsUserService;
import com.xedlab.usersService.domain.users.entity.UserEntity;
import com.xedlab.usersService.exception.HardSkillNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultHardSkillService implements HardSkillService {

  private final HardSkillRepository hardSkillRepository;
  private final UsersHardSkillsRepository usersHardSkillsRepository;
  private final CheckExistsUserService checkExistsUserService;
  private final HardSkillMapper hardSkillMapper;

  @Override
  public List<HardSkillReadDto> getAllByUserId(long userId) {
    checkExistsUserService.existUserById(userId);

    return hardSkillRepository.findAllByUserId(userId).stream()
            .map(hardSkillMapper::toDto)
            .toList();
  }

  @Override
  @Transactional
  public void addToUser(long userId, String hardSkillName) {

    HardSkillsEntity foundOrSavedHardSkillEntity = hardSkillRepository.findBySkillName(hardSkillName)
            .orElseGet(() -> create(hardSkillName));

    UserEntity userEntity = UserEntity.builder()
            .id(userId)
            .build();

    UsersHardSkillsEntity usersHardSkillsEntity = UsersHardSkillsEntity.builder()
            .pk(new UserHardSkillsId(userId, foundOrSavedHardSkillEntity.getId()))
            .hardSkills(foundOrSavedHardSkillEntity)
            .user(userEntity)
            .build();

    usersHardSkillsRepository.save(usersHardSkillsEntity);
  }

  @Override
  @Transactional
  public void removeFromUser(long userId, String hardSkillName) {
    HardSkillsEntity foundHardSkillEntity = hardSkillRepository.findBySkillName(hardSkillName)
            .orElseThrow(() -> new HardSkillNotFoundException("Hard skill with name = %s was not found."
                    .formatted(hardSkillName)));

    usersHardSkillsRepository.deleteById(new UserHardSkillsId(userId, foundHardSkillEntity.getId()));

    int countHardSkills = usersHardSkillsRepository.countByHardSkills_Id(foundHardSkillEntity.getId());
    if (countHardSkills == 0) {
      delete(hardSkillName);
    }
  }

  private HardSkillsEntity create(String hardSkillName) {
    return hardSkillRepository.save(new HardSkillsEntity(hardSkillName));
  }

  private void delete(String hardSkillName) {
    hardSkillRepository.deleteBySkillName(hardSkillName);
  }
}
