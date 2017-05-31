package cn.fishermartyn.inf.misc;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * User : yujixing Date : 2017/5/31 Time : 下午4:25
 */

@ConfigurationProperties(prefix = "dbpool")
public class DBProperties {

	private String url;
	private String username;

	private String password;

	private boolean enableSqlTrack = false;

	private boolean autoChangeDataSource = true;

	private List<String> urlList;
	private List<String> usernameList;
	private List<String> passwordList;

	private int maxActive = 20;
	private int minIdle = 1;
	private int initialSize = 1;

	@PostConstruct
	public void init() throws Exception {

		// 如果有逗号分割，表示有主从
		if (url.contains(",")) {
			urlList = Arrays.asList(StringUtils.split(url, ","));
			usernameList = Arrays.asList(StringUtils.split(username, ","));
			passwordList = Arrays.asList(StringUtils.split(password, ","));

		} else {
			urlList = Arrays.asList(url);
			usernameList = Arrays.asList(username);
			passwordList = Arrays.asList(password);
		}
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnableSqlTrack() {
		return enableSqlTrack;
	}

	public void setEnableSqlTrack(boolean enableSqlTrack) {
		this.enableSqlTrack = enableSqlTrack;
	}

	public boolean isAutoChangeDataSource() {
		return autoChangeDataSource;
	}

	public void setAutoChangeDataSource(boolean autoChangeDataSource) {
		this.autoChangeDataSource = autoChangeDataSource;
	}

	public List<String> getUrlList() {
		return urlList;
	}

	public void setUrlList(List<String> urlList) {
		this.urlList = urlList;
	}

	public List<String> getUsernameList() {
		return usernameList;
	}

	public void setUsernameList(List<String> usernameList) {
		this.usernameList = usernameList;
	}

	public List<String> getPasswordList() {
		return passwordList;
	}

	public void setPasswordList(List<String> passwordList) {
		this.passwordList = passwordList;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer("DBProperties{");
		sb.append("url='").append(url).append('\'');
		sb.append(", username='").append(username).append('\'');
		sb.append(", password='").append(password).append('\'');
		sb.append(", enableSqlTrack=").append(enableSqlTrack);
		sb.append(", autoChangeDataSource=").append(autoChangeDataSource);
		sb.append(", urlList=").append(urlList);
		sb.append(", usernameList=").append(usernameList);
		sb.append(", passwordList=").append(passwordList);
		sb.append(", maxActive=").append(maxActive);
		sb.append(", minIdle=").append(minIdle);
		sb.append(", initialSize=").append(initialSize);
		sb.append('}');
		return sb.toString();
	}
}
