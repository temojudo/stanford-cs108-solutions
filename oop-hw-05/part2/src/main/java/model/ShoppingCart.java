package model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ShoppingCart {
    private final Map<String, Integer> cart;

    public ShoppingCart() {
        cart = new HashMap<>();
    }

    public void addProduct(String id, String quantityStr, int dflt) {
        int num;

        try {
            num = Integer.parseInt(quantityStr);
        } catch (Exception e) {
            num = dflt;
        }

        if (num > 0) {
            addProduct(id, num);
        }
    }

    public void addProduct(String id, int quantity) {
        if (!cart.containsKey(id)) {
            cart.put(id, quantity);
        } else {
            cart.put(id, cart.get(id) + quantity);
        }
    }

    public Set<String> getproductIds() {
        return cart.keySet();
    }

    public int getQuantityOf(String id) {
        return cart.get(id);
    }
}
