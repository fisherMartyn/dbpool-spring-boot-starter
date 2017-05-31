package cn.fishermartyn.inf.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.fishermartyn.inf.autoconfigure.DataSourceAutoConfiguration;
import cn.fishermartyn.inf.autoconfigure.DruidAutoConfiguration;
import cn.fishermartyn.inf.autoconfigure.MybatisAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * User : yujixing
 * Date : 2017/5/31
 * Time : 下午4:06
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import({ DruidAutoConfiguration.class, DataSourceAutoConfiguration.class, MybatisAutoConfiguration.class })
public @interface EnableDbPool {

}