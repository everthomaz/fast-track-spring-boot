package br.com.project.fasttrack.consumer.model;

import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.transaction.annotation.Transactional;

import br.com.project.fasttrack.consumer.repository.OrderRepository;

@Entity(name="orders")
public class Order {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long id;
		
		private String name;
		
		private Long total;
		
		@Enumerated(EnumType.STRING)
		private StatusOrder status = StatusOrder.NOT_PROCESSED;
		
		private String description;
		
		public Order() {
		}
		
		public Order(String description, String name, Long total, StatusOrder status) {
			this.name = name;
			this.total = total;
			this.status = status;
			this.description = description;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public Long getTotal() {
			return total;
		}

		public void setTotal(Long total) {
			this.total = total;
		}

		public StatusOrder getStatus() {
			return status;
		}
		
		public void setStatus(StatusOrder status) {
			this.status = status;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}
}
