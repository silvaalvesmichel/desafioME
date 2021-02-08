package com.me.desafio;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(classes = DesafioMeApplication.class)
class DesafioMeApplicationTests {
	
	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@Value("${spring.application.name}")
	private String appName;

	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}

	@Test
	public void healthTest() throws Exception {
		this.mockMvc.perform(get("/health")).andExpect(status().isOk())
				.andExpect(content().string(containsString("UP")));
	}

	@Test
	public void infoTest() throws Exception {
		this.mockMvc.perform(get("/info")).andExpect(status().isOk())
				.andExpect(content().string(containsString("build")))
				.andExpect(content().string(containsString("id")))
				.andExpect(content().string(containsString(appName)));
	}

}
