package by.clevertec.lobacevich.servlet;

import by.clevertec.lobacevich.dto.UserDto;
import by.clevertec.lobacevich.mapper.UserObjectMapper;
import by.clevertec.lobacevich.mapper.impl.ObjectMapperJson;
import by.clevertec.lobacevich.service.UserService;
import by.clevertec.lobacevich.service.impl.UserServiceImpl;
import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = "/users")
public class UserServlet extends HttpServlet {

    private final UserObjectMapper objectMapper = ObjectMapperJson.getINSTANCE();
    private final UserService userService = UserServiceImpl.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String userId = req.getParameter("id");
        if (userId == null) {
            getAllUsers(resp);
        } else {
            getUserById(resp, userId);
        }
    }

    private void getUserById(HttpServletResponse resp, String userId) throws IOException {
        try {
            UserDto userDto = userService.findUserById(Long.parseLong(userId));
            if (userDto == null) {
                resp.setStatus(404);
                resp.getWriter().write("User not found");
            } else {
                String json = objectMapper.toJson(userDto);
                resp.setStatus(200);
                resp.getWriter().write(json);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            resp.getWriter().write("Incorrect id");
        } finally {
            resp.getWriter().close();
        }
    }

    private void getAllUsers(HttpServletResponse resp) throws IOException {
        String json = objectMapper.listToJson(userService.getAll());
        try (PrintWriter out = resp.getWriter()) {
            out.write(json);
            resp.setStatus(200);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (BufferedReader reader = req.getReader()) {
            String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            UserDto userDto = objectMapper.toUserDto(json);
            UserDto newUserDto = userService.createUser(userDto);
            String newJson = objectMapper.toJson(newUserDto);
            resp.setStatus(201);
            try (PrintWriter out = resp.getWriter()) {
                out.write(newJson);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        try (BufferedReader reader = req.getReader()) {
            String json = reader.lines().collect(Collectors.joining(System.lineSeparator()));
            UserDto userDto = objectMapper.toUserDto(json);
            if (userService.updateUser(userDto)) {
                resp.setStatus(201);
                try (PrintWriter out = resp.getWriter()) {
                    out.write("User was updated");
                }
            } else {
                resp.setStatus(404);
                try (PrintWriter out = resp.getWriter()) {
                    out.write("User not found");
                }
            }
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        String userId = req.getParameter("id");
        if (userId == null ||
                userId.isEmpty() ||
                !StringUtil.isNumeric(userId)) {
            resp.setStatus(400);
            resp.getWriter().write("Incorrect id");
        } else {
            if (userService.deleteUser(Long.parseLong(userId))) {
                resp.setStatus(200);
                resp.getWriter().write("User was deleted successfully");
            } else {
                resp.setStatus(404);
                resp.getWriter().write("User not found");
            }
        }
        resp.getWriter().close();
    }
}
