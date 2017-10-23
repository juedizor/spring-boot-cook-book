package org.test.bookpub;

import java.io.File;
import java.util.concurrent.TimeUnit;

import org.apache.catalina.connector.Connector;
import org.apache.catalina.filters.RemoteIpFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.test.bookpub.formatters.BookFormatter;
import org.test.bookpub.repository.BookRepository;

@Configuration
@PropertySource("classpath:/tomcat.https.properties")
@EnableConfigurationProperties(WebConfiguration.TomcatSslConnectorProperties.class)
public class WebConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private BookRepository bookRepository;

	@Bean
	public RemoteIpFilter remoteIpFilter() {
		return new RemoteIpFilter();
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		return new LocaleChangeInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(localeChangeInterceptor());
	}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		// TODO Auto-generated method stub
		registry.addFormatter(new BookFormatter(bookRepository));
	}

	@Override
	public void configurePathMatch(PathMatchConfigurer configurer) {
		// TODO Auto-generated method stub
		configurer.setUseSuffixPatternMatch(false).setUseTrailingSlashMatch(true);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/internal/**").addResourceLocations("classpath:/");
	}

	@Bean
	public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {
		return new EmbeddedServletContainerCustomizer() {

			@Override
			public void customize(ConfigurableEmbeddedServletContainer container) {
				// TODO Auto-generated method stub
				container.setSessionTimeout(1, TimeUnit.MINUTES);
			}
		};
	}

	@ConfigurationProperties(prefix = "custom.tomcat.https")
	public static class TomcatSslConnectorProperties {
		private Integer port;
		private Boolean ssl = true;
		private Boolean secure = true;
		private String scheme = "https";
		private File keystore;
		private String keystorePassword;

		/**
		 * @return the port
		 */
		public Integer getPort() {
			return port;
		}

		/**
		 * @param port
		 *            the port to set
		 */
		public void setPort(Integer port) {
			this.port = port;
		}

		/**
		 * @return the ssl
		 */
		public Boolean getSsl() {
			return ssl;
		}

		/**
		 * @param ssl
		 *            the ssl to set
		 */
		public void setSsl(Boolean ssl) {
			this.ssl = ssl;
		}

		/**
		 * @return the secure
		 */
		public Boolean getSecure() {
			return secure;
		}

		/**
		 * @param secure
		 *            the secure to set
		 */
		public void setSecure(Boolean secure) {
			this.secure = secure;
		}

		/**
		 * @return the scheme
		 */
		public String getScheme() {
			return scheme;
		}

		/**
		 * @param scheme
		 *            the scheme to set
		 */
		public void setScheme(String scheme) {
			this.scheme = scheme;
		}

		/**
		 * @return the keytore
		 */
		public File getKeystore() {
			return keystore;
		}

		/**
		 * @param keytore
		 *            the keytore to set
		 */
		public void setKeystore(File keystore) {
			this.keystore = keystore;
		}

		/**
		 * @return the keystorePassword
		 */
		public String getKeystorePassword() {
			return keystorePassword;
		}

		/**
		 * @param keystorePassword
		 *            the keystorePassword to set
		 */
		public void setKeystorePassword(String keystorePassword) {
			this.keystorePassword = keystorePassword;
		}

		public void configureConnector(Connector connector) {
			if (port != null)
				connector.setPort(port);
			if (secure != null)
				connector.setSecure(secure);
			if (scheme != null)
				connector.setScheme(scheme);
			if (ssl != null)
				connector.setProperty("SSLEnabled", ssl.toString());
			if (keystore != null && keystore.exists())
				connector.setProperty("keystoreFile", keystore.getAbsolutePath());
			connector.setProperty("keystorePassword", keystorePassword);
		}
	}

	public EmbeddedServletContainerFactory servletContainer(TomcatSslConnectorProperties properties) {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
		tomcat.addAdditionalTomcatConnectors(createSslConnector(properties));
		return tomcat;
	}

	private Connector createSslConnector(TomcatSslConnectorProperties properties) {
		Connector connector = new Connector();
		properties.configureConnector(connector);
		return connector;
	}

}
