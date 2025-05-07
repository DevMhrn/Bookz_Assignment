# Literary Works Management System

## Entity Relationship Design

### Entity Model

The application is built around two main entities with a one-to-many relationship:

1.  **Author Entity (Literary Creator)**:
    *   `creatorId`: Long (Primary Key)
    *   `name`: String
    *   `bio`: String
    *   One-to-Many relationship with Books (Literary Works)

2.  **Book Entity (Literary Work)**:
    *   `id`: Long (Primary Key)
    *   `title`: String
    *   `isbn`: String
    *   Many-to-One relationship with Author (Literary Creator)

### Entity Relationship Diagram

```
┌─────────────────────┐       ┌─────────────────────┐
│   Literary Creator  │       │    Literary Work    │
├─────────────────────┤       ├─────────────────────┤
│ creatorId          │       │ id                  │
│ name                │       │ title               │
│ bio                 │       │ isbn                │
│                     │       │ creator_id         │
└─────────────────────┘       └─────────────────────┘
      │ 1                 ▲ *
      └─────────────────────┘
```

### Implementation in Code

The entities were implemented using JPA annotations:

**Author.java**
```java
@Entity
@Table(name = "literary_creator")
@Getter
@Setter
@ToString(exclude = "literaryWorks")
@NoArgsConstructor
@AllArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long creatorId;
    
    @Column(name = "full_name", nullable = false)
    private String name;
    
    @Column(name = "biography", length = 2000)
    private String bio;
    
    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> literaryWorks = new HashSet<>();
    
    // Convenience methods for relationship management
    public void linkLiteraryWork(Book literaryWork) {
        literaryWorks.add(literaryWork);
        literaryWork.setCreator(this);
    }
    
    public void unlinkLiteraryWork(Book literaryWork) {
        literaryWorks.remove(literaryWork);
        literaryWork.setCreator(null);
    }
}
```

**Book.java**
```java
@Entity
@Table(name = "literary_work")
@Getter
@Setter
@ToString(exclude = "creator")
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_id")
    private Long id;
    
    @Column(name = "work_title", nullable = false)
    private String title;
    
    @Column(name = "international_code", unique = true)
    private String isbn;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private Author creator;
    
    // Constructor without id for easier creation
    public Book(String title, String isbn, Author creator) {
        this.title = title;
        this.isbn = isbn;
        this.creator = creator;
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
    private final AuthorService creatorService;
    private final BookService literaryService;
    
    @Autowired
    public DataInitializer(AuthorService creatorService, BookService literaryService) {
        this.creatorService = creatorService;
        this.literaryService = literaryService;
    }
    
    @Override
    public void run(String... args) throws Exception {
        // Create sample authors and books
        Author author1 = new Author();
        author1.setName("J.K. Rowling");
        author1.setBio("British author best known for the Harry Potter series.");
        creatorService.registerCreator(author1);
        
        Book book1 = new Book();
        book1.setTitle("Harry Potter and the Philosopher's Stone");
        book1.setIsbn("9780747532743");
        book1.setCreator(author1);
        literaryService.registerLiteraryWork(book1);
        
        // Additional authors and books...
    }
}
```

### 2. Create Operations

#### Adding a New Author (Literary Creator)

**Controller Code (AuthorController.java)**
```java
@GetMapping("/register")
public String showCreatorRegistrationForm(Model model) {
    model.addAttribute("literaryCreator", new Author());
    return "registerCreator";
}

@PostMapping("/register")
public String processCreatorRegistration(@ModelAttribute("literaryCreator") Author creator,
                                       RedirectAttributes notification) {
    try {
        creatorService.registerCreator(creator);
        notification.addFlashAttribute("notification", "Literary creator registered successfully");
        return "redirect:/creators";
    } catch (Exception e) {
        notification.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
        return "redirect:/creators/register";
    }
}
```

**JSP Form (registerCreator.jsp)**
```html
<form:form action="/creators/register" method="post" modelAttribute="literaryCreator">
    <div class="form-group">
        <form:label path="name">Name</form:label>
        <form:input path="name" required="true" />
    </div>
    <div class="form-group">
        <form:label path="bio">Bio</form:label>
        <form:textarea path="bio" rows="5" />
    </div>
    <button type="submit" class="btn">Save Author</button>
    <a href="<c:url value='/creators' />" class="btn">Cancel</a>
</form:form>
```

#### Adding a New Book (Literary Work)

