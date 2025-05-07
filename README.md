
# Spring Boot CRUD Application - Books and Authors

## Entity Relationship Design

### Entity Model

The application is built around two main entities with a one-to-many relationship:

1. **Author Entity**:
   - `id`: Long (Primary Key)
   - `name`: String
   - `bio`: String
   - One-to-Many relationship with Books

2. **Book Entity**:
   - `id`: Long (Primary Key)
   - `title`: String
   - `isbn`: String
   - Many-to-One relationship with Author

### Entity Relationship Diagram

```
┌─────────────┐       ┌─────────────┐
│   Author    │       │    Book     │
├─────────────┤       ├─────────────┤
│ id          │       │ id          │
│ name        │       │ title       │
│ bio         │       │ isbn        │
│             │       │ author_id   │
└─────────────┘       └─────────────┘
      │ 1                 ▲ *
      └─────────────────────┘
```

### Implementation in Code

The entities were implemented using JPA annotations:

**Author.java**
```java
@Entity
@Getter
@Setter
@ToString(exclude = "books")
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(length = 1000)
    private String bio;
    
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Book> books = new ArrayList<>();
    
    // Convenience methods for relationship management
    public void addBook(Book book) {
        books.add(book);
        book.setAuthor(this);
    }
    
    public void removeBook(Book book) {
        books.remove(book);
        book.setAuthor(null);
    }
}
```

**Book.java**
```java
@Entity
@Getter
@Setter
@ToString(exclude = "author")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    private String isbn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private Author author;
    
    // Constructor without id for easier creation
    public Book(String title, String isbn, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.author = author;
    }
}
```

## Implementation Details for Each Operation

### 1. Database Population

The application uses a `CommandLineRunner` implementation to populate the database with sample data at startup:

**DataInitializer.java**
```java
@Component
public class DataInitializer implements CommandLineRunner {
    private final AuthorService authorService;
    private final BookService bookService;
    
    @Autowired
    public DataInitializer(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample authors and books
        Author author1 = new Author();
        author1.setName("J.K. Rowling");
        author1.setBio("British author best known for the Harry Potter series.");
        authorService.createAuthor(author1);
        
        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setIsbn("9780747532743");
        book1.setAuthor(author1);
        bookService.createBook(book1);
        
        // Additional authors and books...
    }
}
```

### 2. Create Operations

#### Adding a New Author

**Controller Code (AuthorController.java)**
```java
@GetMapping("/add")
public String showAddForm(Model model) {
    model.addAttribute("author", new Author());
    return "addAuthor";
}

@PostMapping("/add")
public String addAuthor(@ModelAttribute Author author, RedirectAttributes redirectAttributes) {
    try {
        authorService.createAuthor(author);
        redirectAttributes.addFlashAttribute("message", "Author added successfully!");
        return "redirect:/authors";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error adding author: " + e.getMessage());
        return "redirect:/authors/add";
    }
}
```

**JSP Form (addAuthor.jsp)**
```html
<form:form action="/authors/add" method="post" modelAttribute="author">
    <div class="form-group">
        <form:label path="name">Name</form:label>
        <form:input path="name" required="true" />
    </div>
    <div class="form-group">
        <form:label path="bio">Bio</form:label>
        <form:textarea path="bio" rows="5" />
    </div>
    <button type="submit" class="btn">Save Author</button>
    <a href="<c:url value='/authors' />" class="btn">Cancel</a>
</form:form>
```

#### Adding a New Book

**Controller Code (BookController.java)**
```java
@GetMapping("/add")
public String showAddForm(Model model) {
    model.addAttribute("book", new Book());
    model.addAttribute("authors", authorService.getAllAuthors());
    return "addBook";
}

@PostMapping("/add")
public String addBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
    try {
        bookService.createBook(book);
        redirectAttributes.addFlashAttribute("message", "Book added successfully!");
        return "redirect:/books";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error adding book: " + e.getMessage());
        return "redirect:/books/add";
    }
}
```

**JSP Form (addBook.jsp)**
```html
<form:form action="/books/add" method="post" modelAttribute="book">
    <div class="form-group">
        <form:label path="title">Title</form:label>
        <form:input path="title" required="true" />
    </div>
    <div class="form-group">
        <form:label path="isbn">ISBN</form:label>
        <form:input path="isbn" required="true" />
    </div>
    <div class="form-group">
        <form:label path="author">Author</form:label>
        <form:select path="author" required="true">
            <form:option value="" label="-- Select Author --" />
            <form:options items="${authors}" itemValue="id" itemLabel="name" />
        </form:select>
    </div>
    <button type="submit" class="btn">Save Book</button>
    <a href="<c:url value='/books' />" class="btn">Cancel</a>
</form:form>
```

