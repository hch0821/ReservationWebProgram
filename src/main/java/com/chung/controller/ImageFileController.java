package com.chung.controller;

import static com.chung.config.WebMvcContextConfiguration.ROOTPATH;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

//외부 경로에 있는 이미지 파일을 스트림으로 읽어들여 클라이언트에게 뿌려주는 컨트롤러
// /img/, /img_map/, /commentImages/에 있는 이미지 파일들 해당.
@Controller
public class ImageFileController {

	
	// /reserv/image?path=PATH
	@GetMapping(path = "/image")
	public void getImage(HttpServletResponse response, @RequestParam(name = "path", required = true) String path) {
		File file = new File(ROOTPATH, path);
		if (!file.exists()) {
			response.setStatus(404);
			return;
		}

		String contentType = URLConnection.guessContentTypeFromName(file.getAbsolutePath());

		long fileLength = file.length();
		response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\";");
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Type", contentType);
		response.setHeader("Content-Length", "" + fileLength);
		response.setHeader("Pragma", "no-cache;");
		response.setHeader("Expires", "-1;");

		writeFile(response, file);
	}

	private void writeFile(HttpServletResponse response, File file) {
	
		try (	FileInputStream fis = new FileInputStream(file);
				OutputStream out = response.getOutputStream();
			){
			int readCount = 0;
			byte[] buffer = new byte[1024];
			while ((readCount = fis.read(buffer)) != -1) {
				out.write(buffer, 0, readCount);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatus(404);
		}

		
	}
}
