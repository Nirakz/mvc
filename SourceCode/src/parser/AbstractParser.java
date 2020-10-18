package parser;


import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import util.MyLogger;

public abstract class AbstractParser {
	protected Logger log ;
	protected Document doc;
	protected String url;

	public AbstractParser(Document doc, String url) {
		this.log = MyLogger.log;
		this.doc = doc;
		this.url = url;
	}

	public abstract String getTitle();

	public abstract String getThumbnail();

	public abstract String getDescription();
}
