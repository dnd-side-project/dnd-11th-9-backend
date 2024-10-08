package com._119.wepro.project.presentation;

import com._119.wepro.global.util.SecurityUtil;
import com._119.wepro.project.dto.request.ProjectMemberRequest.ProjectMemberCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectCreateRequest;
import com._119.wepro.project.dto.request.ProjectRequest.ProjectUpdateRequest;
import com._119.wepro.project.dto.response.MyProjectResponse;
import com._119.wepro.project.dto.response.ProjectDetailResponse;
import com._119.wepro.project.dto.response.ProjectListResponse;
import com._119.wepro.project.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
@Slf4j
public class ProjectController {

  private final ProjectService projectService;
  private final SecurityUtil securityUtil;

  @GetMapping()
  @Operation(summary = "프로젝트 검색", description = "키워드로 프로젝트를 검색합니다. 키워드 = 프로젝트 이름 혹은 프로젝트 태그")
  @ApiResponse(
      responseCode = "200",
      description = "태그는 문자열로 보내주시면 됩니다",
      content = @Content(
          examples = @ExampleObject(
              value = "[\n"
                  + "    {\n"
                  + "        \"name\": \"project name\",\n"
                  + "        \"memberNum\": 2,\n"
                  + "        \"imgUrl\": [\n"
                  + "            \"https://wepro1.s3.ap-northeast-2.amazonaws.com/profile/33d3aa6366c4698e8362c166e4022f7e8ae9c6e2887efc30e555c08783f52396.jpg\",\n"
                  + "            \"https://wepro1.s3.ap-northeast-2.amazonaws.com/profile/33d3aa6366c4698e8362c166e4022f7e8ae9c6e2887efc30e555c08783f52396.jpg\"\n"
                  + "        ],\n"
                  + "        \"tag\": \"9\"\n"
                  + "    }\n"
                  + "]"
          )
      )
  )
  public ResponseEntity<List<ProjectListResponse>> searchProjects(
      @RequestParam("key") String keyword) {
    List<ProjectListResponse> result = projectService.searchProjects(keyword);
    return ResponseEntity.ok(result);
  }

  @GetMapping("/my")
  @Operation(summary = "내가 속한 프로젝트 조회", description = "내가 프로젝트 멤버로 속해 있는 모든 프로젝트를 검색합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "memberNum = 프로젝트 멤버 총인원 수, reviewCompleteCount = 리뷰를 완료한 인원수",
      content = @Content(
          examples = @ExampleObject(
              value = "[\n"
                  + "    {\n"
                  + "        \"id\": 9,\n"
                  + "        \"name\": \"project name\",\n"
                  + "        \"imgUrl\": \"https://wepro1.s3.ap-northeast-2.amazonaws.com/profile/33d3aa6366c4698e8362c166e4022f7e8ae9c6e2887efc30e555c08783f52396.jpg\",\n"
                  + "        \"memberNum\": 2,\n"
                  + "        \"reviewCompleteCount\": 0\n"
                  + "    },\n"
                  + "    {\n"
                  + "        \"id\": 10,\n"
                  + "        \"name\": \"project name2\",\n"
                  + "        \"imgUrl\": \"https://wepro1.s3.ap-northeast-2.amazonaws.com/profile/33d3aa6366c4698e8362c166e4022f7e8ae9c6e2887efc30e555c08783f52396.jpg\",\n"
                  + "        \"memberNum\": 2,\n"
                  + "        \"reviewCompleteCount\": 0\n"
                  + "    }\n"
                  + "]"
          )
      )
  )
  public ResponseEntity<List<MyProjectResponse>> getMyProjects() {
    List<MyProjectResponse> myProjects = projectService.getMyProjects(
        securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(myProjects);
  }

  @GetMapping("/{id}")
  @Operation(summary = "프로젝트 상세 조회", description = "특정 프로젝트에 대해서 상세조회를 합니다.")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          examples = @ExampleObject(
              value = "{\n"
                  + "    \"name\": \"project name\",\n"
                  + "    \"desc\": \"project desc\",\n"
                  + "    \"imgUrls\": [\n"
                  + "        \"https://wepro1.s3.ap-northeast-2.amazonaws.com/profile/33d3aa6366c4698e8362c166e4022f7e8ae9c6e2887efc30e555c08783f52396.jpg\"\n"
                  + "    ],\n"
                  + "    \"startDate\": \"2024-08-16\",\n"
                  + "    \"endDate\": \"2024-08-19\",\n"
                  + "    \"memberList\": [\n"
                  + "        {\n"
                  + "            \"id\": 2,\n"
                  + "            \"name\": \"조민제2\",\n"
                  + "            \"tag\": \"1\"\n"
                  + "        },\n"
                  + "        {\n"
                  + "            \"id\": 1,\n"
                  + "            \"name\": \"조민제\",\n"
                  + "            \"tag\": \"1\"\n"
                  + "        }\n"
                  + "    ],\n"
                  + "    \"linkList\": \"project Link\"\n"
                  + "}"
          )
      )
  )
  public ResponseEntity<ProjectDetailResponse> getProjectDetail(@PathVariable("id") Long id) {
    ProjectDetailResponse result = projectService.getProjectDetail(id);
    return ResponseEntity.ok(result);
  }

  @PostMapping()
  @Operation(summary = "프로젝트 생성", description = "프로젝트 생성")
  @ApiResponse(
      responseCode = "200",
      description = "새롭게 생성된 프로젝트의 ID값 반환",
      content = @Content(
          examples = @ExampleObject(
              value = "11"
          )
      )
  )
  public ResponseEntity<Long> createProject(
      @RequestBody ProjectCreateRequest projectCreateRequest
  ) {
    Long projectId = projectService.createProject(projectCreateRequest,
        securityUtil.getCurrentMemberId());
    return ResponseEntity.ok(projectId);
  }

  @PutMapping("/{id}")
  @Operation(summary = "프로젝트 수정", description = "프로젝트 수정")
  @ApiResponse(
      responseCode = "200",
      description = "성공시 수정된 프로젝트의 ID 반환",
      content = @Content(
          examples = @ExampleObject(
              value = "9"
          )
      )
  )
  public ResponseEntity<Long> updateProject(@PathVariable("id") Long id,
      @RequestBody ProjectUpdateRequest projectUpdateRequest) {
    //todo: 권한 처리
    Long projectId = projectService.updateProject(id, projectUpdateRequest);
    return ResponseEntity.ok(projectId);
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "프로젝트 삭제", description = "프로젝트 삭제")
  @ApiResponse(
      responseCode = "200",
      description = "성공시 삭제된 프로젝트의 ID 반환",
      content = @Content(
          examples = @ExampleObject(
              value = "7"
          )
      )
  )
  public ResponseEntity<Long> deleteProject(@PathVariable Long id) {
    //todo: 권한 처리
    return ResponseEntity.ok(projectService.deleteProject(id));
  }

  @PostMapping("/{id}/member")
  @Operation(summary = "[미사용] 프로젝트 멤버 추가", description = "")
  @ApiResponse(
      responseCode = "200",
      description = "성공",
      content = @Content(
          examples = @ExampleObject(
              value = ""
          )
      )
  )
  public ResponseEntity<Void> addMember(@RequestBody ProjectMemberCreateRequest dto,
      @PathVariable("id") Long id) {
    projectService.addProjectMember(dto.getMemberId(), id);
    return ResponseEntity.ok(null);
  }
}