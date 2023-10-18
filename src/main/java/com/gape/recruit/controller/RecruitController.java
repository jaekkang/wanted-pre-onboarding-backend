package com.gape.recruit.controller;

import com.gape.recruit.domain.Company;
import com.gape.recruit.domain.Recruit;
import com.gape.recruit.domain.Users;
import com.gape.recruit.dto.recruit.RecruitDto;
import com.gape.recruit.service.CompanyService;
import com.gape.recruit.service.RecruitService;
import com.gape.recruit.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;
    private final CompanyService companyService;
    private final UserService userService;


    @GetMapping("/api/recruits/{id}")
    public Result findOne(@PathVariable("id") Long id) {
        Recruit recruit = recruitService.findOne(id);
        RecruitDto dto = new RecruitDto(recruit);
        return new Result(dto);
    }

    @GetMapping("/api/recruits/{id}/detail")
    public Result findOneDetail(@PathVariable("id") Long id) {
        Recruit recruit = recruitService.findOne(id);
        RecruitDto.FindRecruitDetailResponse dto = new RecruitDto.FindRecruitDetailResponse(recruit);
        return new Result(dto);
    }

    @GetMapping("/api/recruits/search")
    public Optional<List<RecruitDto>> findByKeyword(@RequestParam String search) {
        Optional<List<Recruit>> recruits = recruitService.findByKeyword(search);
        return Optional.of(recruits.orElse(Collections.emptyList())
                .stream()
                .map(m -> new RecruitDto(m))
                .toList());
    }

    @PostMapping("/api/recruits")
    public RecruitDto.CreateRecruitResponse post(@RequestBody @Valid RecruitDto.CreateRecruitRequest request) {
        Company company = companyService.findOne(request.getCompanyId());
        Recruit recruit = Recruit.createRecruit(company, request.getPosition(), request.getReward(), request.getTechStack(), request.getContent());
        Long recruitId = recruitService.post(recruit);

        return new RecruitDto.CreateRecruitResponse(recruitId);
    }

    @PatchMapping("/api/recruits/{id}/apply")
    public RecruitDto.ApplyRecruitResponse apply(@PathVariable("id") Long id, @RequestBody @Valid RecruitDto.ApplyRecruitRequest request) {
        recruitService.findOne(id);
        Users user = userService.findOne(request.getUserId());
        return new RecruitDto.ApplyRecruitResponse(user, recruitService.apply(id, user));
    }

    @PatchMapping("/api/recruits/{id}")
    public RecruitDto.UpdateRecruitResponse UpdateRecruit(
            @PathVariable("id") Long id,
            @RequestBody @Valid RecruitDto.UpdateRecruitRequest request) {
        Recruit recruit = recruitService.findOne(id);

        recruitService.update(id, request);
        return new RecruitDto.UpdateRecruitResponse(recruit);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class UpdateRecruitResponse {
        private String name;
    }

    @GetMapping("/api/recruits")
    public Result finfAllRecruits() {
        List<Recruit> findRecruits = recruitService.findAll();
        List<RecruitDto> collect = findRecruits.stream()
                .map(m -> new RecruitDto(m))
                .collect(Collectors.toList());
        return new Result(collect);
    }

    @DeleteMapping("/api/recruits/{id}")
    public Result deleteRecruit(@PathVariable Long id) {
        Recruit recruit = recruitService.findOne(id);
        recruitService.delete(recruit.getId());
        return new Result(id);
    }
}