### 3. Read Operations

#### Listing All Authors

**Controller Code (AuthorController.java)**
```java
@GetMapping
public String listAuthors(Model model) {
    model.addAttribute("authors", authorService.getAllAuthors());
    return "listAuthors";
}
```

**JSP View (listAuthors.jsp)**
```html
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Bio</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="author" items="${authors}">
            <tr>
                <td>${author.id}</td>
                <td>${author.name}</td>
                <td>${author.bio}</td>
                <td>
                    <a href="<c:url value='/authors/update/${author.id}' />" class="btn">Edit</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
```

#### Listing All Books

**Controller Code (BookController.java)**
```java
@GetMapping
public String listBooks(Model model) {
    model.addAttribute("books", bookService.getAllBooks());
    return "listBooks";
}
```

**JSP View (listBooks.jsp)**
```html
<table>
    <thead>
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>ISBN</th>
            <th>Author</th>
            <th>Actions</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="book" items="${books}">
            <tr>
                <td>${book.id}</td>
                <td>${book.title}</td>
                <td>${book.isbn}</td>
                <td>${book.author.name}</td>
                <td>
                    <a href="<c:url value='/books/update/${book.id}' />" class="btn">Edit</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
```

#### Custom Query for Books with Authors

**Repository Code (BookRepository.java)**
```java
@Query("SELECT b.title, b.isbn, a.name FROM Book b JOIN b.author a")
List<Object[]> fetchBookWithAuthor();
```

**Controller Code (BookController.java)**
```java
@GetMapping("/with-authors")
public String listBooksWithAuthors(Model model) {
    List<Object[]> booksWithAuthors = bookService.getBooksWithAuthor();
    model.addAttribute("booksWithAuthors", booksWithAuthors);
    return "listBooksWithAuthors";
}
```

**JSP View (listBooksWithAuthors.jsp)**
```html
<table>
    <thead>
        <tr>
            <th>Title</th>
            <th>ISBN</th>
            <th>Author</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="bookWithAuthor" items="${booksWithAuthors}">
            <tr>
                <td>${bookWithAuthor[0]}</td> <!-- Title -->
                <td>${bookWithAuthor[1]}</td> <!-- ISBN -->
                <td>${bookWithAuthor[2]}</td> <!-- Author Name -->
            </tr>
        </c:forEach>
    </tbody>
</table>
```

### 4. Update Operations

#### Updating an Author

**Controller Code (AuthorController.java)**
```java
@GetMapping("/update/{id}")
public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Author> author = authorService.getAuthorById(id);
    if (author.isPresent()) {
        model.addAttribute("author", author.get());
        return "updateAuthor";
    } else {
        redirectAttributes.addFlashAttribute("error", "Author not found!");
        return "redirect:/authors";
    }
}

@PostMapping("/update")
public String updateAuthor(@ModelAttribute Author author, RedirectAttributes redirectAttributes) {
    try {
        authorService.updateAuthor(author);
        redirectAttributes.addFlashAttribute("message", "Author updated successfully!");
        return "redirect:/authors";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error updating author: " + e.getMessage());
        return "redirect:/authors/update/" + author.getId();
    }
}
```

**JSP Form (updateAuthor.jsp)**
```html
<form:form action="/authors/update" method="post" modelAttribute="author">
    <form:hidden path="id" />
    <div class="form-group">
        <form:label path="name">Name</form:label>
        <form:input path="name" required="true" />
    </div>
    <div class="form-group">
        <form:label path="bio">Bio</form:label>
        <form:textarea path="bio" rows="5" />
    </div>
    <button type="submit" class="btn">Update Author</button>
    <a href="<c:url value='/authors' />" class="btn">Cancel</a>
</form:form>
```

#### Updating a Book

**Controller Code (BookController.java)**
```java
@GetMapping("/update/{id}")
public String showUpdateForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
    Optional<Book> book = bookService.getBookById(id);
    if (book.isPresent()) {
        model.addAttribute("book", book.get());
        model.addAttribute("authors", authorService.getAllAuthors());
        return "updateBook";
    } else {
        redirectAttributes.addFlashAttribute("error", "Book not found!");
        return "redirect:/books";
    }
}

@PostMapping("/update")
public String updateBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
    try {
        bookService.updateBook(book);
        redirectAttributes.addFlashAttribute("message", "Book updated successfully!");
        return "redirect:/books";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error updating book: " + e.getMessage());
        return "redirect:/books/update/" + book.getId();
    }
}
```

