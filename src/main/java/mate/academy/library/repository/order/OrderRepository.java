package mate.academy.library.repository.order;

import mate.academy.library.model.Order;
import mate.academy.library.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUser(User user, Pageable pageable);
}
