package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Document;

import org.jsoup.select.Elements;

public class YoutubeParser extends AbstractParser {
	public YoutubeParser(Document doc, String url) {
		super(doc, url);
	}

	@Override
	public String getTitle() {
		String title = null;
		try {

			Elements titles = doc.select("span#eow-title");
			title = titles.first().text();

		} catch (Exception e) {
			log.error(e.toString());
			log.info("Cannot parse title of " + url);
			title = url;
		}
		return title;
	}

	@Override
	public String getThumbnail() {
		String thumbnailSrc = null;
		try {
			String regExp = "/?.*(?:youtu.be\\/|v\\/|u/\\w/|embed\\/|watch\\?.*&?v=)";
			Pattern compiledPattern = Pattern.compile(regExp);
			Matcher matcher = compiledPattern.matcher(url);
			if (matcher.find()) {
				int start = matcher.end();
				thumbnailSrc = "http://i2.ytimg.com/vi/"
						+ url.substring(start, start + 11) + "/default.jpg";
			}
		} catch (Exception e) {
			log.error(e.toString());
			log.info("Cannot parse thumbnail of " + url);
		}
	
		return thumbnailSrc;
	}

	@Override
	public String getDescription() {
		return null;
	}

}
