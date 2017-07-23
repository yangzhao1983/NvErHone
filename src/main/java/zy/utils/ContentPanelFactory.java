package zy.utils;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zy.UI.AbstractContentPanel;
import zy.UI.AnalytisisItemsPanel;
import zy.UI.BasicContentPanel;
import zy.UI.ContentPanel;
import zy.UI.EvidenceContentPanel;
import zy.UI.IdentifyContentPanel;
import zy.UI.SignerContentPanel;
import zy.UI.SummaryContentPanel;
import zy.UI.SurveyNoteContentPanel;


public class ContentPanelFactory {

	private static List<ContentPanel> panels = new ArrayList<ContentPanel>();

	private static final Logger logger = LogManager.getLogger(ContentPanelFactory.class.getName());
	
	public static int getBeforeIndex(int index){
		return index-1;
	}
	
	public static int getNextIndex(int index){
		return index+1;
	}
	
	public static ContentPanel getContentPanel(int index, JFrame frame){
		ContentPanel panel = null;
		try {
			panel = panels.get(index);
		} catch (IndexOutOfBoundsException e) {
			logger.error("Cannot get content panel IndexOutOfBoundsException");
			logger.error(e.getMessage());
			System.out.println("The content panel has not been created!");
		}
		
		if(panel != null){
			
			return panel;
		}
		
		switch (index){
			case UIConsts.NAME_CONTENT_PANEL_BASIC: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_BASIC);
				panel = new BasicContentPanel();
				panels.add(index, panel);break;
			}

			case UIConsts.NAME_CONTENT_PANEL_ABSTRACT: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_ABSTRACT);
				panel = new AbstractContentPanel();
				panels.add(index, panel);break;
			}
			
			// ���̸ſ�
			case UIConsts.NAME_CONTENT_PANEL_SUMMARY: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_SUMMARY);
				panel = new SummaryContentPanel(frame);
				panels.add(index, panel);break;
			}
			
			// �������	
			case UIConsts.NAME_CONTENT_PANEL_SURVEY: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_SURVEY);
				panel = new SurveyNoteContentPanel(frame);
				panels.add(index, panel);break;
			}
			
			case UIConsts.NAME_CONTENT_PANEL_ANALYSIS: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_ANALYSIS);
				panel = new AnalytisisItemsPanel(frame);
				panels.add(index, panel);break;
			}
			
			// ��������
			case UIConsts.NAME_CONTENT_PANEL_EVIDENCE: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_EVIDENCE);
				panel = new EvidenceContentPanel();
//				panel = new EvidenceItemsPanel();
				panels.add(index, panel);break;
			}
			
			// �������
			case UIConsts.NAME_CONTENT_PANEL_IDENTIFY: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_IDENTIFY);
				panel = new IdentifyContentPanel();
				panels.add(index, panel);break;
			}
			
			case UIConsts.NAME_CONTENT_PANEL_SIGNER: {
				logger.info("Generate =============== " + UIConsts.NAME_CONTENT_PANEL_SIGNER);
				panel = new SignerContentPanel();
				panels.add(index, panel);break;
			}
		}
		
		return panel;
	}
}
