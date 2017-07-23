package zy.doc.traverse;

import java.util.List;
import java.util.Queue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import zy.doc.IReplace;
import zy.doc.ReplaceContent4OneP;
import zy.doc.ReplaceContent4TwoP;

/**
 * For every run, get the positions of start Mark and end Mark.
 * 
 * @author yangzhao
 * 
 */
public class InitialTraverseParagraphCommand implements
		ITraverseParagraphCommand {

	private static final Logger logger = LogManager.getLogger(InitialTraverseParagraphCommand.class.getName());
	
	private List<XWPFRun> runs;
	
	private boolean canBreak;

	private boolean canSkip = false;
	
	@Override
	public boolean canSkip() {
		return canSkip;
	}

	@Override
	public boolean canBreak() {
		return canBreak;
	}

	private Queue<IReplace> replaces;

	public InitialTraverseParagraphCommand(Queue<IReplace> replaces) {
		this.replaces = replaces;
	}

	@Override
	public void doCommand() {
		logger.info("Start to do command");
		
		if (runs == null || runs.size() == 0) {
			return;
		}

		this.canSkip = false;
		
		IReplace r = replaces.peek();
		
		if(r == null){
			this.canBreak = true;
		}
		
		if(r instanceof ReplaceContent4OneP){
			logger.info("R is instance of ReplaceContent4OneP");
			
			if(r.willReplace(runs)){ 
			
				replaces.poll();
				
				IReplace next = replaces.peek();
				if(next == null){
					this.canBreak = true;
					return;
				}
				if (next instanceof ReplaceContent4TwoP){
					next.willReplace(runs);
				}
			}
		}else{
			logger.info("R is instance of ReplaceContent4TwoP");
			
			if(((ReplaceContent4TwoP)r).isFirstSet()){
				r.willReplace(runs);
			}else{
				if(r.willReplace(runs)){
					replaces.poll();
					IReplace next = replaces.peek();
					if(next == null){
						this.canBreak = true;
						return;
					}
					if (next instanceof ReplaceContent4OneP){
						if(next.willReplace(runs)){
							replaces.poll();
						}
					}else{
						next.willReplace(runs);
					}
				}
			}

		}
		
		logger.info("End to do command");
	}

	@Override
	public void setRuns(List<XWPFRun> runs) {
		this.runs = runs;
	}

}
