package executor;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pojo.LinkDescriptor;

import util.LinkParser;


public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//PropertyConfigurator.configure("./WebContent/WEB-INF/log4j.properties");
		Logger log = LoggerFactory.getLogger(test.class);
		
		String url ;
		url = "http://news.zing.vn/the-gioi/lam-dau-xu-han-ke-tu-tu-nguoi-len-tien/a340993.html#home_noibat2";
	//url = "http://dantri.com.vn/su-kien/arsenal-duoc-va-mat-gi-sau-chuyen-di-cua-running-man-toi-emirates-763642.htm";
	//	url =  "http://dantri.com.vn/xa-hoi/luat-su-mach-nuoc-nguoi-dan-xu-ly-cay-xang-gian-lan-763903.htm";
		//url="http://dantri.com.vn/xa-hoi/trinh-thu-tuong-quyet-so-phan-xe-khong-chinh-chu-763950.htm";
	//	url ="http://dantri.com.vn/xa-hoi/cac-dia-phuong-doc-suc-doi-pho-bao-so-6-dang-tien-vao-dat-lien-764062.htm";
		//url ="http://news.zing.vn/Canh-sat-banh-chung-tung-gay-sot-noi-ve-Ba-Tung-post343058.html#f=category/top/noi-bat/type:featured+";
		//url = "http://news.zing.vn/Co-gai-Gia-Lai-xinh-dep-hut-60000-like-tren-mang-post341172.html#f=detail/sidebar/topview";	          
		//url="http://news.zing.vn/Dich-vu-mo-bien-so-day-o-to-quanh-Keangnam-post343021.html#f=/top/noi-bat/type:featured+picture";
		LinkParser parser = new LinkParser();
		LinkDescriptor ld = parser.getInformation(url);
		
		log.info("ld: {} ", ld.getTitle());
		log.info("ld: {} ", ld.getThumbnail());
		log.info("ld: {} ", ld.getDescription());
		
	}

}
