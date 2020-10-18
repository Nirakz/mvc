package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DanTriParser extends AbstractParser {
	private final String[] thumbnailSelector = {
			"div.fon34.mt3.mr2.fon43  > div[align=center] > img",
			"div.fon34.mt3.mr2.fon43  > div[align=center] > span > img",
			"div.fon34.mt3.mr2.fon43  > div[style=TEXT-ALIGN: center]  > div > img",
			"img[width~=4\\d\\d]" };
	
	public DanTriParser(Document doc, String url) {
		super(doc, url);
	}

	@Override
	public String getTitle() {
		String title = null;
		try {
			Elements titles = doc.select(".fon31.mt2");
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
			
			Elements images;
			Element thumbnail;
			for (int i = 0; i < thumbnailSelector.length; i++) {
				images = doc.select(thumbnailSelector[i]);
				thumbnail = images.first();
				if (thumbnail != null) {
					thumbnailSrc = thumbnail.attr("src");
					break;
				}
			}

		} catch (Exception e) {
			log.error(e.toString());
			log.info("Cannot parse thumbnail of " + url);
		}
		if (thumbnailSrc == null) {
			thumbnailSrc = "images/thumbnail/logo_dantri.png";
		}
		return thumbnailSrc;
	}

	@Override
	public String getDescription() {
		String description = null;
		try {
			Elements descriptions = doc.select(".fon33.mt1");
			description = validateString(descriptions.first().text());
		} catch (Exception e) {
			log.error(e.toString());
			log.info("Cannot parse description of " + url);
		}
		return description;
	}

	private String validateString(String str) {
		int index = str.indexOf(">>");// dantri.com.vn description
		if (index != -1) {
			str = str.substring(0, index);
		}
		return str;
	}

}
