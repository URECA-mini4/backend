package mini.backend;

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

    private AuthenticationRequest authenticationRequest;
    private UserDetails mockUser;

    @BeforeEach
    public void setUp() {
        // 테스트 데이터 설정
        authenticationRequest = new AuthenticationRequest(1L, "testPassword");
        mockUser = User.builder()
                .username("1")
                .password("testPassword")
                .roles("USER")
                .build();
    }

    @Test
    public void testLoginSuccess() throws Exception {

        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(mockUser, null, mockUser.getAuthorities()));

        when(userDetailsService.loadUserById(1L)).thenReturn(mockUser);

        when(jwtUtil.createJwt(anyString(), anyString(), Mockito.anyLong())).thenReturn("fakeToken");

        String jsonRequest = """
            {
                "username": "1",
                "password": "testPassword"
            }
        """;

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk()) // HTTP 200 상태 확인
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

//    @Test
//    public void testLoginFailure() throws Exception {
//        // 자격증명 실패 시, 예외 처리
//        when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
//                .thenThrow(new RuntimeException("Incorrect username or password"));
//
//        String jsonRequest = """
//            {
//                "username": "wrongUser",
//                "password": "wrongPassword"
//            }
//        """;
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isUnauthorized()); // 인증 실패 시 HTTP 401 상태 확인
//    }
}
