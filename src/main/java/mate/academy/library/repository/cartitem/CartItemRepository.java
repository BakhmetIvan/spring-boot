package mate.academy.library.repository.cartitem;

import mate.academy.library.model.CartItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Page<CartItem> findAllByShoppingCartId(Long id, Pageable pageable);
}
