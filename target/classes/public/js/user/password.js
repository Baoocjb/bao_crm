layui.use(['form', 'jquery', 'jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    form.on('submit(saveBtn)', function (data) {
        // console.log(data);
        $.ajax({
            type:"post",
            url:ctx + "/user/updatePwd",
            data:{
                oldPwd:data.field.old_password,
                newPwd:data.field.new_password,
                confirmPwd:data.field.again_password
            },
            success:function (result){
                if(result.code == 200){
                    layer.msg("修改密码成功,系统将在3秒内跳转至登陆界面...", function (){
                        $.removeCookie("userIdStr", {domain:"localhost", path:"/"});
                        $.removeCookie("userName", {domain:"localhost", path:"/"});
                        $.removeCookie("trueName", {domain:"localhost", path:"/"});
                        $.removeCookie("email", {domain:"localhost", path:"/"})
                        var timeSecond = 1;
                        setInterval(function (){
                            --timeSecond;
                            if(timeSecond == 0){
                                window.parent.location.href = ctx + "/index";
                            }
                        }, 1000);
                    });
                }else{
                    layer.msg(result.msg, {icon:5});
                }
            }
        });
    });
});