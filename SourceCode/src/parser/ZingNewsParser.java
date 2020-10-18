package parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ZingNewsParser extends AbstractParser {
	private final String[] titleSelector = { "header > h1" };
	private final String[] descriptionSelector = { "div.summary" };
	private final String[] thumbnailSelector = { "img.oImage",
			" td.pic > img", "div.content > p > img" };

	public ZingNewsParser(Document doc, String url) {
		super(doc, url);
	}

	@Override
	public String getTitle() {
		String title = null;
		try {
			Elements titles;
			for (int i = 0; i < titleSelector.length; i++) {
				titles = doc.select(titleSelector[i]);
				if (titles != null) {
					title = titles.first().text();
					break;
				}
			}

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
			thumbnailSrc = "images/thumbnail/logo_zing_news.jpg";
		}
		return thumbnailSrc;
	}

	@Override
	public String getDescription() {
		String description = null;
		try {

			Elements descriptions;
			for (int i = 0; i < descriptionSelector.length; i++) {
				descriptions = doc.select(descriptionSelector[i]);
				if (descriptions != null) {
					description = descriptions.first().text();
					break;
				}
			}

		} catch (Exception e) {
			log.error(e.toString());
			log.info("Cannot parse description of " + url);
		}
		return description;
	}

}
