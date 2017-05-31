package cn.fishermartyn.inf.autoconfigure;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

import cn.fishermartyn.inf.misc.DBProperties;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.CollectionUtils;

import cn.fishermartyn.inf.interceptor.DataSourceInterceptor;

/**
 * @author yujixing
 *
 * 生成SqlSessionFactory，配置SqlSessionFactoryBean的Plugins
 */
@Configuration
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
@EnableConfigurationProperties(DBProperties.class)
@EnableAspectJAutoProxy
public class MybatisAutoConfiguration {

	@Resource(name = "routing_datasource")
	DataSource dataSource;

	@Autowired
	private DBProperties dbProperties;

	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {

		SqlSessionFactoryBean factory = new SqlSessionFactoryBean();
		factory.setDataSource(dataSource);

		List<Interceptor> interceptors = new ArrayList<>();

		if (dbProperties.isAutoChangeDataSource()) {
			interceptors.add(new DataSourceInterceptor());
		}

		if (!CollectionUtils.isEmpty(interceptors)) {
			factory.setPlugins(interceptors.toArray(new Interceptor[] {}));
		}

		return factory.getObject();
	}

}
