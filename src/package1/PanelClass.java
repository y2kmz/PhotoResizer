package package1;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class PanelClass {
	public PanelClass panel;
//	public boolean isRunFlag = true;
	
	public String sourceDir = null;
	public String targetDir = null;
	public int width = 1024;
	public String exname = "small";
	public float quality = 0.85f;
	
	private FindFolder task = new FindFolder(this);
	
	private final Display display = Display.getDefault();
	private final Shell shell = new Shell();
	private Composite composite = new Composite(shell, SWT.NONE);
	private Group group = new Group(composite, SWT.NONE);
	private final DirectoryDialog dlg = new DirectoryDialog(shell);
	private final Label sourceLabel = new Label(group, SWT.NONE);
	private final Label sourcePath = new Label(group, SWT.NONE);
	public final Button button1 = new Button(group, SWT.NONE);
	private final Label targetLabel = new Label(group, SWT.NONE);
	private final Label targetPath = new Label(group, SWT.NONE);
	public final Button button2 = new Button(group, SWT.NONE);
	private final Label widthLabel = new Label(group, SWT.NONE);
	public final Text text = new Text(group, SWT.BORDER);
	private final Label pxLabel = new Label(group, SWT.NONE);
	private final Label exnameLabel = new Label(group, SWT.NONE);
	public final Text exnameText = new Text(group, SWT.BORDER);
	private final Label qualityLabel = new Label(group, SWT.NONE);
	private final Label persentLabel = new Label(group, SWT.NONE);
	public final Scale scale = new Scale(group, SWT.NONE);
	public final ProgressBar progressBar = new ProgressBar(composite, SWT.NONE);
	public final Text logText = new Text(composite, SWT.MULTI|SWT.BORDER|SWT.V_SCROLL);
	public final Label countLabel = new Label(composite, SWT.NONE);
	public final Button button3 = new Button(composite, SWT.NONE);
	public final Button button4 = new Button(composite, SWT.NONE);

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PanelClass tt = new PanelClass();
		tt.runPanel();
	}
	/**
	 * @param args
	 */
	public void runPanel() {
		panel = this;
		shell.setSize(600, 410);
		shell.setText("照片批量压缩器");
		
		//=========================================
		
		composite.setBounds(0, 0, 600, 410);
		
		group.setText("输入选项");
		group.setBounds(10, 10, 570, 125);
		
		dlg.setText("dir");
		dlg.setMessage("pls select a dir");
		dlg.setFilterPath("C:/");
		
		sourceLabel.setBounds(10, 20, 50, 15);
		sourceLabel.setText("照  片：");
		
		sourcePath.setBounds(120, 20, 445, 15);
		sourcePath.setText("");
		
		button1.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				sourceDir = dlg.open();
				if(sourceDir!=null){
					sourcePath.setText(sourceDir);
				}
			}
		});
		button1.setBounds(60, 15, 50, 20);
		button1.setText("选择");
	
		targetLabel.setBounds(10, 45, 50, 15);
		targetLabel.setText("存放到：");
		
		targetPath.setBounds(120, 45, 445, 15);
		targetPath.setText("");
		targetPath.setAlignment(3);
		
		button2.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				targetDir = dlg.open();
				if(targetDir!=null){
					targetPath.setText(targetDir);
				}
			}
		});
		button2.setBounds(60, 40, 50, 20);
		button2.setText("选择");
		
		widthLabel.setBounds(10, 75, 85, 15);
		widthLabel.setText("设定长边长度：");
		
		text.setText("1024");
		text.setBounds(100, 72, 35, 18);
		
		pxLabel.setBounds(137, 75, 15, 15);
		pxLabel.setText("px");
		
		exnameLabel.setBounds(10, 100, 85, 15);
		exnameLabel.setText("文件附加名：");
		
		exnameText.setText("small");
		exnameText.setBounds(100, 97, 70, 18);
		
		qualityLabel.setBounds(190, 85, 85, 15);
		qualityLabel.setText("图片压缩质量：");

		persentLabel.setBounds(540, 85, 25, 15);
		persentLabel.setText("85%");
		
		scale.setMinimum(10);
		scale.setMaximum(100);
		scale.setIncrement(5);
		scale.setPageIncrement(5);
		scale.setSelection(85);
		scale.setBounds(275, 73, 260, 32);
		scale.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
				persentLabel.setText(scale.getSelection()+"%");
			}
		});
		
		progressBar.setMinimum(0);
		progressBar.setMaximum(100);
		progressBar.setBounds(10, 145, 570, 20);
		
		logText.setBounds(10, 175, 570, 155);
		
		countLabel.setBounds(20, 345, 100, 25);
		countLabel.setText("0/0");
		
		button3.setBounds(420, 340, 70, 25);
		button3.setText("开始");
		button3.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				if(null==sourceDir||"".equals(sourceDir)){
					MessageDialog.openError(null, "Error", "请选择照片的文件夹");
					return;
				}
				if(null==targetDir||"".equals(targetDir)){
					MessageDialog.openError(null, "Error", "请选择处理后储存的位置");
					return;
				}
				try{
					width = Integer.parseInt(text.getText());
				}catch(Exception e3){
					MessageDialog.openError(null, "Error", "图片宽度设置错误，请输入数字");
					return;
				}
				exname = exnameText.getText();
				quality = scale.getSelection()/100;
				new Thread(){
					public void run(){
						task.findFolderProcess();
					}
				}.start();
			}
		});
		
		button4.setBounds(510, 340, 70, 25);
		button4.setText("停止");
		button4.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e){
				task.stopTask();
				progressBar.setSelection(0);
				countLabel.setText("0/0");
				logText.insert("转换进程已终止！\n");
			}
		});
		button4.setEnabled(false);
		
		//=========================================
		shell.layout();
		shell.open();
		while(!shell.isDisposed()){
			if(!display.readAndDispatch()){
				display.sleep();
			}
		}
	}

}
