package com.bao.crm.controller;

import com.bao.crm.base.BaseController;
import com.bao.crm.base.ResultInfo;
import com.bao.crm.model.UserModel;
import com.bao.crm.query.UserQuery;
import com.bao.crm.service.UserService;
import com.bao.crm.utils.UserHolder;
import com.bao.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    @PostMapping("login")
    @ResponseBody
    public ResultInfo login(String userName, String userPwd, HttpServletResponse response) {
        ResultInfo resultInfo = new ResultInfo();
        UserModel userModel = null;
        userModel = userService.userLogin(userName, userPwd);
        resultInfo.setResult(userModel);

        return resultInfo;
    }

    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updatePwd(String oldPwd, String newPwd, String confirmPwd) {
        UserModel userModel = UserHolder.getUser();
        ResultInfo resultInfo = new ResultInfo();
        userService.updateUserPwd(userModel, oldPwd, newPwd, confirmPwd);
        return resultInfo;
    }

    @GetMapping("toPasswordPage")
    public String toPasswordPage() {
        return "user/password";
    }

    @GetMapping("queryAllSales")
    @ResponseBody
    public List<Map<String, Object>> queryAllSales(){
        List<Map<String, Object>> maps = userService.queryAllSales();
        return maps;
    }

    @GetMapping("list")
    @ResponseBody
    public Map<String, Object> queryUserByParams(UserQuery userQuery){
        return userService.queryByParamsForTable(userQuery);
    }

    @GetMapping("index")
    public String index(){
        return "user/user";
    }

    @GetMapping("toAddOrUpdateUserPage")
    public String toAddOrUpdateUserPage(Integer id, HttpServletRequest request){
        if(id != null){
            User user = userService.selectByPrimaryKey(id);
            request.setAttribute("userInfo", user);
        }
        return "user/add_update";
    }

    @PostMapping("add")
    @ResponseBody
    public ResultInfo addUser(User user){
        userService.addUser(user);
        return success();
    }

    @PostMapping("update")
    @ResponseBody
    public ResultInfo updateUser(User user){
        userService.updateUser(user);
        return success();
    }

    @PostMapping("delete")
    @ResponseBody
    public ResultInfo deleteUser(Integer[] ids){
        userService.deleteUsers(ids);
        return success();
    }
}
