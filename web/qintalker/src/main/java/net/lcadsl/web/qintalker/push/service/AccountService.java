package net.lcadsl.web.qintalker.push.service;

import com.google.common.base.Strings;
import net.lcadsl.web.qintalker.push.bean.api.account.AccountRspModel;
import net.lcadsl.web.qintalker.push.bean.api.account.LoginModel;
import net.lcadsl.web.qintalker.push.bean.api.account.RegisterModel;
import net.lcadsl.web.qintalker.push.bean.api.base.ResponseModel;
import net.lcadsl.web.qintalker.push.bean.db.User;
import net.lcadsl.web.qintalker.push.factory.UserFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

//实际访问路径127.0.0.1/api/account/...
@Path("/account")
public class AccountService extends BaseService {
    //登录
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> login(LoginModel model) {

        if (!LoginModel.check(model)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.login(model.getAccount(), model.getPassword());
        if (user != null) {
            //如果有携带PushId
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }

            //返回当前账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);
        } else {
            //登录失败
            return ResponseModel.buildLoginError();
        }


    }


    //注册
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ResponseModel<AccountRspModel> register(RegisterModel model) {


        if (!RegisterModel.check(model)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }

        User user = UserFactory.findByPhone(model.getAccount().trim());

        if (user != null) {
            //已有账户
            return ResponseModel.buildHaveAccountError();
        }

        user = UserFactory.findByName(model.getName().trim());

        if (user != null) {
            //已有名字
            return ResponseModel.buildHaveNameError();
        }

        //开始注册逻辑
        user = UserFactory.register(model.getAccount(),
                model.getPassword(),
                model.getName());


        if (user != null) {
            //如果有携带PushId
            if (!Strings.isNullOrEmpty(model.getPushId())) {
                return bind(user, model.getPushId());
            }

            //返回当前账户
            AccountRspModel rspModel = new AccountRspModel(user);
            return ResponseModel.buildOk(rspModel);

        } else {
            //注册异常
            return ResponseModel.buildRegisterError();
        }

    }


    //绑定设备id
    @POST
    @Path("/bind/{pushId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //从请求头中获取token字段
    //pushId从url地址中获取
    public ResponseModel<AccountRspModel> bind(@HeaderParam("token") String token,
                                               @PathParam("pushId") String pushId) {

        if (Strings.isNullOrEmpty(token) || Strings.isNullOrEmpty(pushId)) {
            //返回参数异常
            return ResponseModel.buildParameterError();
        }
        //拿到自己的个人信息
        //User user = UserFactory.findByToken(token);

        User self = getSelf();

        return bind(self, pushId);

    }


    /**
     * 绑定的操作
     *
     * @param self   自己
     * @param pushId pushId
     * @return user
     */
    private ResponseModel<AccountRspModel> bind(User self, String pushId) {
        //进行设备Id绑定的操作
        User user = UserFactory.bindPushId(self, pushId);

        if (user == null) {
            return ResponseModel.buildServiceError();

        }
        //返回当前账户,并且已经绑定了
        AccountRspModel rspModel = new AccountRspModel(user, true);
        return ResponseModel.buildOk(rspModel);
    }
}


