package com.example.Bank.endpoint;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {
	
	@Bean
	public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(servlet, "/ws/*");
	}

	@Bean(name = "bankstatement")
	public DefaultWsdl11Definition bankStatementSchemaDefinition(XsdSchema bankStatementSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("BankStatementPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://example.com/Bank/service");
		wsdl11Definition.setSchema(bankStatementSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema bankStatementSchema() {
		return new SimpleXsdSchema(new ClassPathResource("bankStatement.xsd"));
	}
	
	@Bean(name = "paymentorder")
	public DefaultWsdl11Definition paymentOrderSchemaDefinition(XsdSchema paymentOrderSchema) {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName("PaymentOrderPort");
		wsdl11Definition.setLocationUri("/ws");
		wsdl11Definition.setTargetNamespace("http://example.com/Bank/service");
		wsdl11Definition.setSchema(paymentOrderSchema);
		return wsdl11Definition;
	}

	@Bean
	public XsdSchema paymentOrderSchema() {
		return new SimpleXsdSchema(new ClassPathResource("paymentOrder.xsd"));
	}
}
