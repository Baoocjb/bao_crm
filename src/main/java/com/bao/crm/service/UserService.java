package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.UserMapper;
import com.bao.crm.dao.UserRoleMapper;
import com.bao.crm.model.UserModel;
import com.bao.crm.utils.*;
import com.bao.crm.vo.User;
import com.bao.crm.vo.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    public UserModel userLogin(String userName, String userPwd) {
        // 用户输入参数判空
        checkLoginParams(userName, userPwd);
        // 通过用户账号查询用户
        User user = userMapper.queryUserByName(userName);
        AssertUtil.isTrue(user == null, "用户账号不存在");
        // 用户存在,校对密码
        checkUserPwd(user.getUserPwd(), userPwd);
        // 密码正确,构建UserModel返回
        UserModel userModel = LoginUserUtil.buildUserInfo(user);
        return userModel;
    }


    /**
     * 校验用户密码
     *
     * @param userPwd
     * @param pwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        AssertUtil.isTrue(!userPwd.equals(Md5Util.encode(pwd)), "用户密码不正确!");
    }

    /**
     * 校验登陆参数是否为空
     *
     * @param userName
     * @param userPwd
     */
    private void checkLoginParams(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName), "用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd), "用户密码不能为空");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUserPwd(UserModel userModel, String oldPwd, String newPwd, String confirmPwd) {
        // 校验是否为空
        checkPwdParams(oldPwd, newPwd, confirmPwd);
        // 都不为空则对比旧密码是否正确
        Integer userId = UserIDBase64.decoderUserID(userModel.getUserIdStr());
        User user = userMapper.selectByPrimaryKey(userId);
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)), "旧密码错误!");
        // 旧密码正确,校验新密码是否和旧密码相同
        AssertUtil.isTrue(oldPwd.equals(newPwd), "新密码不能与旧密码相同!");
        // 新密码和确认密码是否一致
        AssertUtil.isTrue(!newPwd.equals(confirmPwd), "新密码与确认密码不一致!");
        // 修改密码
        int count = userMapper.updatePwdById(userId, Md5Util.encode(newPwd));
        AssertUtil.isTrue(count < 1, "修改密码失败!");

    }

    /**
     * 修改密码参数校验
     *
     * @param oldPwd
     * @param newPwd
     * @param confirmPwd
     */
    private void checkPwdParams(String oldPwd, String newPwd, String confirmPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd), "旧密码不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd), "新密码不能为空!");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd), "确认密码不能为空!");
    }

    /**
     * 查询所有角色为销售的用户
     * @return
     */
    public List<Map<String, Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }

    /**
     * 新增用户
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        // 参数校验
        checkUserParams(user);
        // 设置默认值
        user.setUserPwd(Md5Util.encode("123456"));
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        user.setIsValid(1);
        AssertUtil.isTrue(userMapper.insertSelective(user) != 1, "用户添加失败!");

        // 用户关联角色属性
        relationUserWithRole(user.getId(), user.getRoleIds());
    }

    /**
     * 用户关联角色
     */
    private void relationUserWithRole(Integer id, String roleIds) {
        // 查询用户是否已经关联了角色
        User user = userMapper.selectByPrimaryKey(id);
        // 如果已经关联了角色
        Integer count = userRoleMapper.countUserRelationRole(user.getId());
        if(count > 0){
            // 删除该用户关联的所有角色
            AssertUtil.isTrue(userRoleMapper.delectUserRelationRole(user.getId()) < 1, "用户关联角色失败!");
        }
        if(StringUtils.isBlank(roleIds)){
            return;
        }
        // 为该用户关联角色
        String[] roleIdsArray = roleIds.split(",");
        List<UserRole> list = new ArrayList<>();
        for(String roleId : roleIdsArray){
            UserRole userRole = new UserRole();
            userRole.setRoleId(Integer.parseInt(roleId));
            userRole.setUserId(user.getId());
            userRole.setCreateDate(new Date());
            userRole.setUpdateDate(new Date());
            list.add(userRole);
        }
        Integer resCount = userRoleMapper.insertBatch(list);
        AssertUtil.isTrue(resCount != list.size(), "用户关联角色失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateUser(User user){
        checkUserParams(user);
        User oldUser = userMapper.selectByPrimaryKey(user.getId());
        AssertUtil.isTrue(user.getUserPwd() != null && !oldUser.getUserPwd().equals(Md5Util.encode(user.getUserPwd())), "修改密码非法请求!");
        user.setUpdateDate(new Date());
        userMapper.updateByPrimaryKeySelective(user);
        // 用户关联角色属性
        relationUserWithRole(user.getId(), user.getRoleIds());
    }

    private void checkUserParams(User user) {
        AssertUtil.isTrue(user == null, "用户不能为空");
        AssertUtil.isTrue(user.getUserName() == null, "用户姓名不能为空!");
        AssertUtil.isTrue(user.getEmail() == null, "用户邮箱不能为空!");
        AssertUtil.isTrue(user.getPhone() == null, "手机号码不能为空!");
        AssertUtil.isTrue(!PhoneUtil.isMobile(user.getPhone()), "手机号码格式不正确");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteUsers(Integer[] ids) {
        AssertUtil.isTrue(ids == null || ids.length == 0, "待删除用户不存在!");
        // 删除用户
        AssertUtil.isTrue(userMapper.deleteBatch(ids) != ids.length, "用户删除失败!");
        // 删除用户关联角色
        for(Integer userId : ids){
            Integer countUserRelationRole = userRoleMapper.countUserRelationRole(userId);
            Integer delectCount = userRoleMapper.delectUserRelationRole(userId);
            AssertUtil.isTrue(countUserRelationRole != delectCount, "用户角色删除失败!");
        }
    }
}
