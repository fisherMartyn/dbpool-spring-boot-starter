package cn.fishermartyn.inf.autoconfigure;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.sql.DataSource;

import cn.fishermartyn.inf.misc.DBProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 配置Druid数据源
 * </p>
 * 注意：
 * </p>
 * 如果有一主多从，只会随机从从库中选择一个进行连接池建立（在多个客户端的情况下，读写会均衡）。
 * </p>
 * 
 * 
 * @author yujixing
 * 
 *
 */
@Configuration
@EnableConfigurationProperties(DBProperties.class)
@ConditionalOnClass(DruidDataSource.class)
@ConditionalOnProperty(prefix = "focus-db", name = "url")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)

public class DruidAutoConfiguration {

	@Autowired
    private DBProperties dbProperties;

	@Bean(name = "dbpool_datasources")
	List<DataSource> dataSourceList() {
		int size = dbProperties.getUrlList().size();
		List<DataSource> sources = new ArrayList<>();

		int randomSlave = 0;
		if (size > 1) {
			Random random = new Random(1000);
			randomSlave = random.nextInt(size - 1) + 1;
		}

		/* 只会实例化主库和随机的一个从库 */
		for (int i = 0; i < size; ++i) {
			if (!(i == 0 || i == randomSlave)) {
				continue;
			}
			DruidDataSource dataSource = new DruidDataSource();
			dataSource.setUrl(dbProperties.getUrlList().get(i));
			dataSource.setUsername(dbProperties.getUsernameList().get(i));
			dataSource.setPassword(dbProperties.getPasswordList().get(i));
			dataSource.setInitialSize(dbProperties.getInitialSize());
			dataSource.setMinIdle(dbProperties.getMinIdle());
			dataSource.setMaxActive(dbProperties.getMaxActive());
			try {
				dataSource.init();
			} catch (Exception e) {
				dataSource.close();
				throw new RuntimeException(e);
			}
			sources.add(dataSource);
		}
		if (CollectionUtils.isEmpty(sources)) {
			throw new RuntimeException("datasource cannot init!");
		}
		return sources;
	}
}
