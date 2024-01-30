package com.radovan.spring.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_addresses")
public class OrderAddressEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Integer orderAddressId;

	@Column(nullable = false, length = 75)
	private String address;

	@Column(nullable = false, length = 40)
	private String city;

	@Column(name = "post_code", nullable = false, length = 10)
	private String postcode;

	@OneToOne(mappedBy = "address", fetch = FetchType.EAGER, orphanRemoval = true)
	private OrderEntity order;

	public Integer getOrderAddressId() {
		return orderAddressId;
	}

	public void setOrderAddressId(Integer orderAddressId) {
		this.orderAddressId = orderAddressId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public OrderEntity getOrder() {
		return order;
	}

	public void setOrder(OrderEntity order) {
		this.order = order;
	}

}