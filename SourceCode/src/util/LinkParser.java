package util;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import parser.AbstractParser;
import parser.DanTriParser;
import parser.YoutubeParser;
import parser.ZingNewsParser;
import pojo.LinkDescriptor;

public class LinkParser {
	private Logger log = LoggerFactory.getLogger(this.getClass());
	private final String URL_ZING_NEWS = "http://(www.)?news.zing.vn/.*";
	private final String URL_DAN_TRI = "http://(www.)?dantri.com.vn/.*";
	private final String URL_YOUTUBE = "http://(www.)?youtu(.)?be(.com)?/.*";
	private AbstractParser parser;

	public LinkDescriptor getInformation(String url) {
		LinkDescriptor ld;
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			// url

			if (url.matches(URL_ZING_NEWS)) {
				parser = new ZingNewsParser(doc, url);
			} else if (url.matches(URL_DAN_TRI)) {
				parser = new DanTriParser(doc, url);
			} else if (url.matches(URL_YOUTUBE)) {
				parser = new YoutubeParser(doc, url);
			} else {
				parser = null;
			}
			if (parser != null) {
				ld = new LinkDescriptor();
				ld.setUrl(url);
				ld.setTitle(parser.getTitle());// title
				ld.setThumbnail(parser.getThumbnail()); // thumbnail
				ld.setDescription(parser.getDescription()); // description
			} else {
				ld = getDefault(url);
			}

		} catch (IOException e) {
			e.printStackTrace();
			log.error("Cannot download page at :" + url);
			ld = getDefault(url);
		}
		return ld;
	}

	private LinkDescriptor getDefault(String url) {
		LinkDescriptor ld = new LinkDescriptor();
		ld.setTitle(url);
		ld.setDescription(null);
		ld.setThumbnail("images/thumbnail/image_not_found.jpg");
		return ld;
	}
}
