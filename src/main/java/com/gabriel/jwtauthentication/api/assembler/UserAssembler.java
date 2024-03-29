package com.gabriel.jwtauthentication.api.assembler;

import com.gabriel.jwtauthentication.api.domain.UserModel;
import com.gabriel.jwtauthentication.api.domain.UserRegisterRequest;
import com.gabriel.jwtauthentication.domain.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

    @Autowired
    private ModelMapper modelMapper;

    public UserModel toModel(User user) {
        return modelMapper.map(user, UserModel.class);
    }
    public User toEntity(UserRegisterRequest request) {
        return modelMapper.map(request, User.class);
    }

}
