package com.xy.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.xy.gateway.util.GatewayUtils;

/**
 * 过滤器
 * 
 * @author xiongyan
 * @date 2017年12月18日 上午11:01:57
 */
@Component
public class AccessFilter extends ZuulFilter {
	
	/**
	 * 是否执行该过滤器
	 */
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/**
	 * 执行
	 */
	@Override
	public Object run() {
		Route route = GatewayUtils.getRoute();
		String id = route.getId();
		if (!"baidu".equals(id)) {
			GatewayUtils.responseBody("{\"data\":\"资源 不存在\"}");
			return null;
		}
		
		HttpServletRequest request = RequestContext.getCurrentContext().getRequest(); 
		StringBuffer url = request.getRequestURL();
		String query = request.getQueryString();
		if (StringUtils.isNotEmpty(query)) {
			url.append("?");
			url.append(query);
		}
		System.out.println(url);
		return null;
	}

	/**
	 * 过滤器的类型
	 * pre：可以在请求被路由之前调用
	 * route：在路由请求时候被调用
	 * post：在route和error过滤器之后被调用
	 * error：处理请求时发生错误时被调用
	 */
	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	/**
	 * 过滤器执行顺序
	 * 通过数字指定，数字越大，优先级越低
	 */
	@Override
	public int filterOrder() {
		return 1;
	}

}
