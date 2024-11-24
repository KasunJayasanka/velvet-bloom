package com.store.velvetbloom;

import com.store.velvetbloom.controller.AuthControllerTest;
import com.store.velvetbloom.controller.CartControllerTest;
import com.store.velvetbloom.controller.CategoryControllerTest;
import com.store.velvetbloom.controller.CustomerControllerTest;
import com.store.velvetbloom.controller.ProductControllerTest;
import com.store.velvetbloom.controller.OrderController;
import com.store.velvetbloom.service.*;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@SelectClasses({
		// Controller Tests
		AuthControllerTest.class,
		CartControllerTest.class,
		CategoryControllerTest.class,
		CustomerControllerTest.class,
		ProductControllerTest.class,
		OrderController.class,

		// Service Tests
		AuthServiceTest.class,
		CartServiceTest.class,
		CategoryServiceTest.class,
		CustomerServiceTest.class,
		ProductServiceTest.class,
		OrderServiceTest.class,
})
@SpringBootTest
public class VelvetbloomApplicationTests {
}
