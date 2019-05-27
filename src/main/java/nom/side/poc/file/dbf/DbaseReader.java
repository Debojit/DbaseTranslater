package nom.side.poc.file.dbf;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.dbf.DBFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ToXMLContentHandler;

public class DbaseReader {
	
	public String dbaseToCsv(InputStream dbaseInputStream) 
	{
		String responseData = null;
		
		try {
			String dbaseContent = this.parseDbf(dbaseInputStream);
			Document dbaseDocument = this.getDomObjectForString(dbaseContent);
			String[] header = this.getHeaders(dbaseDocument);
			List<String[]> records = this.getRecords(dbaseDocument);
			
			StringBuilder dbaseCsvStringBuilder = new StringBuilder(String.join(",", header) + "\n");
			for(String[] record : records) {
				dbaseCsvStringBuilder.append(String.join(",", record) + "\n");
			}
			responseData = dbaseCsvStringBuilder.toString();
		} catch (IOException | SAXException | TikaException | ParserConfigurationException | XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return responseData;
	}
	
	public String dbaseToJson(InputStream dbaseInputStream) {
		String responseData = null;
		try {
			String dbaseContent = this.parseDbf(dbaseInputStream);
			Document dbaseDocument = this.getDomObjectForString(dbaseContent);
			String[] header = this.getHeaders(dbaseDocument);
			List<String[]> records = this.getRecords(dbaseDocument);
			
			StringBuilder dbaseJsonStringBuilder = new StringBuilder("[\n");
			for(String[] record : records) {
				dbaseJsonStringBuilder.append("\t{\n");
				for(int i = 0; i < record.length; i++)
					if(i == record.length - 1) {
						dbaseJsonStringBuilder.append("\t\t\"" + header[i] + "\"")
						  .append(" : ")
						  .append("\"" + record[i] + "\"\n");
					} else {
						dbaseJsonStringBuilder.append("\t\t\"" + header[i] + "\"")
											  .append(" : ")
											  .append("\"" + record[i] + "\",\n");
					}
				if(records.indexOf(record) == records.size() - 1)
					dbaseJsonStringBuilder.append("\t}\n");
				else
					dbaseJsonStringBuilder.append("\t},\n");
			}
			responseData = dbaseJsonStringBuilder.append("]").toString();
		} catch (IOException | SAXException | TikaException | ParserConfigurationException | XPathExpressionException e) {
			e.printStackTrace();
		}
		return responseData;
	}
	
	public String dbaseToXml(InputStream dbaseInputStream) {
		String rootNode = "ROOTELEMENT";
		String recordNode = "RECORD";
		return dbaseToXml(rootNode, recordNode, dbaseInputStream);
	};
	
	public String dbaseToXml(String rootNode, String recordNode, InputStream dbaseInputStream) {
		String responseData = null;
		
		try {
			String dbaseContent = this.parseDbf(dbaseInputStream);
			Document dbaseDocument = this.getDomObjectForString(dbaseContent);
			String[] header = this.getHeaders(dbaseDocument);
			List<String[]> records = this.getRecords(dbaseDocument); 
			
			StringBuilder dbaseXmlStringBuilder = new StringBuilder("<?xml version=\"1.0\" ?>\n<" + rootNode + ">\n");
			for(String[] record : records) {
				dbaseXmlStringBuilder.append("\t<" + recordNode + ">\n");
				for(int i = 0; i < record.length; i++)
					dbaseXmlStringBuilder.append("\t\t<" + header[i] + ">")
										 .append(record[i])
										 .append("</" + header[i] + ">\n");
				dbaseXmlStringBuilder.append("\t</" + recordNode + ">\n");
			}
			responseData = dbaseXmlStringBuilder.append("</" + rootNode + ">").toString();
		} catch (IOException | SAXException | TikaException | ParserConfigurationException | XPathExpressionException e) {
			e.printStackTrace();
		}
		
		return responseData;
	}
	
	private String parseDbf(InputStream dbaseInputStream) throws IOException, SAXException, TikaException {
		Parser parser =  new DBFParser();
		ContentHandler handler = new BodyContentHandler(new ToXMLContentHandler());
		parser.parse(dbaseInputStream, handler, new Metadata(), new ParseContext());
		
		if(dbaseInputStream != null) {
			try {
				dbaseInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return handler.toString();
	}
	
	private Document getDomObjectForString(String xmlString) throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		Document table = builder.parse(new InputSource(new StringReader(xmlString.trim().replaceAll("\t", ""))));
		return table;
	}
	
	private String[] getHeaders(Document dbaseDocument) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList tableHeader = (NodeList)xPath.evaluate("//table/thead/th", dbaseDocument, XPathConstants.NODESET);
		
		String [] headers = new String[tableHeader.getLength()];
		for(int i = 0; i < tableHeader.getLength(); i++) {
			headers[i] = tableHeader.item(i).getTextContent();
		}
		
		return headers;
	}
	
	private List<String[]> getRecords(Document dbaseDocument) throws XPathExpressionException {
		XPath xPath = XPathFactory.newInstance().newXPath();
		NodeList tableRecords = (NodeList)xPath.evaluate("//table/tbody/tr", dbaseDocument, XPathConstants.NODESET);
		
		List<String[]> records = new ArrayList<String[]>(tableRecords.getLength());
		
		for(int i = 0; i < tableRecords.getLength(); i++) {
			NodeList recordNodes = tableRecords.item(i).getChildNodes();
			String[] record = new String[recordNodes.getLength()];
			for(int j = 0; j < recordNodes.getLength(); j++)
				record[j] = recordNodes.item(j).getTextContent();
			records.add(record);
		}
		return records;
	}
}