**Controller Code (BookController.java)**
```java
@GetMapping("/register")
public String showRegistrationForm(Model model) {
    model.addAttribute("literaryWork", new Book());
    model.addAttribute("creators", creatorService.findAllCreators());
    return "registerWork";
}

@PostMapping("/register")
public String processWorkRegistration(@ModelAttribute("literaryWork") Book work, 
                                     RedirectAttributes notification) {
    try {
        literaryService.registerLiteraryWork(work);
        notification.addFlashAttribute("notification", "Literary work successfully registered");
        return "redirect:/catalog";
    } catch (Exception e) {
        notification.addFlashAttribute("errorMessage", "Registration failed: " + e.getMessage());
        return "redirect:/catalog/register";
    }
}
```

**JSP Form (registerWork.jsp)**
```html
<form:form action="/catalog/register" method="post" modelAttribute="literaryWork">
    <div class="form-group">
        <form:label path="title">Title</form:label>
        <form:input path="title" required="true" />
    </div>
    <div class="form-group">
        <form:label path="isbn">ISBN</form:label>
        <form:input path="isbn" required="true" />
    </div>
    <div class="form-group">
        <form:label path="creator">Author</form:label>
        <form:select path="creator" required="true">
            <form:option value="" label="-- Select Author --" />
            <form:options items="${creators}" itemValue="creatorId" itemLabel="name" />
        </form:select>
    </div>
    <button type="submit" class="btn">Save Book</button>
    <a href="<c:url value='/catalog' />" class="btn">Cancel</a>
</form:form>
```

### 3. Read Operations

#### Listing All Authors (Literary Creators)

**Controller Code (AuthorController.java)**
```java
@GetMapping
public String displayCreatorDirectory(Model model) {
    model.addAttribute("creatorDirectory", creatorService.findAllCreators());
    return "creatorDirectory";
}
```

**JSP View (creatorDirectory.jsp)**
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
        <c:forEach var="creator" items="${creatorDirectory}">
            <tr>
                <td>${creator.creatorId}</td>
                <td>${creator.name}</td>
                <td>${creator.bio}</td>
                <td>
                    <a href="<c:url value='/creators/edit/${creator.creatorId}' />" class="btn">Edit</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
```

#### Listing All Books (Literary Works)

**Controller Code (BookController.java)**
```java
@GetMapping
public String displayCatalog(Model model) {
    model.addAttribute("literaryWorks", literaryService.browseCatalog());
    return "catalogView";
}
```

**JSP View (catalogView.jsp)**
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
        <c:forEach var="publication" items="${literaryWorks}">
            <tr>
                <td>${publication.id}</td>
                <td>${publication.title}</td>
                <td>${publication.isbn}</td>
                <td>${publication.creator.name}</td>
                <td>
                    <a href="<c:url value='/catalog/edit/${publication.id}' />" class="btn">Edit</a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
```

#### Custom Query for Books with Authors (Detailed Catalog View)

**Repository Code (BookRepository.java)**
```java
@Query("SELECT w.title, w.isbn, c.name FROM Book w JOIN w.creator c")
List<Object[]> retrieveWorksWithCreators();
```

**Controller Code (BookController.java)**
```java
@GetMapping("/detailed-view")
public String detailedCatalogView(Model model) {
    List<Object[]> catalogWithCreators = literaryService.getCatalogWithCreators();
    model.addAttribute("catalogItems", catalogWithCreators);
    return "detailedCatalogView";
}
```

**JSP View (detailedCatalogView.jsp)**
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
        <c:forEach var="catalogEntry" items="${catalogItems}">
            <tr>
                <td>${catalogEntry[0]}</td> <!-- Title -->
                <td>${catalogEntry[1]}</td> <!-- ISBN -->
                <td>${catalogEntry[2]}</td> <!-- Author Name -->
            </tr>
        </c:forEach>
    </tbody>
</table>
```

### 4. Update Operations

#### Updating an Author (Literary Creator)

**Controller Code (AuthorController.java)**
```java
@GetMapping("/edit/{creatorId}")
public String showCreatorEditForm(@PathVariable Long creatorId, Model model,
                                 RedirectAttributes notification) {
    Optional<Author> creatorToEdit = creatorService.findCreatorById(creatorId);
    if (creatorToEdit.isPresent()) {
        model.addAttribute("literaryCreator", creatorToEdit.get());
        return "editCreatorProfile";
    } else {
        notification.addFlashAttribute("errorMessage", "Creator not found in directory");
        return "redirect:/creators";
    }
}

