package com.spring.cosmos.ebookstore.controller.Order;

import com.azure.cosmos.models.PartitionKey;
import com.spring.cosmos.ebookstore.model.cart.CartService;
import com.spring.cosmos.ebookstore.model.cart.Cart;
import com.spring.cosmos.ebookstore.model.order.OrderRepository;
import com.spring.cosmos.ebookstore.model.order.LineItem;
import com.spring.cosmos.ebookstore.model.order.Order;
import com.spring.cosmos.ebookstore.model.user.Address;
import com.spring.cosmos.ebookstore.model.user.User;
import com.spring.cosmos.ebookstore.model.user.UserRepository;
import com.spring.cosmos.ebookstore.security.SecuredUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private OrderRepository orderRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, CartService cartService, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @PostMapping(value = "/ebooks/order/create")
    public String createOrder(OrderForm orderForm, Model model, HttpSession session, @AuthenticationPrincipal SecuredUser securedUser) {

        orderRepository.save(getOrderFromOrderForm(orderForm));
        if (orderForm.getStreetAddress() != null) {
            User user =getUserFromOrderForm(orderForm);
            userRepository.save(user);
            securedUser.setAddress(user.getAddress());
        }
        cartService.deleteCart(session.getId(), session.getId());
        model.addAttribute("customer", securedUser);
        return "orderconfirmation";

    }

    @PostMapping(value = "/ebooks/order/checkout")
    public String checkOut(@ModelAttribute Cart cart, Model model, HttpSession session, @AuthenticationPrincipal SecuredUser securedUser) {

        model.addAttribute("customer", securedUser);
        model.addAttribute("order", getOrder(cart, securedUser.getUsername()));
        model.addAttribute("cartItemCount", cartService.getNumberOfItemsInTheCart(session.getId()));
        return "checkout";
    }


    @GetMapping(value = "/ebooks/order/customer/{customerId}")
    public String getCustomerOrders(@PathVariable String customerId, Model model, HttpSession session, @AuthenticationPrincipal SecuredUser securedUser) {
        model.addAttribute("customer", securedUser);
        model.addAttribute("orders", orderRepository.getOrdersByCustomerId(customerId));
        model.addAttribute("cartItemCount", cartService.getNumberOfItemsInTheCart(session.getId()));
        return "orders";
    }

    private Order getOrder(Cart cart, String customerId) {
        Order order = new Order();
        order.setCustomerId(customerId);
        order.setStatus("SHIPPED");
        order.setSubTotal(cart.getSubTotal());
        List<LineItem> lineItems = new ArrayList<>();
        order.setLineItems(lineItems);
        cart
                .getItems()
                .forEach(cartItem -> {
                    LineItem lineItem = new LineItem();
                    lineItem.setId(cartItem.getId());
                    lineItem.setTitle(cartItem.getTitle());
                    lineItem.setDescription(cartItem.getDescription());
                    lineItem.setAuthor(cartItem.getAuthor());
                    lineItem.setImage(cartItem.getImage());
                    lineItem.setQuantity(cartItem.getQuantity());
                    lineItem.setPrice(cartItem.getPrice());
                    lineItems.add(lineItem);
                });
        return order;
    }

    private Order getOrderFromOrderForm(OrderForm orderForm) {
        Order order = new Order();
        order.setCustomerId(orderForm.getCustomerId());
        order.setStatus("RECEIVED");
        order.setSubTotal(orderForm.getSubTotal());
        List<LineItem> lineItems = new ArrayList<>();
        order.setLineItems(lineItems);
        orderForm
                .getLineItems()
                .forEach(item -> {
                    LineItem lineItem = new LineItem();
                    lineItem.setId(item.getId());
                    lineItem.setTitle(item.getTitle());
                    lineItem.setDescription(item.getDescription());
                    lineItem.setAuthor(item.getAuthor());
                    lineItem.setImage(item.getImage());
                    lineItem.setQuantity(item.getQuantity());
                    lineItem.setPrice(item.getPrice());
                    lineItems.add(lineItem);
                });
        return order;
    }

    private User getUserFromOrderForm(OrderForm orderForm) {
        User user = userRepository
                .findById(orderForm.getCustomerId(), new PartitionKey(orderForm.getCustomerId()))
                .get();
        user.setId(orderForm.getCustomerId());
        Address address = new Address(orderForm.getStreetAddress(), orderForm.getCity(), orderForm.getState(), orderForm.getZip(), "USA");
        user.setAddress(address);
        return user;
    }

}
