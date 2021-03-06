package cn.lrl.resolver;

import cn.lrl.annotation.UserParamResolver;
import cn.lrl.utils.CookieUtil;
import cn.lrl.utils.RedisUtil;
import cn.lrl.vo.RespMsgVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

//@Component
public class UserArgResolver implements HandlerMethodArgumentResolver {

    @Autowired
    RedisUtil redisUtil;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // 当请求路径中存在UserParamResolver注解时触发
        return parameter.hasParameterAnnotation(UserParamResolver.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String userLoginCookie = CookieUtil.getCookieValue(request, "userLoginCookie");

        // 用户是二次登录
        if (!StringUtils.isEmpty(userLoginCookie) && redisUtil.get(userLoginCookie) != null) {
            return RespMsgVo.success();
        }
        return null;
    }
}
