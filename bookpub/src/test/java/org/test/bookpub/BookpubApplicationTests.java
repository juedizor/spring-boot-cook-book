package org.test.bookpub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
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
	private BookRepository bookReository;
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
		assertEquals(1, bookReository.count());
	}

	@Test
	public void webappBookIsbnApi() {
		Book book = restTemplate.getForObject("http://localhost:" + port + "/books/978-1-78528-415-1", Book.class);
		assertNotNull(book);
		assertEquals("Packt", book.getPublisher().getName());
	}

	@Test
	public void webappPublisherApi() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/publisher/1")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(
						MockMvcResultMatchers.content().contentType(MediaType.parseMediaType("application/hal+json")))
				.andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Packt")))
				.andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Packt"));

	}

}
