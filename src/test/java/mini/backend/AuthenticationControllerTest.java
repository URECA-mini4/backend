package mini.backend;

import mini.backend.auth.AuthenticationFacade;
import mini.backend.auth.AuthenticationRequest;
import mini.backend.auth.JwtUtil;
import mini.backend.user.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private MyUserDetailsService userDetailsService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private AuthenticationFacade authenticationFacade;

    private AuthenticationRequest authenticationRequest;
    private UserDetails mockUser;

    @BeforeEach
    public void setUp() {
        authenticationRequest = new AuthenticationRequest("testUser", "testPassword");
        mockUser = User.builder()
                .username("testUser")
                .password("testPassword")
                .roles("USER")
                .build();
    }

    @Test
    public void testLoginSuccess() throws Exception {
        // Mock login process
        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities()));

        when(userDetailsService.loadUserByUsername("testUser")).thenReturn(mockUser);

        when(jwtUtil.createJwt(anyString(), anyString(), Mockito.anyLong())).thenReturn("fakeToken");

        String loginRequest = """
            {
                "username": "testUser",
                "password": "testPassword"
            }
        """;

        // Perform login and get accessToken
        String loginResponse = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Extract the accessToken from the response
        String accessToken = extractTokenFromResponse(loginResponse, "accessToken");

        // Mock getAuthentication to return userId
        Long mockUserId = 1L;
        when(authenticationFacade.getAuthentication()).thenReturn(mockUserId);

        // Log the value of getAuthentication
        System.out.println("Mocked userId from getAuthentication: " + authenticationFacade.getAuthentication());

    }

    private String extractTokenFromResponse(String response, String tokenField) {
        try {
            org.json.JSONObject jsonResponse = new org.json.JSONObject(response);
            return jsonResponse.getString(tokenField);
        } catch (org.json.JSONException e) {
            throw new RuntimeException("Error parsing JSON response", e);
        }
    }
}
