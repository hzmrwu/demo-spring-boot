package com.mk.demoonspringboot.hystrix;

import com.mk.demoonspringboot.mybatis_plus.entity.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

public class GetProductInfoCommand extends HystrixCommand<User> {

    private Long productId;

    public GetProductInfoCommand(Long productId) {
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoCommandGroup"));
        /* 也可以设置信号量隔离策略
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetCityNameGroup"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)));*/
        this.productId = productId;
    }

    @Override
    protected User run() throws Exception {
        /*String url = "http://localhost:8081/getProductInfo?productId=" + productId;
        // 调用商品服务接口
        String response = HttpClientUtils.sendGetRequest(url);
        return JSONObject.parseObject(response, ProductInfo.class);*/
        //假装http跨服务请求
        User user = new User();
        user.setId(productId);
        return user;
    }

    //线程池实现资源隔离
    public static void main(String[] args) {
        HystrixCommand<User> getProductInfoCommand = new GetProductInfoCommand(123L);
        // 通过command执行，获取最新商品数据
        //execute() 方法，其实是同步的。也可以对 command 调用 queue() 方法，它仅仅是将 command 放入线程池的一个等待队列，就立即返回，拿到一个 Future 对象
        User productInfo = getProductInfoCommand.execute();
        System.out.println(productInfo);
    }
}
