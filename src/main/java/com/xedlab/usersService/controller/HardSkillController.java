package com.xedlab.usersService.controller;

import com.xedlab.usersService.domain.hardSkill.HardSkillService;
import com.xedlab.usersService.domain.hardSkill.dto.HardSkillReadDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hard-skill")
@Tag(name = "Hard skill Controller", description = "Hard skill REST API")
public class HardSkillController {

  private final HardSkillService hardSkillService;

  @GetMapping("/{userId}")
  @Operation(summary = "Get All HardSkillReadDto by User id.")
  public List<HardSkillReadDto> getAllByUserId(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long userId
  ) {
    return hardSkillService.getAllByUserId(userId);
  }

  @PostMapping("/{userId}")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(summary = "Add Hard skill to User by User id.")
  public void addToUser(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long userId,
          @RequestParam(name = "skillName") String skillName

  ) {
    hardSkillService.addToUser(userId, skillName);
  }

  @DeleteMapping("/{userId}")
  @Operation(summary = "Remove Hard skill from User by User id.")
  public void removeFromUser(
          @PathVariable
          @Min(value = 1, message = "User id must be more then 0.") long userId,
          @RequestParam(name = "skillName") String skillName
  ) {
    hardSkillService.removeFromUser(userId, skillName);
  }
}
