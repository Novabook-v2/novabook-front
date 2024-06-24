package store.novabook.front.store.cart;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/carts")
@Controller
public class CartController {

    @GetMapping
    public String getCartAll() {
        return "store/cart/cart_list";
    }
}
