package aquarium.aquariumstore.controller;

import aquarium.aquariumstore.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import aquarium.aquariumstore.model.Product;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

// Клас ProductController є контролером в Spring MVC,
// який відповідає за обробку запитів пов’язаних з товарами
// та взаємодіє з репозиторієм ProductRepo для отримання даних.

@Controller
public class ProductController {

    @Autowired
    private ProductRepo productRepo;


    // Відображення сторінки з товарами відповідно параметрів sortField та sortDir
    @GetMapping("/products")
    public String showProducts(@RequestParam(value = "sortField", required = false, defaultValue = "product_id") String sortField,
                               @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                               Model model) {
        List<Product> products = productRepo.findAll(sortField, sortDir);
        model.addAttribute("products", products);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "products";
    }

    // Відображення сторінки з товарами за певною категорією
    @GetMapping("/products/category/{categoryId}")
    public String showProductsByCategory(@PathVariable("categoryId") int categoryId,
                                         @RequestParam(value = "sortField", required = false, defaultValue = "product_id") String sortField,
                                         @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                         Model model) {
        List<Product> products = productRepo.getProductsByCategory(categoryId, sortField, sortDir);
        model.addAttribute("products", products);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "products";
    }

    // Відображення сторінки з товарами за певною підкатегорією
    @GetMapping("/product_detail/category/{categoryId}")
    public String showProductsByCategoryD(@PathVariable("categoryId") int categoryId,
                                          @RequestParam(value = "sortField", required = false, defaultValue = "product_id") String sortField,
                                          @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                          Model model) {
        List<Product> products = productRepo.getProductsByCategory(categoryId, sortField, sortDir);
        model.addAttribute("products", products);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "products";
    }

    // Відображення сторінки з товарами за певною категорією
    @GetMapping("/cart/category/{categoryId}")
    public String showProductsByCategoryC(@PathVariable("categoryId") int categoryId,
                                          @RequestParam(value = "sortField", required = false, defaultValue = "product_id") String sortField,
                                          @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                          Model model) {
        List<Product> products = productRepo.getProductsByCategory(categoryId, sortField, sortDir);
        model.addAttribute("products", products);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "products";
    }


    // Відображення сторінки з товарами за певною підкатегорією
    @GetMapping("/products/type/{typeId}")
    public String showProductsByType(@PathVariable("typeId") int typeId,
                                     @RequestParam(value = "sortField", required = false, defaultValue = "product_id") String sortField,
                                     @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                     Model model) {
        List<Product> products = productRepo.getProductsByType(typeId, sortField, sortDir);
        model.addAttribute("products", products);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "products";
    }

    // Відображення сторінки з товарами за певною підкатегорією
    @GetMapping("/product_detail/type/{typeId}")
    public String showProductsByTypeD(@PathVariable("typeId") int typeId,
                                      @RequestParam(value = "sortField", required = false, defaultValue = "product_id") String sortField,
                                      @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                      Model model) {
        List<Product> products = productRepo.getProductsByType(typeId, sortField, sortDir);
        model.addAttribute("products", products);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "products";
    }

    // Відображення сторінки з товарами за певною підкатегорією
    @GetMapping("/cart/type/{typeId}")
    public String showProductsByTypeC(@PathVariable("typeId") int typeId,
                                      @RequestParam(value = "sortField", required = false, defaultValue = "product_id") String sortField,
                                      @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                      Model model) {
        List<Product> products = productRepo.getProductsByType(typeId, sortField, sortDir);
        model.addAttribute("products", products);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        return "products";
    }

    // Відображення сторінки з повною інформацією про товар
    @GetMapping("/products/{id}")
    public String showProductDetail(@PathVariable("id") int id, Model model) {
        Product product = productRepo.getProductByID(id);
        model.addAttribute("product", product);
        return "product_details";
    }
}