package com.xy.gateway.util;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.http.MediaType;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.zuul.context.RequestContext;

/**
 * GatewayUtils
 * 
 * @author xiongyan
 * @date 2017年12月18日 下午3:30:16
 */
public class GatewayUtils {
	
	private GatewayUtils() {
		
	}
	
	/**
	 * UrlPathHelper
	 */
	private static final UrlPathHelper URLPATHHELPER = new UrlPathHelper();
	
	/**
	 * RouteLocator
	 */
	private static RouteLocator routeLocator = SpringContextUtils.getBean(RouteLocator.class);

	/**
	 * 是否终止
	 * 
	 * @return
	 */
	public static boolean isEnd() {
		return !RequestContext.getCurrentContext().sendZuulResponse();
	}
	
	/**
	 * 获取Route
	 * 
	 * @return
	 */
	public static Route getRoute() {
		return routeLocator.getMatchingRoute(URLPATHHELPER.getPathWithinApplication(RequestContext.getCurrentContext().getRequest()));
	}
	
	/**
	 * 网关自己响应
	 * 
	 * @param result
	 */
	public static void responseBody(String result) {
		RequestContext ctx = RequestContext.getCurrentContext();
		ctx.addZuulResponseHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
		ctx.setSendZuulResponse(false);
		ctx.setResponseStatusCode(HttpServletResponse.SC_OK);
		String callback = ctx.getRequest().getParameter("callback");
		if (StringUtils.isNotEmpty(callback)) {
			int index = result.indexOf("({");
			if (index == -1) {
				ctx.setResponseBody(callback + "(" + result + ")");
			} else {
				ctx.setResponseBody(callback + result.substring(index));
			}
		} else {
			ctx.setResponseBody(result);
		}
	}

}
