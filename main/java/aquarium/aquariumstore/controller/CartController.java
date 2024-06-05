package aquarium.aquariumstore.controller;

import aquarium.aquariumstore.model.*;
import aquarium.aquariumstore.repository.ClientRepo;
import aquarium.aquariumstore.repository.OrderRepo;
import aquarium.aquariumstore.repository.ProductRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

// Клас CartController є контролером в Spring MVC,
// який відповідає за обробку запитів до сторінок пов’язаних з кошиком та оформленням замовлення
// та взаємодіє з репозиторіями ProductRepo, ClientRepo та OrderRepo для отримання даних.

@Controller
public class CartController {

    // Репозиторії для отримання даних
    @Autowired
    private ProductRepo productRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private OrderRepo orderRepo;

    // Кошик
    private final Cart cart = new Cart();

    // Відображення вмісту кошика
    @GetMapping("/cart")
    public String showProducts(Model model) {
        List<Cart.CartItem> itemsInCart = cart.showCart();
        double totalPrice = itemsInCart.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
        String formattedTotalPrice = String.format("%.2f", totalPrice);
        model.addAttribute("cartItems", itemsInCart);
        model.addAttribute("totalPrice", formattedTotalPrice);
        return "cart";
    }

    // Обробка запиту для додавання товару у кошик
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam("productId") int productId, @RequestParam("quantity") int quantity, HttpServletRequest request) {
        Product product = productRepo.getProductByID(productId);
        cart.addToCart(new Cart.CartItem(product, quantity));
        System.out.println(cart.showCart());
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    // Обробка запиту для видалення продукту з кошика
    @PostMapping("/cart/remove")
    public String removeFromCart(@RequestParam("productId") int productId, HttpServletRequest request) {
        cart.removeFromCart(productId);
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }


    // Відображення сторінки для оформлення замовлення
    @GetMapping("/order")
    public String orderPage(Model model) {
        List<Cart.CartItem> itemsInCart = cart.showCart();
        double totalPrice = itemsInCart.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
        String formattedTotalPrice = String.format("%.2f", totalPrice);
        model.addAttribute("cartItems", itemsInCart);
        model.addAttribute("totalPrice", formattedTotalPrice);
        return "order";
    }

    // Оформлення замовлення
    @PostMapping("/order/new_order")
    public String makeAnOrder(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
                              @RequestParam("phone") String phone, @RequestParam("email") String email,
                              @RequestParam("country") String country, @RequestParam("city") String city,
                              @RequestParam("street") String street, @RequestParam("building") String building,
                              @RequestParam("details") String details, @RequestParam("postalCode") String postalCode,
                              @RequestParam("payment") String payment, Model model) {
        Customer newCustomer = new Customer();
        newCustomer.setFirstName(firstName);
        newCustomer.setLastName(lastName);
        newCustomer.setPhone(phone);
        newCustomer.setEmail(email);

        int newClientID = clientRepo.addNewCustomer(newCustomer);

        Address newAddress = new Address();
        newAddress.setCountry(country);
        newAddress.setCity(city);
        newAddress.setStreet(street);
        newAddress.setBuilding(building);
        newAddress.setDetails(details);
        newAddress.setPostalCode(postalCode);
        newAddress.setCustomerID(newClientID);

        orderRepo.addNewAddress(newAddress);

        double total = cart.calculateTotal();
        Date orderDate = new Date(Calendar.getInstance().getTimeInMillis());

        int newOrderId = orderRepo.addNewOrder(newClientID, total, orderDate);

        Payment newPayment = new Payment();
        newPayment.setMethod(payment);
        newPayment.setOrderID(newOrderId);
        orderRepo.addNewPayment(newPayment);

        orderRepo.addAllOrderLines(newOrderId, cart);

        cart.emptyCart();
        model.addAttribute("orderId", newOrderId);
        return "congrats";
    }

}