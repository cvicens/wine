package com.redhat.wine.cellar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import com.uber.jaeger.Configuration;
import com.uber.jaeger.samplers.ProbabilisticSampler;

@SpringBootApplication
public class WineCellarApplication {
	public static final String WINE_SERVICE = "wine-cellar";

	private static final String TRACING_SERVICE_NAME = "TRACING_SERVICE_NAME";
    private static final String TRACING_SERVICE_PORT = "TRACING_SERVICE_PORT";

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	public io.opentracing.Tracer jaegerTracer() {
		String agentHost = System.getenv(TRACING_SERVICE_NAME);
		Integer agentPort = System.getenv(TRACING_SERVICE_PORT) != null ? Integer.decode(System.getenv(TRACING_SERVICE_PORT)) : 6831;
		
		/*return new Configuration(WINE_SERVICE, new Configuration.SamplerConfiguration(ProbabilisticSampler.TYPE, 1),
				new Configuration.ReporterConfiguration())
				.getTracer();*/

		return new Configuration(
			WINE_SERVICE,
			new Configuration.SamplerConfiguration("const", 1),
			new Configuration.ReporterConfiguration(
				true, agentHost, agentPort, 1000, 10000)
		).getTracer();
	}


	//@Bean
	/*public io.opentracing.Tracer zipkinTracer() {
		OkHttpSender okHttpSender = OkHttpSender.builder()
				.encoding(Encoding.JSON)
				.endpoint("http://localhost:9411/api/v1/spans")
				.build();
		AsyncReporter<Span> reporter = AsyncReporter.builder(okHttpSender).build();
		Tracer braveTracer = Tracing.newBuilder()
				.localServiceName("spring-boot")
				.reporter(reporter)
				.traceId128Bit(true)
				.sampler(Sampler.ALWAYS_SAMPLE)
				.build();
		return BraveTracer.create(braveTracer);
	}*/

	public static void main(String[] args) {
		SpringApplication.run(WineCellarApplication.class, args);
	}
}
