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

    public List<Role> getRolesByContentIdAndUserId(String artistic_content_id,String user_id) {
        return roleRepository.findRolesByContentId(artistic_content_id, user_id);
    }

    public void addRole(Role role, String username, String artistic_content_id){
        User user = userService.getUserByUsername(username).orElse(null);
        ArtisticContent artisticContent = artisticContentService.getContentById(artistic_content_id).orElse(null);
        role.setUser(user);
        role.setArtisticContent(artisticContent);
        roleRepository.save(role);
    }

    public void deleteRole(Role role){
        roleRepository.delete(role);
    }
}
