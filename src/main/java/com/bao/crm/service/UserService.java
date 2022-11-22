package com.bao.crm.service;

import com.bao.crm.base.BaseService;
import com.bao.crm.dao.UserMapper;
import com.bao.crm.model.UserModel;
import com.bao.crm.utils.AssertUtil;
import com.bao.crm.utils.LoginUserUtil;
import com.bao.crm.utils.Md5Util;
import com.bao.crm.utils.UserIDBase64;
import com.bao.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static com.bao.crm.utils.LoginUserUtil.buildUserInfo;

@Service
public class UserService extends BaseService<User, Integer> {
    @Resource
    private UserMapper userMapper;

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

    public List<Map<String, Object>> queryAllSales(){
        return userMapper.queryAllSales();
    }
}
