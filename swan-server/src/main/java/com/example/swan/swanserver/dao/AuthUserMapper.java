package com.example.swan.swanserver.dao;


import com.example.swan.swanserver.model.po.DefaultAuthUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author feng
 * auth_user mapper
 */
@Mapper
public interface AuthUserMapper {


    int insert(DefaultAuthUser authUser);

    DefaultAuthUser selectAuthUserByUserId(String userId);

    DefaultAuthUser selectAuthUserByUsername(String username);

    DefaultAuthUser selectAuthUserByPhone(String phone);


    DefaultAuthUser selectAuthUserByEmail(String email);

    String selectUidByPhone(String phone);

    String selectUidByEmail(String email);

    String selectUidByUsername(String username);


}
