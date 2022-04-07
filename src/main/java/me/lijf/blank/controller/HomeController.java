package me.lijf.blank.controller;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Enumeration;

@RestController
@RequestMapping("/")
public class HomeController {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    private String AUTH_METHOD="Basic ";
    private Logger logger= LoggerFactory.getLogger(HomeController.class);

    @RequestMapping("/echo")
    public Object echo(HttpServletRequest request){
        try {
            return success(wrap(request));
        }catch (Exception e){
            return fail(e);
        }
    }

    private Result success(Object data){
        return new Result("200",data);
    }

    private Result fail(Exception e){
        return new Result("500",e.getLocalizedMessage());
    }

    @RequestMapping("/route0/echo")
    public Object stub(HttpServletRequest request){
        return success("worked!");
    }

    @RequestMapping("/health")
    public Object health(){
        return success("hello,world.");
    }


    private String wrap(HttpServletRequest request) throws IOException {
        StringBuilder sb=new StringBuilder();
        Enumeration<String> headerNames=request.getHeaderNames();
        logger.info("=======HEADERS=========");
        while(headerNames.hasMoreElements()){
            String headerName= headerNames.nextElement();
            sb.append(headerName);
            sb.append(":");
            sb.append(request.getHeader(headerName));
            sb.append("\n");
            logger.info("{}:{}",headerName,request.getHeader(headerName));
        }
        logger.info("=======PARAMETERS=========");
        Enumeration<String> paramsNames=request.getParameterNames();
        while(paramsNames.hasMoreElements()){
            String paramsName=paramsNames.nextElement();
            sb.append(paramsName);
            sb.append(":");
            sb.append(request.getParameter(paramsName));
            sb.append("\n");
            logger.info("{}:{}",paramsName,request.getParameter(paramsName));
        }
        logger.info("=======BODY=========");
        BufferedReader br = request.getReader();
        StringBuffer body=new StringBuffer();
        String line;
        while((line=br.readLine())!=null){
            body.append(line);
        }
        br.close();
        JSONObject bodyParams=JSONObject.parseObject(body.toString());
        bodyParams.entrySet().stream().forEach(entry->{
            logger.info("{}:{}",entry.getKey(),entry.getValue());
        });
        sb.append(body);

/*
        String encrypted=request.getHeader("authorization").substring(AUTH_METHOD.length());
        String text= new String(Base64.getDecoder().decode(encrypted));
        logger.info("{}<====>{}",encrypted,text);
*/
        if(!"67795E98229F004A42D43D5CE62D007E".equals(request.getHeader("token")))
            throw new RuntimeException("请通过网关调用。");
        return sb.toString();
    }

    @RequestMapping("/cache")
    public Result cache(){
        redisTemplate.opsForValue().set("xOpr","Lijf");
        return success(redisTemplate.opsForValue().get("xOpr"));
    }
}
