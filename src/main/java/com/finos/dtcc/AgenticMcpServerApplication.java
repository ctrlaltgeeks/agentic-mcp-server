package com.finos.dtcc;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.finos.dtcc.tool.ClientTool;

@SpringBootApplication
@EnableJpaAuditing
public class AgenticMcpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgenticMcpServerApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider fintechTools(ClientTool clientOnboarding) {
		return MethodToolCallbackProvider.builder().toolObjects(clientOnboarding).build();
	}

}
