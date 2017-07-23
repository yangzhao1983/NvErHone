package zy.UI.Logic;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.openxml4j.opc.OPCPackage;

import zy.UI.EditCommonFrame;
import zy.UI.ImportWord;
import zy.doc.CustomXWPFDocument;
import zy.dso.DocObject;


public class ImportWordLogic extends CommonLogic {

	private ImportWord frame;
	private String path;

	private static final Logger logger = LogManager.getLogger(ImportWordLogic.class.getName());
	
	public void setPath(String path) {
		this.path = path;
	}

	public ImportWordLogic(ImportWord frame){
		this.frame = frame;
	}
	
	@Override
	public void navigate2Next() {

		EditCommonFrame ecFrame = new EditCommonFrame(frame, frame.getObject());
		ecFrame.init();
		
		ecFrame.pack();
		
		ecFrame.setLocationRelativeTo(null);
        
		ecFrame.setVisible(true);
		frame.setVisible(false);

	}

	@Override
	public void navigate2Before() {
		super.navigate2Before();
	}

	// Background task for loading images.
	SwingWorker<DocObject, Void> worker = new SwingWorker<DocObject, Void>() {
		@Override
		public DocObject doInBackground(){
			
			CustomXWPFDocument doc = null;
			try {
				OPCPackage pack = POIXMLDocument
						.openPackage(path);
				doc = new CustomXWPFDocument(pack);
				
			} catch (IOException e) {
				e.printStackTrace();
//				throw new LogicException(this.getClass()+".doInBackground:" + e.getClass(), e); 
			}

			if(doc == null){
				return null;
			}else{
				return doc.getDocObject();
			}
		}

		@Override
		public void done() {
//			frame.getBtnNext().setEnabled(true);
//			frame.getBtnNext().revalidate();
		}
	};

	@Override
	public void setUIInfo() {
		frame.getBtnNext().setEnabled(false);
		frame.getBtnNext().revalidate();
		
		worker.execute();
		try {
			frame.setObject(worker.get());
			frame.getObject().setPath(path);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
			logger.equals(" can not setUIInfo InterruptedException");
			e.printStackTrace();
		} catch (ExecutionException e) {
			logger.error(e.getMessage());
			logger.equals(" can not setUIInfo IOException");
			e.printStackTrace();
		}
	}
	
}
