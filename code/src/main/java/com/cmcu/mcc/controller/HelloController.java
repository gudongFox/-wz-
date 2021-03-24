package com.cmcu.mcc.controller;

import com.deepoove.poi.XWPFTemplate;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

@RestController
public class HelloController {
    @RequestMapping("/hello")
    public void hello(HttpServletResponse response) throws IOException {
        String classpath = this.getClass().getResource("/").getPath().replaceFirst("/", "");
        String webappRoot = classpath.replaceAll("WEB-INF/classes/", "");
        String filePath =webappRoot+"assets/doc/房建-设计与开发任务书.docx";

        XWPFTemplate xwpfTemplate = XWPFTemplate.compile(filePath).render(new HashMap<String, Object>(){{
            put("projectName", "Poi-tl 模板引擎");
        }});

        sendResponse(response, "template.docx", xwpfTemplate);


    }

    private void sendResponse(HttpServletResponse response, String fileName, XWPFTemplate xwpfTemplate) throws IOException{
        response.setContentType("application/csv");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
        xwpfTemplate.write(response.getOutputStream());
        xwpfTemplate.close();
    }
}