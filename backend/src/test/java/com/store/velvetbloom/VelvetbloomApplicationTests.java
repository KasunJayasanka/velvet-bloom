package com.store.velvetbloom;


import com.store.velvetbloom.controller.AuthControllerTest;
import com.store.velvetbloom.controller.CategoryControllerTest;
import com.store.velvetbloom.controller.CustomerControllerTest;
import com.store.velvetbloom.controller.ProductControllerTest;
import com.store.velvetbloom.service.AuthServiceTest;
import com.store.velvetbloom.service.CartServiceTest;
import com.store.velvetbloom.service.CategoryServiceTest;
import com.store.velvetbloom.service.CustomerServiceTest;
import com.store.velvetbloom.service.ProductServiceTest;
import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.springframework.boot.test.context.SpringBootTest;

@Suite
@SelectClasses({
		// Controller Tests
		AuthControllerTest.class,
		CategoryControllerTest.class,
		CustomerControllerTest.class,
		ProductControllerTest.class,

		// Service Tests
		AuthServiceTest.class,
		CartServiceTest.class,
		CategoryServiceTest.class,
		CustomerServiceTest.class,
		ProductServiceTest.class
})

@SpringBootTest
public class VelvetbloomApplicationTests {
	
}
