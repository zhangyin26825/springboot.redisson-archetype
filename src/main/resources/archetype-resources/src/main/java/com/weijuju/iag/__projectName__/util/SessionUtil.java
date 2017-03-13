#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${basePackage}.util;/**
 * Created by zhangyin on 2017/3/13.
 */

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhangyin
 * @create 2017-03-13
 */
public class SessionUtil {

    private static HttpServletRequest getRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request;
    }

    public static void setAttribute(String name,Object value){
        getRequest().getSession().setAttribute(name,value);
    }

    public static <T> T getAttribute(String name,Class<T> c){
       return (T)getRequest().getSession().getAttribute(name);
    }
}