**JSP Form (updateBook.jsp)**
```html
<form:form action="/books/update" method="post" modelAttribute="book">
    <form:hidden path="id" />
    <div class="form-group">
        <form:label path="title">Title</form:label>
        <form:input path="title" required="true" />
    </div>
    <div class="form-group">
        <form:label path="isbn">ISBN</form:label>
        <form:input path="isbn" required="true" />
    </div>
    <div class="form-group">
        <form:label path="author">Author</form:label>
        <form:select path="author" required="true">
            <form:option value="" label="-- Select Author --" />
            <form:options items="${authors}" itemValue="id" itemLabel="name" />
        </form:select>
    </div>
    <button type="submit" class="btn">Update Book</button>
    <a href="<c:url value='/books' />" class="btn">Cancel</a>
</form:form>
```

## Challenges Faced and Solutions

### 1. Circular Reference in Entity Relationships

**Challenge:**
When trying to update a book, the application encountered a `StackOverflowError` due to circular references between the `Book` and `Author` entities. This occurred because both entities were using Lombok's `@Data` annotation, which generates `toString()` methods that include all fields, creating an infinite recursion when one entity tries to print the other.

**Error Message:**
```
Failed to convert from type [@jakarta.persistence.ManyToOne @jakarta.persistence.JoinColumn com.example.books_crud.model.Author] to type [java.lang.String] for value [com.example.books_crud.model.Author$HibernateProxy@5267eaf2]
```

**Root Cause:**
The error was caused by a circular reference in the `toString()` methods:
1. `Book.toString()` calls `author.toString()`
2. `Author.toString()` calls `books.toString()`
3. For each book in books, `book.toString()` calls `author.toString()`
4. And the cycle continues until a `StackOverflowError` occurs

**Solution:**
Replace the `@Data` annotation with more specific Lombok annotations and exclude the fields causing circular references:

```java
// In Book.java
@Getter
@Setter
@ToString(exclude = "author")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    // Fields and methods
}

// In Author.java
@Getter
@Setter
@ToString(exclude = "books")
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    // Fields and methods
}
```

This solution prevents the infinite recursion by excluding the fields that cause the circular reference from the `toString()` methods.

### 2. View Resolver Conflict

**Challenge:**
The application had both Thymeleaf and JSP configured, causing conflicts in view resolution. When accessing `/books`, Spring was trying to find a Thymeleaf template called "listBooks" instead of using the JSP file.

**Error Message:**
```
Error resolving template [listBooks], template might not exist or might not be accessible by any of the configured Template Resolvers
```

**Solution:**
Disable Thymeleaf in the `application.properties` file:

```properties
# Disable Thymeleaf
spring.thymeleaf.enabled=false
```

This ensures that Spring uses the JSP view resolver to find and render views.

### 3. Lazy Loading and Session Management

**Challenge:**
Using `FetchType.LAZY` for the `@ManyToOne` relationship in the `Book` entity can cause `LazyInitializationException` when trying to access the author outside of a transaction.

**Solution:**
Ensure that all access to lazy-loaded relationships occurs within a transaction by:
1. Using `@Transactional` on service methods that need to access these relationships
2. Fetching the required data eagerly when needed

```java
@Transactional
public Book updateBook(Book book) {
    return bookRepository.save(book);
}
```

### 4. Form Handling with Entity Relationships

**Challenge:**
When submitting forms with entity relationships (e.g., selecting an author for a book), Spring MVC needs to convert the form data to the appropriate entity objects.

**Solution:**
Use `@ModelAttribute` in the controller methods and ensure that the form fields correctly map to the entity properties:

```java
@PostMapping("/update")
public String updateBook(@ModelAttribute Book book, RedirectAttributes redirectAttributes) {
    try {
        bookService.updateBook(book);
        redirectAttributes.addFlashAttribute("message", "Book updated successfully!");
        return "redirect:/books";
    } catch (Exception e) {
        redirectAttributes.addFlashAttribute("error", "Error updating book: " + e.getMessage());
        return "redirect:/books/update/" + book.getId();
    }
}
```

In the JSP form, use `form:select` with `itemValue` and `itemLabel` to correctly map the selected author:

```html
<form:select path="author" required="true">
    <form:option value="" label="-- Select Author --" />
    <form:options items="${authors}" itemValue="id" itemLabel="name" />
</form:select>
```

These solutions ensured that the application could handle entity relationships correctly in forms and avoid common pitfalls in JPA entity management.

[Github URL](https://github.com/ArchismanMidya/Book_Crud)  https://github.com/ArchismanMidya/Book_Crud
# Bookz_Assignment
