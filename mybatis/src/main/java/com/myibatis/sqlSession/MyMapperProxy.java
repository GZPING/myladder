package com.myibatis.sqlSession;

/**
 * Created by GD on 2018/10/23.
 * Blog: https://blog.csdn.net/SaketGD
 * GitHub: https://github.com/GZPING
 */

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 *
 * @Description
 * @Author GD
 * @Date 2018/10/23 22:34
 * @Since 1.0V
 */

public class MyMapperProxy implements InvocationHandler {
    private  MySqlSession mySqlsession;

    private MyConfiguration myConfiguration;

    public MyMapperProxy(MyConfiguration myConfiguration,MySqlSession mySqlsession) {
        this.myConfiguration=myConfiguration;
        this.mySqlsession=mySqlsession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        MapperBean readMapper = myConfiguration.readMapper("UserMapper.xml");
        //是否是xml文件对应的接口
        if(!method.getDeclaringClass().getName().equals(readMapper.getInterfaceName())){
            return null;
        }
        List<Function> list = readMapper.getList();
        if(null != list || 0 != list.size()){
            for (Function function : list) {
                //id是否和接口方法名一样
                if(method.getName().equals(function.getFuncName())){
                    return mySqlsession.selectOne(function.getSql(), String.valueOf(args[0]));
                }
            }
        }
        return null;
    }
}