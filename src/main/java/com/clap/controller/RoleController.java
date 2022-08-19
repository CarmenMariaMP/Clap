package com.clap.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.clap.model.Role;
import com.clap.model.Search;
import com.clap.model.Validators.RoleValidator;
import com.clap.services.ArtisticContentService;
import com.clap.services.RoleService;
import com.clap.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoleController {
    private final UserService userService;
    private final RoleService roleService;
    private final ArtisticContentService artisticContentService;
    private final RoleValidator roleValidator;

    @RequestMapping("/owner/{user_id}/content/{artistic_content_id}/role")
    public String addRolesView(@PathVariable String user_id, @PathVariable String artistic_content_id,
            Map<String, Object> model) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        List<Role> existingRoles = roleService.getRolesByContentId(artistic_content_id);
        model.put("role", new Role());
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("existingRoles", existingRoles);
        model.put("search", new Search());
        return "roles.html";
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/role")
    public String addRoles(@Valid @ModelAttribute("role") Role role,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model,
            BindingResult result) {
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        List<Role> existingRoles = roleService.getRolesByContentId(artistic_content_id);
        model.put("search", new Search());
        roleValidator.validate(role, result);
        if (result.hasErrors()) {
            return "roles.html";
        }
        try {
            roleService.addRole(role, role.getUsername(), artistic_content_id);
        } catch (Exception e) {
            return "roles.html";
        }
        model.put("existingRoles", existingRoles);
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        return String.format("redirect:/owner/%s/content/%s/role", user_id, artistic_content_id);
    }

    @PostMapping("/owner/{user_id}/content/{artistic_content_id}/delete_role/{role_id}")
    public String deleteRoles(@PathVariable String role_id,
            @PathVariable String user_id, @PathVariable String artistic_content_id, Map<String, Object> model) {
        String contentType = artisticContentService.getTypeById(artistic_content_id);
        String username = userService.getLoggedUser();
        if (username == null) {
            return "redirect:/login";
        }
        model.put("search", new Search());
        try {
            roleService.deleteRole(role_id);
        } catch (Exception e) {
            return "roles.html";
        }
        model.put("user_id", user_id);
        model.put("artistic_content_id", artistic_content_id);
        model.put("contentType", contentType);
        return String.format("redirect:/owner/%s/content/%s/role", user_id, artistic_content_id);
    }

}
