package com.picpay.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.picpay.model.response.User
import com.picpay.service.UserService
import org.bson.types.ObjectId
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.internal.matchers.Any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
class UserControllerTest(
    @Autowired private val mockMvc: MockMvc
){
    private val mapper = ObjectMapper()

    @MockBean
    lateinit var userService: UserService

    @Nested
    inner class UserCreation {
        @Test
        fun `When create user then user created with expected email`() {
            /*
            val user = mockDefaultUser()
            Mockito.`when`(userService.createUser(user)).thenReturn(user)
            mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsString(user))
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isCreated)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("email").value(user.email))
            */
        }
    }


    @Nested
    inner class UserRetrieve {

        @Test
        fun `when retrieve user while user is exists then user returns with expected id`() {
            /*
            val user = mockDefaultUser()
            Mockito.`when`(userService.getUser(user.id)).thenReturn(user)
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .accept(MediaType.APPLICATION_JSON)
            )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(user.id))
            */
        }

        @Test
        fun `When retrieve user while user does not exists then user not found`() {
            /*
            Mockito.`when`(userService.getUser(ObjectId(Any.ANY.toString()))).thenReturn(null)
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
            */
        }



        @Test
        fun `When retrieve transfers while user does not exists then transfers not found`() {
            /*
            Mockito.`when`(userService.getTransfers(ObjectId(Any.ANY.toString()))).thenReturn(null)
            mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound)
            */
        }
    }

    /*private fun mockDefaultUser() = User(

    )*/
}
