package Entity;

import Entity.SimCardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimCardTransactionRepo extends JpaRepository<SimCardTransaction, Long> {

}
