#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.controller;/**
 * Created by zhangyin on 2017/3/7.
 */
import ${basePackage}.web.WebConfig;
import org.redisson.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author zhangyin
 * @create 2017-03-07
 */
@Controller
@RequestMapping
public class VersionController {

    @Autowired
    RedissonClient redissonClient;
    @Autowired
    WebConfig webConfig;
    //修改版本号接口
    @RequestMapping("/version")
    @ResponseBody
    public ModelMap refreshVersion(){
        ModelMap map=new ModelMap();
        Integer version = webConfig.updateVersion();
        map.put("version",version);
        return map;
    }

}
