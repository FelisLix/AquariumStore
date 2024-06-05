package aquarium.aquariumstore.controller;


import aquarium.aquariumstore.model.*;

import aquarium.aquariumstore.repository.ClientRepo;
import aquarium.aquariumstore.repository.OfficeRepo;
import aquarium.aquariumstore.repository.OrderRepo;
import aquarium.aquariumstore.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

// Клас OfficeController є контролером в Spring MVC,
// який відповідає за обробку запитів до сторінок пов’язаних зі сторінками де працює адміністратор або співробітники
// та взаємодіє з репозиторіями OfficeRepo, OrderRepo, ClientRepo та ProductRepo для отримання даних.

@Controller
public class OfficeController {

    // Репозиторії для отримання даних
    @Autowired
    OfficeRepo officeRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ClientRepo clientRepo;

    @Autowired
    ProductRepo productRepo;

    // Відображення головної сторінки "офісу"
    @GetMapping("/office")
    public String adminOffice() {
        return "office";
    }



    // Відображення сторінки зі списком замовлень
    @GetMapping("/order_list")
    public String showOrders(Model model) {

        List<AboutOrder> orders = officeRepo.findAllOrdersInfo();
        model.addAttribute("orders", orders);

        List<Employee> employees = officeRepo.findAllSalesRepresentative();
        model.addAttribute("employees", employees);

        Map<Integer, List<OrderLineInfo>> orderLinesMap = new HashMap<>();

        for (AboutOrder aboutOrder : orders) {
            List<OrderLineInfo> orderLines = officeRepo.findLines(aboutOrder.getOrderId());
            orderLinesMap.put(aboutOrder.getOrderId(), orderLines);
        }
        model.addAttribute("orderLinesMap", orderLinesMap);
        return "order_list";
    }

    // Обробка запиту для оновлення даних про замовлення
    @PostMapping("/order_list/update_order/{orderId}")
    public String updateOrder(@RequestParam(value = "orderStatus", required = false) Optional<String> orderStatus,
                              @RequestParam(value = "paymentStatus", required = false) Optional<String> paymentStatus,
                              @RequestParam(value = "employeeId") int employeeId,
                              @PathVariable("orderId") int orderId) {
        orderStatus.ifPresent(status -> officeRepo.setOrderStatus(orderId, status, employeeId));
        paymentStatus.ifPresent(status -> officeRepo.setPaymentStatus(orderId, status));
        return "redirect:/order_list";
    }

    // Обробка запиту для видалення замовлення
    @PostMapping("/order_list/delete_order/{orderId}")
    public String deleteOrder(@PathVariable("orderId") int orderId) {
        officeRepo.deleteOrder(orderId);
        return "redirect:/order_list";
    }


    // Відображення сторінки зі списком покупців
    @GetMapping("/customers")
    public  String showClients(Model model){
        List<Customer> customers = clientRepo.findAll();
        model.addAttribute("customers", customers);
        return "customers";
    }

