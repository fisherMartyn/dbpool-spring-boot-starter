package cn.fishermartyn.inf.autoconfigure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import cn.fishermartyn.inf.misc.DbTypeHolder;

/**
 * @author yujixing
 * </p>
 * 生成RoutingDataSource实例
 */
@Configuration
@AutoConfigureAfter(DruidAutoConfiguration.class)
public class DataSourceAutoConfiguration {

	@Resource(name = "dbpool_datasources")
	List<DataSource> sources;

	@Bean(name = "routing_datasource")
	public DMSRoutingDataSource dataSource() {

		DMSRoutingDataSource resolver = new DMSRoutingDataSource();

		Map<Object, Object> dataSources = new HashMap<>();

		dataSources.put(DbTypeHolder.MASTER, sources.get(0));

		if (2 == sources.size())  {
			dataSources.put(DbTypeHolder.SLAVE, sources.get(1));
		} else {
			dataSources.put(DbTypeHolder.SLAVE, sources.get(0));
		}

		resolver.setTargetDataSources(dataSources);
		resolver.setDefaultTargetDataSource(sources.get(0));

		return resolver;
	}

	public class DMSRoutingDataSource extends AbstractRoutingDataSource {

		@Override
		protected Object determineCurrentLookupKey() {
			return DbTypeHolder.getDbType();
		}
	}
}
