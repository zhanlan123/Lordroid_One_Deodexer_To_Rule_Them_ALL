/*
 * 
 * 
 * Copyright 2016 Rachid Boudjelida <rachidboudjelida@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package deodex.tools;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;

import deodex.S;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

public class ZipTools {

	/**
	 * @returns success
	 * @param odex
	 * @return success
	 *         
	 * @throws IOException
	 */
	public static boolean extractOdex(File odex) throws IOException {
		File Decomdex = new File(odex.getParentFile().getAbsolutePath()
				+ StringUtils.getCropString(odex.getName(), odex.getName().length() - 4));
		if (odex.getName().endsWith(S.ODEX_EXT)) {
			return true;
		} else {
			FileInputStream fin = new FileInputStream(odex);
			BufferedInputStream in = new BufferedInputStream(fin);
			FileOutputStream out = new FileOutputStream(Decomdex);
			XZCompressorInputStream xzIn = new XZCompressorInputStream(in);
			final byte[] buffer = new byte[32768];
			int n = 0;
			while (-1 != (n = xzIn.read(buffer))) {
				out.write(buffer, 0, n);
			}
			out.close();
			xzIn.close();

		}
		return Decomdex.exists();
	}

	/**
	 * search a filename is a zip file
	 * 
	 * @param fileName
	 * @param zipFile
	 * @return returns true is a file with the same name is in the zip file !
	 */
	public static boolean isFileinZip(String fileName, ZipFile zipFile) {
		try {

			// Get the list of file headers from the zip file
			@SuppressWarnings("rawtypes")
			List fileHeaderList = zipFile.getFileHeaders();

			// Loop through the file headers
			for (int i = 0; i < fileHeaderList.size(); i++) {
				FileHeader fileHeader = (FileHeader) fileHeaderList.get(i);
				String name = fileHeader.getFileName();
				if (name.contains("/")) {
					name = name.substring(name.lastIndexOf("/"));
				}
				if (name.equals(fileName)) {
					return true;
				}

			}

		} catch (ZipException e) {
			e.printStackTrace();
		}
		return false;
	}

}