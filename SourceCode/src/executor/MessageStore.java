package executor;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.MessageDAO;


import pojo.Message;
import util.MyLogger;

public class MessageStore implements Runnable{
	private ArrayList<Message> messageHistory;
	private MessageDAO messageDAO;
	private Logger log;
	public MessageStore(ArrayList<Message> messageHistory, MessageDAO messageDAO) {
		this.messageHistory = messageHistory;
		this.messageDAO = messageDAO;
		this.log = MyLogger.log;
	}

	@Override
	public void run() {
		synchronized (messageHistory) {
			if (messageHistory.size() != 0) {
				if (messageDAO.storeHistory(messageHistory)) {
					//System.out.println("Store success");
					log.info("Store success");
					messageHistory.clear();
				} else {
					//System.out.println("Store fail");
					log.info("Store fail");
				}
			}
		}
		
	}
	
}
