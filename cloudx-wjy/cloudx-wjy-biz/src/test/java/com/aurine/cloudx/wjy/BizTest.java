package com.aurine.cloudx.wjy;

import com.aurine.cloudx.wjy.utils.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ： huangjj
 * @date ： 2021/4/25
 * @description： 业务测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class BizTest {
    @Test
    public void RedisTest(){
        Object object = RedisUtil.lPop("wjy_project"+1000000232);
        System.out.println(object.toString());
        RedisUtil.lLeftSet("wjy_project"+1000000232,object);
        Object object2 = RedisUtil.lPop("wjy_project"+1000000232);
        System.out.println(object2.toString());
    }
}