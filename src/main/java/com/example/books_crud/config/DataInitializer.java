package com.example.books_crud.config;

import com.example.books_crud.model.Author;
import com.example.books_crud.model.Book;
import com.example.books_crud.service.AuthorService;
import com.example.books_crud.service.BookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Initializes sample data for development and testing purposes
 */
@Component
@Profile("!production")
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);
    
    private final AuthorService creatorService;
    private final BookService literaryService;

    @Autowired
    public DataInitializer(AuthorService creatorService, BookService literaryService) {
        this.creatorService = creatorService;
        this.literaryService = literaryService;
    }

    @Override
    public void run(String... args) {
        logger.info("Initializing sample literary data...");
        
        // Create sample literary creators
        List<Author> sampleCreators = createSampleCreators();
        
        // Create sample literary works
        createSampleLiteraryWorks(sampleCreators);
        
        logger.info("Sample data initialization complete");
    }
    
    private List<Author> createSampleCreators() {
        Author creator1 = new Author();
        creator1.setName("Emily Brontë");
        creator1.setBio("English novelist and poet, best known for her only novel, Wuthering Heights.");
        
        Author creator2 = new Author();
        creator2.setName("Gabriel García Márquez");
        creator2.setBio("Colombian novelist known for magical realism and One Hundred Years of Solitude.");
        
        Author creator3 = new Author();
        creator3.setName("Haruki Murakami");
        creator3.setBio("Japanese writer whose works blend elements of fantasy and realism.");
        
        Author creator4 = new Author();
        creator4.setName("Toni Morrison");
        creator4.setBio("American novelist and Nobel Prize winner known for exploring Black identity.");
        
        Author creator5 = new Author();
        creator5.setName("Jorge Luis Borges");
        creator5.setBio("Argentine short-story writer known for philosophical fiction.");
        
        // Save creators and collect them
        List<Author> creators = Arrays.asList(creator1, creator2, creator3, creator4, creator5);
        creators.forEach(creator -> creatorService.registerCreator(creator));
        
        logger.info("Created {} sample literary creators", creators.size());
        return creators;
    }
    
    private void createSampleLiteraryWorks(List<Author> creators) {
        // Create works for first creator
        Book work1 = new Book();
        work1.setTitle("Wuthering Heights");
        work1.setIsbn("9780141439556");
        work1.setCreator(creators.get(0));
        literaryService.registerLiteraryWork(work1);
        
        // Create works for second creator
        Book work2 = new Book();
        work2.setTitle("One Hundred Years of Solitude");
        work2.setIsbn("9780060883287");
        work2.setCreator(creators.get(1));
        literaryService.registerLiteraryWork(work2);
        
        Book work3 = new Book();
        work3.setTitle("Love in the Time of Cholera");
        work3.setIsbn("9780307389732");
        work3.setCreator(creators.get(1));
        literaryService.registerLiteraryWork(work3);
        
        // Create works for third creator
        Book work4 = new Book();
        work4.setTitle("Norwegian Wood");
        work4.setIsbn("9780375704024");
        work4.setCreator(creators.get(2));
        literaryService.registerLiteraryWork(work4);
        
        Book work5 = new Book();
        work5.setTitle("Kafka on the Shore");
        work5.setIsbn("9781400079278");
        work5.setCreator(creators.get(2));
        literaryService.registerLiteraryWork(work5);
        
        // Create works for fourth creator
        Book work6 = new Book();
        work6.setTitle("Beloved");
        work6.setIsbn("9781400033416");
        work6.setCreator(creators.get(3));
        literaryService.registerLiteraryWork(work6);
        
        // Create works for fifth creator
        Book work7 = new Book();
        work7.setTitle("Ficciones");
        work7.setIsbn("9780802130303");
        work7.setCreator(creators.get(4));
        literaryService.registerLiteraryWork(work7);
        
        logger.info("Created 7 sample literary works");
    }
}