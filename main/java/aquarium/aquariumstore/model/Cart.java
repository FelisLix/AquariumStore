package aquarium.aquariumstore.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class Cart {
    private ArrayList<CartItem> listOfItems;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CartItem {
        private Product product;
        private int quantity;
        public CartItem(Product product){
            this.product = product;
            this.quantity = 1;
        }
    }
    public Cart(){
        this.listOfItems = new ArrayList<>();
    }
    public void addToCart(CartItem cartItem){
       listOfItems.add(cartItem);
    }

    public void removeFromCart(int productId){
        listOfItems.removeIf(item -> item.getProduct().getID() == productId);
    }
    public ArrayList<CartItem> showCart(){
        return listOfItems;
    }
    public void emptyCart(){
        listOfItems.clear();
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem item : listOfItems) {
            total += item.getProduct().getPrice() * item.getQuantity();
        }
        return total;
    }
}
