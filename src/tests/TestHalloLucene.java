package tests;

import org.junit.Test;

public class TestHalloLucene {

	@Test
	public void testBuild() throws Exception {
		final String root = "E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\";
		final controler.LuceneRetriever hl = new controler.LuceneRetriever();
		final String [] argvs  = {"HalloLucene", "build", root+"data\\test"};
		hl.excute(argvs);
	}
	
	@Test
	public void testSearch() throws Exception {
		final controler.LuceneRetriever hl = new controler.LuceneRetriever();
		final String[] argv = {"HalloLucene", "search", "Earthquake", "3"};
		hl.excute(argv);
	}
	
	@Test
	public void testExit() throws Exception {
		final controler.LuceneRetriever hl = new controler.LuceneRetriever();
		final String[] argv = {"HalloLucene", "exit"};
		hl.excute(argv);
	}
/*	
	@Test
	public void testXmlDoc() throws JDOMException, IOException, SAXException, ParserConfigurationException {
		File f = new File("E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\data\\queries.xml");
		xmlParsing.XmlDoc xd = new xmlParsing.XmlDoc(f);
		List<String> res = xd.getElementsByTagName("subtopic");
		Iterator<String> iter = res.iterator();
		int i = 0;
		while(iter.hasNext()) {
			System.out.println(i++);
			System.out.println(iter.next());
		}
	}
	
	@Test
	public void testXmlDocWithAttr() throws JDOMException, IOException, SAXException, ParserConfigurationException {
		File f = new File("E:\\Users\\Assassin\\workspace\\temporal_information_retrieval\\data\\queries.xml");
		xmlParsing.XmlDoc xd = new xmlParsing.XmlDoc(f);
		List<String> res = xd.getElementsByTagNameAndAttribute("subtopic", "type", "atemporal");
		Iterator<String> iter = res.iterator();
		int i = 0;
		while(iter.hasNext()) {
			System.out.println(i++);
			System.out.println(iter.next());
		}
	}
	*/
}
