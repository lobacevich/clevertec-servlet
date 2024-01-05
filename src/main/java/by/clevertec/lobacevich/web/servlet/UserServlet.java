package by.clevertec.lobacevich.web.servlet;

import by.clevertec.lobacevich.config.SpringConfig;
import by.clevertec.lobacevich.dto.UserDto;
import by.clevertec.lobacevich.mapper.UserObjectMapper;
import by.clevertec.lobacevich.mapper.impl.ObjectMapperJson;
import by.clevertec.lobacevich.pdf.PdfGenerator;
import by.clevertec.lobacevich.pdf.impl.UserPdfGenerator;
import by.clevertec.lobacevich.service.UserService;
import by.clevertec.lobacevich.service.impl.UserServiceImpl;
import com.itextpdf.styledxmlparser.jsoup.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

@WebServlet(name = "userServlet", urlPatterns = "/users")
@Configurable
public class UserServlet extends HttpServlet {

    @Autowired
    private UserObjectMapper objectMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        objectMapper = context.getBean("objectMapperJson", ObjectMapperJson.class);
        userService = context.getBean("userServiceImpl", UserServiceImpl.class);
        pdfGenerator = context.getBean("userPdfGenerator", UserPdfGenerator.class);
        context.close();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = req.getParameter("id");
        String pageSize = req.getParameter("pagesize");
        String pageNumber = req.getParameter("page");
        if (userId == null) {
            getAllUsers(resp, pageSize, pageNumber);
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
                pdfGenerator.createPdf(userDto);
            }
        } catch (NumberFormatException e) {
            resp.setStatus(400);
            resp.getWriter().write("Incorrect id");
        } finally {
            resp.getWriter().close();
        }
    }

    private void getAllUsers(HttpServletResponse resp, String pageSizeStr, String pageNumberStr) throws IOException {
        int pageSize = checkIntParam(pageSizeStr) ? Integer.parseInt(pageSizeStr) : 20;
        int pageNumber = checkIntParam(pageNumberStr) ? Integer.parseInt(pageNumberStr) : 1;
        String json = objectMapper.listToJson(userService.getAll(pageNumber, pageSize));
        try (PrintWriter out = resp.getWriter()) {
            out.write(json);
            resp.setStatus(200);
        }
    }

    private static boolean checkIntParam(String param) {
        return param != null &&
                !param.isEmpty() &&
                StringUtil.isNumeric(param) &&
                Integer.parseInt(param) > 0;
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
