package com.vinci.gshoot.controllers;

import com.vinci.gshoot.utils.Base64Coder;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Controller
public class DownloadController {
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    @RequestMapping("/download.do")
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String encodedPath = request.getParameter("doc");
        String filepath = Base64Coder.decodeString(encodedPath);
        InputStream fileInputStream = new FileInputStream(new File(filepath));

        try {
            response.reset();
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment; filename=" + filepath);

            ServletOutputStream out = response.getOutputStream();

            byte[] buf = new byte[DEFAULT_BUFFER_SIZE];
            BufferedInputStream br = new BufferedInputStream(fileInputStream);

            int len;
            while ((len = br.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.flush();
        } finally {
            IOUtils.closeQuietly(fileInputStream);
        }
    }
}