@PostMapping("/edit")
public String processProfileUpdate(@ModelAttribute("literaryCreator") Author updatedCreator,
                                  RedirectAttributes notification) {
    try {
        creatorService.modifyCreatorDetails(updatedCreator);
        notification.addFlashAttribute("notification", "Creator profile updated");
        return "redirect:/creators";
    } catch (Exception e) {
        notification.addFlashAttribute("errorMessage", "Update failed: " + e.getMessage());
        return "redirect:/creators/edit/" + updatedCreator.getCreatorId();
    }
}
```

**JSP Form (editCreatorProfile.jsp)**
```html
<form:form action="/creators/edit" method="post" modelAttribute="literaryCreator">
    <form:hidden path="creatorId" />
    <div class="form-group">
        <form:label path="name">Name</form:label>
        <form:input path="name" required="true" />
    </div>
    <div class="form-group">
        <form:label path="bio">Bio</form:label>
        <form:textarea path="bio" rows="5" />
    </div>
    <button type="submit" class="btn">Update Author</button>
    <a href="<c:url value='/creators' />" class="btn">Cancel</a>
</form:form>
```

#### Updating a Book (Literary Work)

**Controller Code (BookController.java)**
```java
@GetMapping("/edit/{workId}")
public String showEditForm(@PathVariable Long workId, Model model, 
                          RedirectAttributes notification) {
    Optional<Book> workToEdit = literaryService.locateLiteraryWork(workId);
    if (workToEdit.isPresent()) {
        model.addAttribute("literaryWork", workToEdit.get());
        model.addAttribute("creators", creatorService.findAllCreators());
        return "editWorkForm";
    } else {
        notification.addFlashAttribute("errorMessage", "Literary work not found in catalog");
        return "redirect:/catalog";
    }
}

@PostMapping("/edit")
public String processWorkUpdate(@ModelAttribute("literaryWork") Book updatedWork, 
                               RedirectAttributes notification) {
    try {
        literaryService.reviseWorkDetails(updatedWork);
        notification.addFlashAttribute("notification", "Literary work details updated");
        return "redirect:/catalog";
    } catch (Exception e) {
        notification.addFlashAttribute("errorMessage", "Update failed: " + e.getMessage());
        return "redirect:/catalog/edit/" + updatedWork.getId();
    }
}
```

**JSP Form (editWorkForm.jsp)**
```html
<form:form action="/catalog/edit" method="post" modelAttribute="literaryWork">
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
        <form:label path="creator">Author</form:label>
        <form:select path="creator" required="true">
            <form:option value="" label="-- Select Author --" />
            <form:options items="${creators}" itemValue="creatorId" itemLabel="name" />
        </form:select>
    </div>
    <button type="submit" class="btn">Update Book</button>
    <a href="<c:url value='/catalog' />" class="btn">Cancel</a>
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
@ToString(exclude = "creator") // Updated from "author"
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    // Fields and methods
}

// In Author.java
@Getter
@Setter
@ToString(exclude = "literaryWorks") // Updated from "books"
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
// Example from BookService.java
@Transactional
public Book reviseWorkDetails(Book revisedWork) { // Updated method signature
    // Ensure work exists before updating
    if (revisedWork.getId() == null || 
        !literaryWorkRepository.existsById(revisedWork.getId())) { // Assuming literaryWorkRepository
        throw new IllegalArgumentException("Cannot update non-existent literary work");
    }
    return literaryWorkRepository.save(revisedWork); // Assuming literaryWorkRepository
}
```

### 4. Form Handling with Entity Relationships

**Challenge:**
When submitting forms with entity relationships (e.g., selecting an author for a book), Spring MVC needs to convert the form data to the appropriate entity objects.

**Solution:**
Use `@ModelAttribute` in the controller methods and ensure that the form fields correctly map to the entity properties:

```java
// Example from BookController.java
@PostMapping("/edit") // Updated path
public String processWorkUpdate(@ModelAttribute("literaryWork") Book updatedWork, // Updated parameter
                               RedirectAttributes notification) {
    try {
        literaryService.reviseWorkDetails(updatedWork); // Updated service call
        notification.addFlashAttribute("notification", "Literary work details updated"); // Updated flash attribute
        return "redirect:/catalog"; // Updated redirect path
    } catch (Exception e) {
        notification.addFlashAttribute("errorMessage", "Update failed: " + e.getMessage()); // Updated flash attribute
        return "redirect:/catalog/edit/" + updatedWork.getId(); // Updated redirect path
    }
}
```

In the JSP form, use `form:select` with `itemValue` and `itemLabel` to correctly map the selected author:

```html
<form:select path="creator" required="true"> <!-- path updated to "creator" -->
    <form:option value="" label="-- Select Author --" />
    <form:options items="${creators}" itemValue="creatorId" itemLabel="name" /> <!-- items updated, itemValue updated -->
</form:select>
```

These solutions ensured that the application could handle entity relationships correctly in forms and avoid common pitfalls in JPA entity management.
