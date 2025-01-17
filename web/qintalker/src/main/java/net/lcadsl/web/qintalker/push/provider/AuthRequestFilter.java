package net.lcadsl.web.qintalker.push.provider;


import com.google.common.base.Strings;
import net.lcadsl.web.qintalker.push.bean.api.base.ResponseModel;
import net.lcadsl.web.qintalker.push.bean.db.User;
import net.lcadsl.web.qintalker.push.factory.UserFactory;
import org.glassfish.jersey.server.ContainerRequest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.security.Principal;


/**
 * 用于所有的请求的接口的过滤和拦截
 */
@Provider
public class AuthRequestFilter implements ContainerRequestFilter {
    //继承实现接口的方法
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        //检测是否是登陆注册接口
        String relationPath=((ContainerRequest)requestContext).getPath(false);
        if (relationPath.startsWith("account/login")||relationPath.startsWith("account/register")){
            //直接返回，不做拦截
            return;
        }
        //从Headers中取到第一个token
        String token=requestContext.getHeaders().getFirst("token");
        if (!Strings.isNullOrEmpty(token)){
            //查询自己信息
            final User self= UserFactory.findByToken(token);
            if (self!=null){
                //给当前请求添加一个上下文
                requestContext.setSecurityContext(new SecurityContext() {
                    //主体部分
                    @Override
                    public Principal getUserPrincipal() {
                        return self;
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return true;
                    }

                    @Override
                    public boolean isSecure() {
                        //默认false，HTTPS
                        return false;
                    }

                    @Override
                    public String getAuthenticationScheme() {
                        return null;
                    }
                });
                //写入上下文后就返回
                return;
            }

        }

        //返回一个账户需要登录的model
        ResponseModel model= ResponseModel.buildAccountError();
        //构建一个返回
        Response response=Response.status(Response.Status.OK)
                .entity(model)
                .build();
        //停止一个请求的继续下发，调用该方法后直接返回请求
        //不会走到Service中
        requestContext.abortWith(response);

    }
}
