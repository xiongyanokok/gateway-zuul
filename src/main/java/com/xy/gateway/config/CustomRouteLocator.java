package com.xy.gateway.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;

/**
 * Created by xujingfeng on 2017/4/1.
 */
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

	public CustomRouteLocator(String servletPath, ZuulProperties properties) {
		super(servletPath, properties);
		refresh();
	}

	@Override
	public void refresh() {
		doRefresh();
	}

	@Override
	protected Map<String, ZuulRoute> locateRoutes() {
		Map<String, ZuulRoute> routesMap = new LinkedHashMap<>();
		// 动态加载路由信息
		routesMap.putAll(dynamicLocateRoutes());
		return routesMap;
	}

	private Map<String, ZuulRoute> dynamicLocateRoutes() {
		Map<String, ZuulRoute> routes = new LinkedHashMap<>();
		ZuulRoute baidu = new ZuulRoute();
		baidu.setId("baidu");
		baidu.setPath("/baidu/**");
		baidu.setStripPrefix(true);
		baidu.setUrl("https://www.baidu.com");
		routes.put("/baidu/**", baidu);

		ZuulRoute cdsq = new ZuulRoute();
		cdsq.setId("cdsq");
		cdsq.setPath("/cdsq/**");
		cdsq.setStripPrefix(true);
		cdsq.setUrl("http://test.apicaidao.hexun.com");
		routes.put("/cdsq/**", cdsq);

		ZuulRoute lesson = new ZuulRoute();
		lesson.setId("lesson");
		lesson.setPath("/lesson/**");
		lesson.setStripPrefix(true);
		lesson.setUrl("http://test.apilesson.hexun.com");
		routes.put("/lesson/**", lesson);
		return routes;
	}

}
