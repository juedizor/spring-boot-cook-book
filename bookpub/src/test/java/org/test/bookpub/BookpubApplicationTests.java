package org.test.bookpub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.test.bookpub.entity.Book;
import org.test.bookpub.repository.BookRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = BookpubApplication.class)
public class BookpubApplicationTests {

	@Autowired
	private WebApplicationContext context;
	@Autowired
	private BookRepository bookRepository;
	@LocalServerPort
	private int port;

	private MockMvc mockMvc;
	private RestTemplate restTemplate = new RestTemplate();

	@Before
	public void setupMockMvc() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void contextLoads() {
		assertEquals(1, bookRepository.count());
	}

	@Test
	public void getAllBooktest() {
		ResponseEntity<Iterable<Book>> iter = restTemplate.exchange("http://localhost:" + port + "/books/",
				HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<Iterable<Book>>() {
				});
		List<Book> list = Lists.newArrayList(iter.getBody());
		assertTrue(list != null && !list.isEmpty());
	}

	@Test
	public void webappBookIsbnApi() {
		Book book = restTemplate.getForObject("http://localhost:" + port + "/books/978-1-78528-415-1", Book.class);
		assertNotNull(book);
		assertEquals("Packt", book.getPublisher().getName());
	}

	@Test
	public void webappPublisherApi() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/books/978-1-78528-415-1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Packt")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Spring Boot Recipe"));

	}

}
