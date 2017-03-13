#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.web;/**
 * Created by zhangyin on 2017/3/8.
 */

import org.redisson.RedissonClient;
import org.redisson.core.MessageListener;
import org.redisson.core.RBucket;
import org.redisson.core.RTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.ServletContext;

/**
 * @author zhangyin
 * @create 2017-03-08
 */
@Component
@Configuration
public class WebConfig  extends WebMvcConfigurerAdapter implements ServletContextAware{


    private static String REDIS_VERSION_KEY="${projectName}:version";

    private static String REDIS_VERSION_TOP_KEY="${projectName}:version:top";


    @Value("${symbol_dollar}{StaticResourcesUrl}")
    String staticResourcesUrl;

    @Value("${symbol_dollar}{url_domain}")
    String domain;

    @Autowired
    RedissonClient redissonClient;

    ServletContext servletContext;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext=servletContext;
        servletContext.setAttribute("resourceUrl",staticResourcesUrl);
        servletContext.setAttribute("activityUrl",domain);
        initVersion();
    }

    //初始化版本号
    public  void initVersion(){
        RBucket<Integer> bucket = redissonClient.getBucket(REDIS_VERSION_KEY);
        Integer version = bucket.get();
        if(version==null){
            version=1;
        }
        bucket.set(version);
        servletContext.setAttribute("version",version);
        RTopic<Integer> topic = redissonClient.getTopic(REDIS_VERSION_TOP_KEY);
        topic.addListener(new MessageListener<Integer>() {
            @Override
            public void onMessage(String channel, Integer msg) {
                servletContext.setAttribute("version",msg);
            }
        });
    }
    //修改版本号
    public Integer updateVersion(){
        RBucket<Integer> bucket = redissonClient.getBucket(REDIS_VERSION_KEY);
        Integer oldVersion = bucket.get();
        Integer newVersion=oldVersion+1;
        bucket.set(newVersion);
        RTopic<Integer> topic = redissonClient.getTopic(REDIS_VERSION_TOP_KEY);
        topic.publish(newVersion);
        return newVersion;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //TODO 添加拦截器

        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}
