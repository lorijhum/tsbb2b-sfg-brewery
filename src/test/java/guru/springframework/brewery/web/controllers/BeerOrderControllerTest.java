package guru.springframework.brewery.web.controllers;

import guru.springframework.brewery.domain.Customer;
import guru.springframework.brewery.repositories.CustomerRepository;
import guru.springframework.brewery.services.BeerOrderService;
import guru.springframework.brewery.web.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BeerOrderController.class)
class BeerOrderControllerTest {

    @MockBean
    BeerOrderService beerOrderService;

    @Autowired
    MockMvc mockMvc;

    BeerOrderDto beerOrder;

    BeerOrderLineDto beerOrderLine;
    OrderStatusEnum orderStatus;
    BeerOrderPagedList beerOrderPagedList;
    BeerDto validBeer;


    Customer customer;

    CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {
        beerOrderLine = BeerOrderLineDto.builder().id(UUID.randomUUID())
                .version(1)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .beerId(UUID.randomUUID())
                .orderQuantity(1)
                .build();


        beerOrder = BeerOrderDto.builder()
                .id(UUID.randomUUID())
                .version(1)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .customerId(UUID.randomUUID())
                .beerOrderLines(new ArrayList<>())
                .orderStatus(orderStatus)
                .orderStatusCallbackUrl("/api/callback")
                .customerRef("John Doe")
                .build();

        validBeer = BeerDto.builder().id(UUID.randomUUID())
                .version(1)
                .beerName("Beer1")
                .beerStyle(BeerStyleEnum.PALE_ALE)
                .price(new BigDecimal("12.99"))
                .quantityOnHand(4)
                .upc(123456789012L)
                .createdDate(OffsetDateTime.now())
                .lastModifiedDate(OffsetDateTime.now())
                .build();


    }

    @AfterEach
    void tearDown() {
        reset(beerOrderService);
    }

    @Test
    void listOrders() throws Exception {
        given(beerOrderService.listOrders(any(), any())).willReturn(beerOrderPagedList);

        mockMvc.perform(get("/api/v1/customers/85d4506-e7dd-446e-a092-5f30b98e7b26/orders"))
                .andExpect(status().isOk());

    }

    @Test
    void getOrder() throws Exception {
        given(beerOrderService.getOrderById(any(), any())).willReturn(beerOrder);

        System.out.println(beerOrder.toString());

        mockMvc.perform(get("/api/v1/customers/85d4506-e7dd-446e-a092-5f30b98e7b26/orders/f25767d9-342a-48ac-a788-0a7a38ae6fb3"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));
    }


}