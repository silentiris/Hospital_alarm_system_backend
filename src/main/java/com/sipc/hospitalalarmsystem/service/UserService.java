package com.sipc.hospitalalarmsystem.service;

/**
 * @author CZCZCZ
 * &#064;date 2023-09-10 17:27
 */
public interface UserService {

    String login(String phone, String password);

    Boolean register(String username, String password, Integer role);

    Boolean updatePassword(Integer id, String oldPassword, String newPassword);

    Boolean updateName(Integer id, String name);


}
