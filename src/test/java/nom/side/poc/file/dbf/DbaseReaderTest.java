package nom.side.poc.file.dbf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class DbaseReaderTest {
	private String testFile = "C:\\u99\\data\\CUSTOMS.dbf";
	
	@Test
	public void dbaseToXml() throws IOException {
		File dbaseFile = new File(testFile);
		FileInputStream fis = new FileInputStream(dbaseFile);
		
		String fileName = dbaseFile.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		
		DbaseReader reader = new DbaseReader();
		String content = reader.dbaseToXml(fileName, fileName + "_Record", fis);
		fis.close();
		
		Assert.assertNotNull(content);
		
		File fileNameXml = new File(dbaseFile.getParent() + File.separator + fileName + ".xml");
		FileOutputStream fos = new FileOutputStream(fileNameXml);
		fos.write(content.getBytes());
		fos.close();
	}
	
	@Test
	public void dbaseToJson() throws IOException {
		File dbaseFile = new File(testFile);
		FileInputStream fis = new FileInputStream(dbaseFile);
		
		String fileName = dbaseFile.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		
		DbaseReader reader = new DbaseReader();
		String content = reader.dbaseToJson(fis);
		fis.close();
		
		Assert.assertNotNull(content);
		File fileNameJson = new File(dbaseFile.getParent() + File.separator + fileName + ".json");
		FileOutputStream fos = new FileOutputStream(fileNameJson);
		fos.write(content.getBytes());
		fos.close();
	}
	
	@Test
	public void dbaseToCsv() throws IOException {
		File dbaseFile = new File(testFile);
		FileInputStream fis = new FileInputStream(dbaseFile);
		
		String fileName = dbaseFile.getName();
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		
		DbaseReader reader = new DbaseReader();
		String content = reader.dbaseToCsv(fis);
		fis.close();
		
		Assert.assertNotNull(content);
		File fileNameJson = new File(dbaseFile.getParent() + File.separator + fileName + ".csv");
		FileOutputStream fos = new FileOutputStream(fileNameJson);
		fos.write(content.getBytes());
		fos.close();
	}
}
