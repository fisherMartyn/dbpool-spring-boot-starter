package cn.fishermartyn.inf.interceptor;

import java.util.Locale;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import cn.fishermartyn.inf.misc.DbTypeHolder;

/**
 * @author yujixing
 *
 * </p>
 *
 * 利用mybatis plugin，根据不同的statement切换数据源
 *
 *
 */
@Intercepts({ @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }),
		@Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class,
				RowBounds.class, ResultHandler.class }) })
public class DataSourceInterceptor implements Interceptor {

	private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";

	@Override
	public Object intercept(Invocation invocation) throws Throwable {

		boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();

		if (synchronizationActive) {
			DbTypeHolder.setMaster();
		} else {
			Object[] objects = invocation.getArgs();
			MappedStatement ms = (MappedStatement) objects[0];

			if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
				// 只对没手动设置主库的进行处理
				if (DbTypeHolder.getDbType() != DbTypeHolder.MASTER) {
					// 自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
					if (ms.getId().contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
						DbTypeHolder.setMaster();
					} else {
						BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
						String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
						// 对于select for update, select (insert)这类语句使用主库
						if (sql.matches(REGEX)) {
							DbTypeHolder.setMaster();
						} else {
							DbTypeHolder.setSlave();
						}
					}
				}
			} else {
				DbTypeHolder.setMaster();
			}
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {
	}

}
