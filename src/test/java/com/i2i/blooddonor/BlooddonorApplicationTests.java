package com.i2i.blooddonor;

import com.i2i.blooddonor.model.Member;
import com.i2i.blooddonor.repository.DonorRepository;
import com.i2i.blooddonor.requestModel.MemberDTO;
import com.i2i.blooddonor.service.DonorService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.sql.Date;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@EnableCaching
class BlooddonorApplicationTests {

	@Container
	@ServiceConnection
	static GenericContainer redis = new GenericContainer(DockerImageName.parse("redis:7.4.2"))
			.withExposedPorts(6379);

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private DonorRepository donorRepository;
	@Autowired
	private CacheManager cacheManager;
	@MockitoSpyBean
	private DonorRepository donorRepositorySpy;

	private final ObjectMapper objectMapper = new ObjectMapper();

//	@BeforeEach
//	void setUp() {
//		donorRepository.deleteAll(); // Ensure a clean database for each test
//	}

	@Test
	void testCreateProductAndCacheIt() throws Exception {
		System.out.println("inside test 1--");
		MemberDTO memberDTO = new MemberDTO();
		memberDTO.setName("Fredrick");
		memberDTO.setBloodGroup("A2B+ve");
		memberDTO.setAddress("Chennai");
		memberDTO.setContact("787629892");
//		LocalDate localDate =LocalDate.now();
//		memberDTO.setLastDonatedOn(Date.valueOf(localDate));

		// Step 1: Create a Product
		MvcResult result = mockMvc.perform(post("/blooddonors/newMember")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(memberDTO)))
				.andExpect(status().isCreated())
				.andReturn();


		Member createdProduct = objectMapper.readValue(result.getResponse().getContentAsString(), Member.class);
		int productId = createdProduct.getId();


		// Step 2: Check Product Exists in DB
		Assertions.assertTrue(donorRepository.findById(productId).isPresent());

		// Step 3: Check Cache
		Cache cache = cacheManager.getCache("MEMBER_CACHE");
		assertNotNull(cache);
		assertNotNull(cache.get(productId, Member.class));
	}

	@Test
	void testGetProductAndVerifyCache() throws Exception {
		// Step 1: Save product in DB
		Member member = new Member();
		member.setName("Alex");
		donorRepository.save(member);

		// Step 2: Fetch product
		mockMvc.perform(MockMvcRequestBuilders.get("/blooddonors/findMemberById/" + member.getId()))
				.andExpect(status().isOk());
//				.andExpect(jsonPath("$.name").value("name"));

		Mockito.verify(donorRepositorySpy, Mockito.times(1)).findById(member.getId());

		Mockito.clearInvocations(donorRepositorySpy);

		mockMvc.perform(MockMvcRequestBuilders.get("/blooddonors/findMemberById/" + member.getId()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("name"));

		Mockito.verify(donorRepositorySpy, Mockito.times(0)).findById(member.getId());
	}

	@DynamicPropertySource
	static void redisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.redis.host", () -> "localhost");
		registry.add("spring.redis.port", () -> "6379");
	}

}
