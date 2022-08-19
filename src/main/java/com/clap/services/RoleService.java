package com.clap.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clap.model.ArtisticContent;
import com.clap.model.Role;
import com.clap.model.User;
import com.clap.repository.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final ArtisticContentService artisticContentService;

    public List<Role> getRolesByContentId(String artistic_content_id) {
        return roleRepository.findRolesByContentId(artistic_content_id);
    }

    public List<Role> getRolesByUsername(String username) {
        return roleRepository.findRolesByUsername(username);
    }
    public void addRole(Role role, String username, String artistic_content_id){
        User user = userService.getUserByUsername(username).orElse(null);
        ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
        role.setUser(user);
        role.setArtisticContent(artisticContent);
        roleRepository.save(role);
    }

    public void deleteRole(String user_id){
        roleRepository.deleteRole(user_id);
    }

    public void deleteRolesByContentId(String artistic_content_id){
        roleRepository.deleteRolesByContentId(artistic_content_id);
    }
}

