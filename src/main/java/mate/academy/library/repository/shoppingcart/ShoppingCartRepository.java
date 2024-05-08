package mate.academy.library.repository.shoppingcart;

import mate.academy.library.model.ShoppingCart;
import mate.academy.library.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    ShoppingCart findShoppingCartByUser(User user);
}
