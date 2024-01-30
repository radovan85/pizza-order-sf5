package com.radovan.spring.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@Table(name = "orders")
public class OrderEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer orderId;

	@Column(name = "order_price", nullable = false)
	private Float orderPrice;

	@ManyToOne
	@JoinColumn(name = "cart_id")
	private CartEntity cart;

	@CreationTimestamp
	@Column(name = "created_at", nullable = false)
	private Timestamp createdAt;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.SUBSELECT)
	private List<OrderItemEntity> orderedItems;

	@OneToOne(orphanRemoval = true, fetch = FetchType.EAGER)
	@JoinColumn(name = "address_id")
	private OrderAddressEntity address;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Float getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(Float orderPrice) {
		this.orderPrice = orderPrice;
	}

	public CartEntity getCart() {
		return cart;
	}

	public void setCart(CartEntity cart) {
		this.cart = cart;
	}

	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	public List<OrderItemEntity> getOrderedItems() {
		return orderedItems;
	}

	public void setOrderedItems(List<OrderItemEntity> orderedItems) {
		this.orderedItems = orderedItems;
	}

	public OrderAddressEntity getAddress() {
		return address;
	}

	public void setAddress(OrderAddressEntity address) {
		this.address = address;
	}

}
