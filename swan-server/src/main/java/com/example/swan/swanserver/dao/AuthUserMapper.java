package com.example.swan.swanserver.dao;


import com.example.swan.swanserver.model.po.DefaultAuthUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 根据userId 修改密码
     * @param userId 用户id
     * @param password 密码
     * @return 受影响行数
     */
    int updatePasswordByUserId(@Param("userId") String userId, @Param("password") String password);

    int selectCountByUserId(String userId);
}
