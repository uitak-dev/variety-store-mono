package com.demo.variety_store_mono.admin.controller;

import com.demo.variety_store_mono.admin.request.GlobalOptionRequest;
import com.demo.variety_store_mono.admin.request.SearchOption;
import com.demo.variety_store_mono.admin.response.GlobalOptionResponse;
import com.demo.variety_store_mono.admin.service.GlobalOptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/options")
@RequiredArgsConstructor
public class GlobalOptionController {

    private final GlobalOptionService globalOptionService;

    /** 옵션 목록 조회 페이지 */
    @GetMapping
    public String optionList(@ModelAttribute SearchOption searchOption,
                               Pageable pageable, Model model) {

        Page<GlobalOptionResponse> optionList = globalOptionService.getOptionSearchList(searchOption, pageable);
        model.addAttribute("optionList", optionList);

        return "admin/content/option/option-list";
    }

    /** 옵션 상세 조회 페이지 */
    @GetMapping("/{optionId}")
    public String optionDetails(@PathVariable Long optionId, Model model) {

        GlobalOptionResponse option = globalOptionService.getGlobalOption(optionId);
        model.addAttribute("option", option);

        return "admin/content/option/option-detail";
    }

    /** 옵션 등록 페이지 */
    @GetMapping("/new")
    public String optionNew(Model model) {

        model.addAttribute("option", new GlobalOptionRequest());

        return "admin/content/option/option-new";
    }

    /** 옵션 등록 API */
    @PostMapping("/new")
    public String newOption(@ModelAttribute GlobalOptionRequest request,
                              RedirectAttributes redirectAttributes) {

        GlobalOptionResponse option = globalOptionService.createGlobalOption(request);
        redirectAttributes.addAttribute("optionId", option.getId());

        return "redirect:/admin/options/{optionId}";
    }

    /** 옵션 수정 페이지 */
    @GetMapping("/{optionId}/edit")
    public String optionUpdate(@PathVariable Long optionId, Model model) {

        GlobalOptionResponse option = globalOptionService.getGlobalOption(optionId);
        model.addAttribute("option", option);
        return "admin/content/option/option-edit";
    }

    /** 옵션 수정 API */
    @PostMapping("/{optionId}/edit")
    public String updateOption(@PathVariable Long optionId,
                                 GlobalOptionRequest request, Model model) {

        GlobalOptionResponse option = globalOptionService.updateGlobalOption(optionId, request);
        return "redirect:/admin/options/{optionId}";
    }
}
