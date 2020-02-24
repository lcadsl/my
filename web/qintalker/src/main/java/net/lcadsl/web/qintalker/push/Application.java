package net.lcadsl.web.qintalker.push;

import net.lcadsl.web.qintalker.push.provider.GsonProvider;
import net.lcadsl.web.qintalker.push.service.AccountService;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

public class Application extends ResourceConfig {
    public Application(){
        //ע���߼�����İ�������
        //packages("net.lcadsl.web.qintalker.push.service");
        packages(AccountService.class.getPackage().getName());
        //Json������
        //register(JacksonJsonProvider.class);
        //�滻������ΪGson
        register(GsonProvider.class);
        //��־��ӡ���
        register(Logger.class);
    }
}
