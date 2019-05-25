package nom.side.poc.file.dbf;

import java.io.IOException;

import javax.activation.DataHandler;

import org.junit.Assert;
import org.junit.Test;

import nom.side.poc.file.dbf.common.model.DbaseTranslaterRequest;
import nom.side.poc.file.dbf.common.model.DbaseTranslaterRequest.DataFormat;
import nom.side.poc.file.dbf.ws.DbaseTranslaterService;
import nom.side.poc.file.dbf.ws.DbaseTranslaterServiceImpl;

public class DbaseTranslaterServiceTest {

	@Test
	public void testReadDbaseContent() throws IOException {
		DbaseTranslaterService service = new DbaseTranslaterServiceImpl();
		
		DataHandler handler = new DataHandler(new Object(), "application/octet-stream");
		
		DbaseTranslaterRequest request = new DbaseTranslaterRequest(DataFormat.XML, handler);
		
		service.readDbaseContent(request);
		
		Assert.assertTrue(true);
	}

}
