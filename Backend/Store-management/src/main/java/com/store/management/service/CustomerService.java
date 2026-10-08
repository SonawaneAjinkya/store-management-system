package com.store.management.service;

import com.store.management.entity.Customer;
import com.store.management.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer addCustomer(Customer customer) {

        if (customer.getCreatedDate() == null) {
            customer.setCreatedDate(LocalDateTime.now());
        }

        return customerRepository.save(customer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with id: " + id));
    }

    public Customer updateCustomer(
            Integer id,
            Customer updatedCustomer) {

        Customer existingCustomer = getCustomerById(id);

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setPhone(updatedCustomer.getPhone());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setAddress(updatedCustomer.getAddress());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Integer id) {

        if (!customerRepository.existsById(id)) {
            throw new RuntimeException(
                    "Customer not found with id: " + id);
        }

        customerRepository.deleteById(id);
    }

    public Customer getCustomerByPhone(String phone) {
        return customerRepository.findByPhone(phone)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Customer not found with phone: "
                                        + phone));
    }

    public List<Customer> searchCustomers(String name) {
        return customerRepository
                .findByNameContainingIgnoreCase(name);
    }

    public List<Customer> searchCustomersByPhone(String phone) {
        return customerRepository
                .findByPhoneContaining(phone);
    }
}