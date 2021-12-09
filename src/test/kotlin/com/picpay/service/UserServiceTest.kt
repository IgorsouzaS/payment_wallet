package com.picpay.service

import com.picpay.repository.UserRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
@AutoConfigureMockMvc
class UserServiceTest (@Autowired val userService: UserService){

    @MockBean
    lateinit var userRepository: UserRepository

    @Nested
    inner class UserCreation {


    }

    @Nested
    inner class UserRetrieve {


    }

    @Nested
    inner class UserOperationsRetrieve {


    }
}