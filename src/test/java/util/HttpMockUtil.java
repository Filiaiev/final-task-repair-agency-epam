package util;

import org.mockito.stubbing.Answer;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class HttpMockUtil {

    private static HttpSession session;
    private static HttpServletRequest request;
    private static HttpServletResponse response = mock(HttpServletResponse.class);
    private static Map<String, Object> attributesMap;

    private HttpMockUtil(){};

    public static void mockHttp(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        ServletContext context = mock(ServletContext.class);
        when(request.getServletContext()).thenReturn(context);
        when(request.getSession()).thenReturn(session);

        doAnswer((Answer<Object>) invocationOnMock -> {
            String key = invocationOnMock.getArgument(0);
            Object value = invocationOnMock.getArgument(1);
            attributesMap.put(key, value);
            return null;
        }).when(session).setAttribute(anyString(), any());
    }

    public static HttpSession getSession() {
        return session;
    }

    public static HttpServletRequest getRequest() {
        return request;
    }

    public static HttpServletResponse getResponse() {
        return response;
    }

    public static Map<String, Object> getAttributesMap() {
        return attributesMap;
    }

    public static void resetAttributesMap(){
        attributesMap = new HashMap<>();
    }
}