    // Обробка запиту для видалення покупця
    @PostMapping("/customers/delete_customer/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") int customerId) {
        clientRepo.deleteCustomer(customerId);
        return "redirect:/customers";
    }

    // Відображення сторінки зі списком співробітників (користувачів)
    @GetMapping("/users")
    public  String showEmployees(Model model){
        List<Employee> employees = officeRepo.findAllEmployees();
        model.addAttribute("employees", employees);
        return "users";
    }

    // Обробка запиту для оновлення даних про співробітника
    @PostMapping("/employee/update")
    public String updateEmployee(@RequestParam("employeeId") int employeeId,
                                 @RequestParam("firstName") String firstName,
                                 @RequestParam("lastName") String lastName,
                                 @RequestParam("title") String title,
                                 @RequestParam("phone") String phone,
                                 @RequestParam("email") String email,
                                 @RequestParam("country") String country,
                                 @RequestParam("city") String city,
                                 @RequestParam("hireDate") String hireDate,
                                 @RequestParam("birthDate") String birthDate) {
        officeRepo.updateEmployee(employeeId, firstName, lastName,
                title, phone, email, country, city, hireDate, birthDate);
        return "redirect:/users";
    }

    // Обробка запиту для додавання нового співробітника
    @PostMapping("/employee/add_new")
    public String addNewEmployee(@RequestParam("firstNameN") String firstName,
                                 @RequestParam("lastNameN") String lastName,
                                 @RequestParam("titleN") String title,
                                 @RequestParam("phoneN") String phone,
                                 @RequestParam("emailN") String email,
                                 @RequestParam("countryN") String country,
                                 @RequestParam("cityN") String city,
                                 @RequestParam("hireDateN") String hireDate,
                                 @RequestParam("birthDateN") String birthDate) {
        officeRepo.addNewEmployee(firstName, lastName,
                title, phone, email, country, city, hireDate, birthDate);
        return "redirect:/users";
    }

    // Відображення сторінки зі списком усіх товарів
    @GetMapping("/products_editor")
    public String showListOfProducts(Model model) {
        List<Product> products = productRepo.findAllProducts();
        List<Category> categories = productRepo.findAllCategories();
        List<Type> types = productRepo.findAllTypes();
        List<Image> images = productRepo.findAllImages();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        model.addAttribute("images", images);
        return "products_editor";
    }

    // Обробка запиту для пошуку товарів за назвою
    @GetMapping("/products_editor/search")
    public String searchProducts(@RequestParam String searchTerm, Model model) {
        List<Product> list = productRepo.findProductByName(searchTerm);
        model.addAttribute("products", list);
        return "products_editor";
    }


    // Обробка запиту для оновлення даних про товар
    @PostMapping("/product/update")
    public String updateProduct(@RequestParam("productId")int productId, @RequestParam("name") String name,
                                @RequestParam("price") double price, @RequestParam("quantity") int quantity,
                                 @RequestParam("size") String size,
                                @RequestParam("description") String description, @RequestParam("imageURL") String imageURL,
                                @RequestParam("lifespan") int lifespan,  @RequestParam("imageID") int imageID,
                                @RequestParam("origin") String origin) {
        productRepo.updateProduct(productId, name, price, quantity, size, description, imageURL, lifespan, origin, imageID);
        return "redirect:/products_editor";
    }



    // Обробка запиту для видалення товару за ID
    @PostMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productRepo.deleteProduct(id);
        return "redirect:/products_editor";
    }


    // Обробка запиту для додавання нового товару
    @PostMapping("/product/add")
    public String addProduct(@RequestParam("nameN") String name, @RequestParam("priceN") double price,
                             @RequestParam("quantityN") int quantity, @RequestParam("sizeN") String size,
                             @RequestParam("descriptionN") String description, @RequestParam("lifespanN") int lifespan,
                             @RequestParam("imageIdN") int imageID, @RequestParam("originN") String origin,
                             @RequestParam("typeIdN") int typeId) {
        Timestamp createdAt = new Timestamp(System.currentTimeMillis());
        productRepo.addNewProduct(name, typeId, price, quantity, size, description, createdAt, imageID, lifespan, origin);
        return "redirect:/products_editor";
    }


    // Відображення сторінки зі списком усіх категорій та підкатегорій
    @GetMapping("/categories_and_types")
    public String showListOfCatTypes(Model model) {
        List<Category> categories = productRepo.findAllCategories();
        List<Type> types = productRepo.findAllTypes();
        model.addAttribute("categories", categories);
        model.addAttribute("types", types);
        return "categories_and_types";
    }

    // Обробка запиту для додавання нової категорії
    @PostMapping("/category/add_new")
     public String addCategory(@RequestParam("categoryName") String name) {
        productRepo.addNewCategory(name);
        return "redirect:/categories_and_types";
    }

    // Обробка запиту для додавання нової підкатегорії
    @PostMapping("/type/add_new")
    public String addType(@RequestParam("typeName") String name, @RequestParam("typeCat") int category) {
        productRepo.addNewType(name, category);
        return "redirect:/categories_and_types";
    }

    // Обробка запиту для видалення категорії
    @PostMapping("/category/delete/{id}")
    public String deleteCategory(@PathVariable() int id) {
        productRepo.deleteCategory(id);
        return "redirect:/categories_and_types";
    }

    // Обробка запиту для видалення підкатегорії
    @PostMapping("/type/delete/{id}")
    public String deleteType(@PathVariable int id) {
        productRepo.deleteType(id);
        return "redirect:/categories_and_types";
    }

    // Відображення сторінки зі списком усіх медіафайлів
    @GetMapping("/images")
    public String showImages(Model model) {
        List<Image> images = productRepo.findAllImages();
        model.addAttribute("images", images);
        return "images";
    }

    // Обробка запиту для додавання нового медіафайлу
    @PostMapping("/image/add_new")
    public String addImage(@RequestParam("imageURL") String url) {
        productRepo.addImage(url);
        return "redirect:/images";
    }

    // Обробка запиту для видалення медіафайлу
    @PostMapping("/image/delete/{id}")
    public String deleteImage(@PathVariable int id) {
        productRepo.deleteImage(id);
        return "redirect:/images";
    }

}
