layui.use(['form', 'jquery', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on('submit(login)', function (data) {
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        $.ajax({
            type: "post",
            url: ctx + "/user/login",
            data: {
                userName:data.field.username,
                userPwd:data.field.password
            },
            success:function (result){
                console.log(result);
                if(result.code == 200){
                    layer.msg("登录成功!", function (){
                        // 判断用户是否选择记住密码（判断复选框是否被选中，如果选中，则设置cookie对象7天生效）
                        if ($("#rememberMe").prop("checked")) {
                            // 选中，则设置cookie对象7天生效
                            // 将用户信息设置到cookie中
                            $.cookie("userIdStr",result.result.userIdStr, {expires:7});
                            $.cookie("userName",result.result.userName, {expires:7});
                            $.cookie("trueName",result.result.trueName, {expires:7});
                        }else {
                            $.cookie("userIdStr",result.result.userIdStr);
                            $.cookie("userName",result.result.userName);
                            $.cookie("trueName",result.result.trueName);
                        }
                        window.location.href = ctx + "/main";

                        layer.msg(result.msg,{icon:6});
                    });
                }else{
                    layer.msg(result.msg,{icon:5});
                }
            }
        });
        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
});