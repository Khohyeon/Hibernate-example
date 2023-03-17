package shop.mtcoding.hiberpc.config.filter;


import javax.servlet.*;
import java.io.IOException;

public class MyBlackListFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 주위 : 버퍼를 비우면, 컨트롤러에서 버퍼를 읽지 못한다.
        // 데이터 타입 : x-www-form-urlencoded
        String value = request.getParameter("value");

        if(value == null){
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("value 파라메타에 전송해 주세요.");
            return;
        }

        if (value.equals("babo")){  // 여기서 "바보"라고 하면 한글 설정이 안되있어서 영어로 사용
            response.setContentType("text/plain; charset=utf-8");
            response.getWriter().println("당신은 블랙리스트가 되었습니다.");
            return;
        }
        chain.doFilter(request, response);
    }
}
