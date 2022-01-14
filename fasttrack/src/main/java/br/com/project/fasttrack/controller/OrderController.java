package br.com.project.fasttrack.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.project.fasttrack.controller.dto.OrderDto;
import br.com.project.fasttrack.controller.form.AtualizacaoOrderForm;
import br.com.project.fasttrack.controller.form.OrderForm;
import br.com.project.fasttrack.kafka.KafkaSender;
import br.com.project.fasttrack.model.Order;
import br.com.project.fasttrack.model.StatusOrder;
import br.com.project.fasttrack.repository.OrderRepository;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private KafkaSender kafkaSender;
	
	
	@GetMapping
	public List<OrderDto> lista(){
		List<Order> orders = orderRepository.findAll();
		return OrderDto.converter(orders);
	}
	
	@GetMapping("/search")
	public List<OrderDto> lista(String max_total, String min_total, String status){
		if (max_total != null && min_total == null && status == null){
			List<Order> orders = orderRepository.findOneByTotalLessThanEqual(Long.parseLong(max_total));
			return OrderDto.converter(orders);
		} else if (max_total == null && min_total != null && status == null){
			List<Order> orders = orderRepository.findOneByTotalGreaterThanEqual(Long.parseLong(min_total));
			return OrderDto.converter(orders);
		} else if (max_total != null && min_total != null && status == null){
			List<Order> orders = orderRepository.findOneByTotalGreaterThanEqualAndTotalLessThanEqual(Long.parseLong(min_total), Long.parseLong(max_total));
			return OrderDto.converter(orders);
		} else if (max_total != null && min_total != null && status != null){
			List<Order> orders = orderRepository.findOneByTotalGreaterThanEqualAndTotalLessThanEqualAndStatus(Long.parseLong(min_total), Long.parseLong(max_total), StatusOrder.valueOf(status));
			return OrderDto.converter(orders);
		} else if (max_total != null && min_total == null && status != null){
			List<Order> orders = orderRepository.findOneByTotalLessThanEqualAndStatus(Long.parseLong(max_total), StatusOrder.valueOf(status));
			return OrderDto.converter(orders);
		} else if (max_total == null && min_total == null && status != null){
			List<Order> orders = orderRepository.findOneByStatus(StatusOrder.valueOf(status));
			return OrderDto.converter(orders);
		} else if (max_total == null && min_total != null && status != null){
			List<Order> orders = orderRepository.findOneByTotalGreaterThanEqualAndStatus(Long.parseLong(min_total), StatusOrder.valueOf(status));
			return OrderDto.converter(orders);
		} else {
			List<Order> orders = orderRepository.findAll();
			return OrderDto.converter(orders);
		}
		
	}
	
	@GetMapping("{id}")
	public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id){
		Optional<Order> order = orderRepository.findById(id);
		
		if (order.isPresent()) {
			return ResponseEntity.ok(new OrderDto(order.get()));
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@PostMapping
	public ResponseEntity<OrderDto> cadastrar(@RequestBody OrderForm form, UriComponentsBuilder uriBuilder) throws InterruptedException, ExecutionException {
		Order order = form.converter();
		orderRepository.save(order);
		
		URI uri = uriBuilder.path("/orders/{id}").buildAndExpand(order.getId()).toUri();
		OrderDto orderDto = new OrderDto(order);
		orderDto.setId(order.getId());
		kafkaSender.sendMessage(orderDto.converterParaJson());
		return ResponseEntity.created(uri).body(new OrderDto(order));
	}
	
	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<OrderDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoOrderForm form) {
		Optional<Order> optional = orderRepository.findById(id);
		if (optional.isPresent()) {
			Order order = form.atualizar(id, orderRepository);
			return ResponseEntity.ok(new OrderDto(order));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Order> optional = orderRepository.findById(id);
		if (optional.isPresent()) {
			orderRepository.deleteById(id);
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
