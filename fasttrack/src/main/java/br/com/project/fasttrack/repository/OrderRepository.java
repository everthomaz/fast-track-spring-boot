package br.com.project.fasttrack.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.project.fasttrack.model.Order;
import br.com.project.fasttrack.model.StatusOrder;

public interface OrderRepository extends JpaRepository<Order, Long> {
	
	List<Order> findOneByTotalLessThanEqual(Long max_total);
	
	List<Order> findOneByTotalGreaterThanEqual(Long min_total);
	
	List<Order> findOneByStatus(StatusOrder status);
	
	List<Order> findOneByTotalGreaterThanEqualAndTotalLessThanEqual(Long min_total, Long max_total);
	
	List<Order> findOneByTotalGreaterThanEqualAndTotalLessThanEqualAndStatus(Long min_total, Long max_total, StatusOrder status);
	
	List<Order> findOneByTotalLessThanEqualAndStatus(Long max_total, StatusOrder status);
	
	List<Order> findOneByTotalGreaterThanEqualAndStatus(Long min_total, StatusOrder status);
}
