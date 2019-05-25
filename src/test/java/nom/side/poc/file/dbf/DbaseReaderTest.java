package nom.side.poc.file.dbf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DbaseReaderTest {
	private static String dbaseFileName = "CUSTOMS.dbf";
	private URL dbaseFileUrl = ClassLoader.getSystemResource(dbaseFileName);
	
	private static InputStream dbaseFileInputStream;
	private static OutputStream targetFileOutputStream;
	
	private static TemporaryFolder tmpDir = new TemporaryFolder();
	
	@BeforeClass
	public static void setup() throws IOException {
		tmpDir.create();
	}
	
	@AfterClass
	public static void tearDown() throws IOException {
		tmpDir.delete();
	}
	
	@Before
	public void initStreams() throws IOException, URISyntaxException {
		File dbaseFile = new File(dbaseFileUrl.toURI());
		dbaseFileInputStream = new FileInputStream(dbaseFile);
	}
	
	@After
	public void resetStreams() throws IOException {
		dbaseFileInputStream.close();
		targetFileOutputStream.flush();
		targetFileOutputStream.close();
	}
	
	@Test
	public void dbaseToXml() throws IOException, URISyntaxException {
		String fileName = dbaseFileName.substring(0, dbaseFileName.lastIndexOf("."));
		
		DbaseReader reader = new DbaseReader();
		String content = reader.dbaseToXml(fileName, fileName + "_Record", dbaseFileInputStream);
		
		Assert.assertNotNull(content);
		
		File xmlFileName = tmpDir.newFile(fileName + ".xml");
		targetFileOutputStream = new FileOutputStream(xmlFileName);
		targetFileOutputStream.write(content.getBytes());
	}
	
	@Test
	public void dbaseToJson() throws IOException {
		String fileName = dbaseFileName.substring(0, dbaseFileName.lastIndexOf("."));
		
		DbaseReader reader = new DbaseReader();
		String content = reader.dbaseToJson(dbaseFileInputStream);
		
		Assert.assertNotNull(content);
		File fileNameJson = tmpDir.newFile(fileName + ".json");
		targetFileOutputStream = new FileOutputStream(fileNameJson);
		targetFileOutputStream.write(content.getBytes());
	}
	
	@Test
	public void dbaseToCsv() throws IOException {
		String fileName = dbaseFileName.substring(0, dbaseFileName.lastIndexOf("."));
		
		DbaseReader reader = new DbaseReader();
		String content = reader.dbaseToCsv(dbaseFileInputStream);
		
		Assert.assertNotNull(content);
		File fileNameCsv = tmpDir.newFile(fileName + ".csv");
		targetFileOutputStream = new FileOutputStream(fileNameCsv);
		targetFileOutputStream.write(content.getBytes());
	}
}