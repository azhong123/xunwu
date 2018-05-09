package com.spring.service.user.impl;

import com.spring.common.base.ServiceResult;
import com.spring.entity.Role;
import com.spring.entity.User;
import com.spring.repository.RoleRepository;
import com.spring.repository.UserRepository;
import com.spring.service.user.IUserService;
import com.spring.web.dto.UserDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户service 实现
 * Created by chenxizhong on 2018/5/5.
 */
@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ModelMapper modelMapper;


    /**
     * 通过用户名查找用户
     * @param name
     * @return
     */
    @Override
    public User findUserByName(String name) {
        User user = userRepository.findUserByName(name);
        if(user  == null){
            return null;
        }

        List<Role> roles = roleRepository.findRolesByUserId(user.getId());

        if(roles == null && CollectionUtils.isEmpty(roles)){
            throw  new DisabledException("权限非法");
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();
        roles.forEach(role -> {
            authorityList.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        });

        user.setAuthorityList(authorityList);
        return user;
    }

    /**
     * 查找用户信息
     * @param userId
     * @return
     */
    @Override
    public ServiceResult<UserDTO> findById(Long userId) {
        User user = userRepository.findOne(userId);
        if(user == null){
            return ServiceResult.notFound();
        }

        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
        return ServiceResult.of(userDTO);
    }
}
