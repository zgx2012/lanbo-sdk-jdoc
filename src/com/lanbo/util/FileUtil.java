package com.lanbo.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtil {

	@SuppressWarnings("resource")
	public static boolean createAndWriteToFile(String filename,
			StringBuilder content) {
		File writefile = new File(filename);
		BufferedWriter write;
		try {
			write = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(writefile), "UTF-8"));
			write.write(content.toString());
			write.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	@SuppressWarnings({ "resource" })
	public static boolean createAndWriteToFile(String filename,
			String content) {
		File writefile = new File(filename);
		File filePath = writefile.getParentFile();
		if (filePath != null && !filePath.exists()) {
			filePath.mkdirs();
		}
		
		BufferedWriter write;
		try {
			write = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(writefile), "UTF-8"));
			write.write(content);
			write.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

    // recursively create the directories to the output
    public static void ensureDirectory(File f) {
        File parent = f.getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
    }
}
