package com.example.notice.common.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class CommonUtil {

	/**
	 * 저장 경로 폴더 확인 및 생성
	 * @param saveDir
	 */
	public static File mkdir(String saveDir){
		File dir = new File(saveDir);
		try {
			if (!dir.exists() && !dir.isFile()) {
				FileUtils.forceMkdir(dir);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		return dir;
	}
}
