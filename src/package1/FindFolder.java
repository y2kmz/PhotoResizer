package package1;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.swt.widgets.Display;
public class FindFolder {
	
	private PanelClass panel;
	private boolean stopFlag;
	
	public FindFolder(PanelClass panelObj){
		this.panel = panelObj;
	}
	
	public void stopTask(){
		stopFlag = true;
		setPanelState(true);
	}
	    
    /**
     * 
     * @param panel
     */
    public void findFolderProcess() {
    	setPanelState(false);
    	stopFlag = false;
    	long a = System.currentTimeMillis();
    	int fcounter = 0;
    	int counter = 0;
    	List<String> folderList = new ArrayList<String>();
    	List<String> fileList = new ArrayList<String>();
    	
    	LinkedList<File> list = new LinkedList<File>();
    	File dir = new File(panel.sourceDir);
    	File file[] = dir.listFiles();
    	for (int i = 0; i < file.length; i++) {
    		if (file[i].isDirectory()){
    			list.add(file[i]);
    		}
    		else{
    			if(file[i].getAbsolutePath().endsWith(".JPG")||
    					file[i].getAbsolutePath().endsWith(".JPEG")||
    					file[i].getAbsolutePath().endsWith(".jpeg")||
    					file[i].getAbsolutePath().endsWith(".jpg")||
    					file[i].getAbsolutePath().endsWith(".bmp")||
    					file[i].getAbsolutePath().endsWith(".BMP")||
    					file[i].getAbsolutePath().endsWith(".gif")||
    					file[i].getAbsolutePath().endsWith(".GIF")){
	    			fileList.add(file[i].getAbsolutePath());
	    			counter++;    
    			}
    		}
    	}
    	File tmp;
    	while (!list.isEmpty()) {
    		tmp = list.removeFirst();
    		if (tmp.isDirectory()) {
    			file = tmp.listFiles();
    			fcounter++;
    			folderList.add(tmp.getAbsolutePath());
    			if (file == null)
    				continue;
    			for (int i = 0; i < file.length; i++) {
    				if (file[i].isDirectory()){
    					list.add(file[i]);   
    				}
    				else{
    					if(file[i].getAbsolutePath().endsWith(".JPG")||
    	    					file[i].getAbsolutePath().endsWith(".JPEG")||
    	    					file[i].getAbsolutePath().endsWith(".jpeg")||
    	    					file[i].getAbsolutePath().endsWith(".jpg")||
    	    					file[i].getAbsolutePath().endsWith(".bmp")||
    	    					file[i].getAbsolutePath().endsWith(".BMP")||
    	    					file[i].getAbsolutePath().endsWith(".gif")||
    	    					file[i].getAbsolutePath().endsWith(".GIF")){
	    					fileList.add(file[i].getAbsolutePath());
	    					counter++;
    					}
    				}
    			}
    		} else {
    			if(tmp.getAbsolutePath().endsWith(".JPG")||
    					tmp.getAbsolutePath().endsWith(".JPEG")||
    					tmp.getAbsolutePath().endsWith(".jpeg")||
    					tmp.getAbsolutePath().endsWith(".jpg")||
    					tmp.getAbsolutePath().endsWith(".bmp")||
    					tmp.getAbsolutePath().endsWith(".BMP")||
    					tmp.getAbsolutePath().endsWith(".gif")||
    					tmp.getAbsolutePath().endsWith(".GIF")){
    			fileList.add(tmp.getAbsolutePath());
    			counter++;
    			}
    		}
    	}
//    	System.out.println("folder : "+fcounter);
//    	System.out.println("files  : "+counter);
//    	printCollection(folderList);
    	for(String folderPath : folderList){
    		if(stopFlag){
    			return;
    		}
    		String newFolder = folderPath.replace(panel.sourceDir, panel.targetDir);
			File dirFile = new File(newFolder);
			if(dirFile.exists() == false){
				insertLogText("创建目录"+newFolder+"\n");
				dirFile.mkdir();
			}
    	}
    	
    	insertLogText("开始转换"+panel.sourceDir+"中的图片\n");
    	insertLogText("文件夹中共有"+counter+"个图片\n");
    	GraphicCompressUtil graphicUtil = new GraphicCompressUtil();
    	int index = 1;
    	for (String path : fileList) {
    		if(stopFlag){
    			return;
    		}
    		insertLogText(path+"\n");
    		setCountText(index+"/"+counter);
    		setProgress((index*100)/counter);
    		graphicUtil.proce(path, panel.sourceDir, panel.targetDir,
    				panel.width, panel.quality, panel.exname, 
    				GraphicCompressUtil.HIGH_QUALITY);
    		index++;
    	}
//    	System.out.println(System.currentTimeMillis() - a);
    	long times = System.currentTimeMillis() - a;
    	insertLogText("转换完成，共用时"+(float)times/1000+"秒\n");
    	setPanelState(true);
    }
    
//    private static void printCollection(Collection<String> co){
//    	for (String o : co) {
//			System.out.println(o);
//		}
//    }
    
    private void setPanelState(final boolean flag){
    	Display.getDefault().asyncExec(new Runnable(){
    		public void run(){
    			panel.button1.setEnabled(flag);
    			panel.button2.setEnabled(flag);
    			panel.button3.setEnabled(flag);
    			panel.button4.setEnabled(!flag);
    			
    			panel.exnameText.setEnabled(flag);
    			panel.text.setEnabled(flag);
    			
    			panel.scale.setEnabled(flag);
    		}
    	});
    }
    
    private void setProgress(final int selection){
    	Display.getDefault().asyncExec(new Runnable(){
    		public void run(){
    			panel.progressBar.setSelection(selection);
    		}
    	});
    }
    
    private void insertLogText(final String text){
    	Display.getDefault().asyncExec(new Runnable(){
    		public void run(){
    			panel.logText.insert(text);
    		}
    	});
    }
    
    private void setCountText(final String text){
    	Display.getDefault().asyncExec(new Runnable(){
    		public void run(){
    			panel.countLabel.setText(text);
    		}
    	});
    }
    
}
