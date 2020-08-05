package PEJ;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface TaegbaeRepository extends PagingAndSortingRepository<Taegbae, Long>{

    Optional<Taegbae> findAllByPurchaseId(String purchaseId);

}