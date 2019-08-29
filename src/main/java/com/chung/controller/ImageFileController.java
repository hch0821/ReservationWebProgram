package com.chung.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.chung.service.IImageFileService;

@Controller
public class ImageFileController {
	// /reserv/image?path=PATH
	@GetMapping(path = "/image")
	public void getImage(HttpServletResponse response, @RequestParam(name = "path", required = true) String path) {
		File file = new File(IImageFileService.ROOT_DIRECTORY, path);
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

	private void writeFile(HttpServletResponse response, File file) 
	{
		FileInputStream fis = null;
		OutputStream out = null;
		try {
			fis = new FileInputStream(file);
			out = response.getOutputStream();
			int readCount = 0;
			byte[] buffer = new byte[1024];
			while ((readCount = fis.read(buffer)) != -1) {
				out.write(buffer, 0, readCount);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			response.setStatus(404);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}

		}
	}
}
