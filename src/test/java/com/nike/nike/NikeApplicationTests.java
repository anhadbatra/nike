package com.nike.nike;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.nike.nike.controller.AuthController;
import com.nike.nike.model.Role;
import com.nike.nike.model.User;
import com.nike.nike.service.RoleService;
import com.nike.nike.service.UserService;

@SpringBootTest
class NikeApplicationTests {

	@InjectMocks
    private AuthController registrationController;

	 @Mock
    private Model model;
	
    @Mock
    private UserService userService;

    @Mock
    private RoleService roleService;

	@Mock
    private BindingResult bindingResult;

	@Test
	void contextLoads() {
	}
	@Test
	void simpleTest() {
    assertEquals(2, 1 + 1);
}

    @Test
    void testShowRegistrationForm() {
        String viewName = registrationController.showRegistrationForm(model);
        verify(model).addAttribute(eq("user"), any(User.class));
        assertEquals("register", viewName);
    }
	@Test
    void testRegisterUser_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.isUsernameTaken(user.getUsername())).thenReturn(false);
        when(userService.isEmailTaken(user.getEmail())).thenReturn(false);
        when(roleService.findByName("ROLE_USER")).thenReturn(role);

        String viewName = registrationController.registerUser(user, bindingResult, model);

        verify(userService).registerUser(user);
        verify(userService).assignRolesToUser(user, List.of(role.getId()));
        assertEquals("redirect:/login?registered", viewName);
    }


}
