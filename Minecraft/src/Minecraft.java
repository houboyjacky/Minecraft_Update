import java.awt.EventQueue;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.io.FileUtils;

import javax.swing.JMenuBar;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JTabbedPane;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;

public class Minecraft extends JFrame {

	private static final long serialVersionUID = 1L;
	private String OSBit ="32位元";
	private static String[] Mods = new String[]{"NEI系列","更多動作模組","動態光線模組","小地圖(LiteMod)", "快捷鍵大師(LiteMod)","更多玩家訊息(LiteMod)","GUI美化(LiteMod)","GUI美化之音樂包(LiteMod)"};
	private JTabbedPane tabbedPane;
	private DefaultListModel<String> Setup_unchoosedlist;
	private DefaultListModel<String> Setup_choosedlist;
	private DefaultListModel<String> Update_unchoosedlist;
	private DefaultListModel<String> Update_choosedlist;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Minecraft frame = new Minecraft();
					frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					frame.setBounds(100, 100, 640, 480);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public boolean loadURL(String propertiesUrl) {
		try {
			URL url = new URL(propertiesUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			new BufferedReader(new InputStreamReader(con.getInputStream()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String getProfileString(String file, String section, String variable, String defaultValue)
			throws IOException {
		String strLine, valueString = "";
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		try {
			while ((strLine = bufferedReader.readLine()) != null) {
				// 讀取INI檔到記憶體

				strLine = strLine.trim();
				Pattern p;
				Matcher m, m2;
				p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");
				Pattern p2 = Pattern.compile("\\[.*\\]");
				m = p.matcher((strLine));
				m2 = p2.matcher((strLine));
				if (m2.find()) {
					if (m.find()) {
						isInSection = true;
					} else {
						isInSection = false;
					}
				}
				if (isInSection == true) {
					strLine = strLine.trim();
					String[] strArray = strLine.split("=");
					if (strArray.length == 2) {
						valueString = strArray[0].trim();
						if (valueString.equalsIgnoreCase(variable)) {
							valueString = strLine.substring(strLine.indexOf("=") + 1).trim();
							return valueString;
						}
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception

		} finally {

			bufferedReader.close();
		}
		return defaultValue;
	}
	
	public static boolean setProfileString(String file, String section, String variable, String value)
			throws IOException {
		/**
		 * 修改ini配置文檔中變數的值
		 * 
		 * @param file
		 *            配置文檔的路徑
		 * @param section
		 *            要修改的變數所在段名稱
		 * @param variable
		 *            要修改的變數名稱
		 * @param value
		 *            變數的新值
		 * @throws IOException
		 *             拋出文檔操作可能出現的io異常
		 */
		String RN = "\r\n";
		String fileContent, allLine;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
		boolean isInSection = false;
		fileContent = "";
		try {
			while ((allLine = bufferedReader.readLine()) != null) {
				allLine = allLine.trim();
				Pattern p;
				Matcher m, m2;
				p = Pattern.compile("\\[\\s*" + section + "\\s*\\]");
				Pattern p2 = Pattern.compile("\\[.*\\]");
				m = p.matcher((allLine));
				m2 = p2.matcher((allLine));
				if (m2.find()) {
					if (m.find()) {
						isInSection = true;
					} else {
						isInSection = false;
					}
				}
				if (isInSection == true) {
					String[] strArray = allLine.split("=");
					if (allLine.indexOf("=") > 0) {
						String valueString = strArray[0].trim();
						if (valueString.equalsIgnoreCase(variable)) {
							String newLine = valueString + "=" + value;
							fileContent += "" + newLine + RN;
						} else {
							fileContent += "" + allLine + RN;
						}
					} else {
						fileContent += allLine + RN;
					}
				} else {
					if (allLine.indexOf("=") > 0) {
						fileContent += "" + allLine + RN;
					} else {
						fileContent += allLine + RN;
					}
				}
			}
			// System.out.println(fileContent);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file, false));
			bufferedWriter.write(fileContent);
			bufferedWriter.flush();
			bufferedWriter.close();
			return true;
		} catch (Exception e) {

			e.printStackTrace();
		} finally {
			bufferedReader.close();
		}
		return false;
	}
	
	public void Prepare(){
		Setup_unchoosedlist = new DefaultListModel<String>();
		Setup_choosedlist = new DefaultListModel<String>();
		Update_unchoosedlist = new DefaultListModel<String>();
		Update_choosedlist = new DefaultListModel<String>();
		for(int i = 0; i < Mods.length; i++){
			Setup_unchoosedlist.addElement(Mods[i]);
			Update_unchoosedlist.addElement(Mods[i]);
		}	
	}
	
 	public void MenuBar(){		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu action = new JMenu("\u57F7\u884C");
		menuBar.add(action);

		JMenuItem menuItem = new JMenuItem("\u96E2\u958B");
		action.add(menuItem);

		JMenu Exp = new JMenu("\u8AAA\u660E");
		menuBar.add(Exp);

		JMenuItem Explanation = new JMenuItem("\u8AAA\u660E");
		Exp.add(Explanation);

		JMenuItem Thanks = new JMenuItem("\u81F4\u8B1D");
		Exp.add(Thanks);
		
		menuItem.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				System.exit(0);
			}
		});
		Explanation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JOptionPane.showMessageDialog(Exp, "版權所有，有任何問題可以來信寄到\n" + "houboyjacky@gmail.com\n"
						+ "Copyright © 2015 by JackyHou" + " All rights reserved", "說明",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		Thanks.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				JOptionPane.showMessageDialog(Exp,
						"感謝各大網站的Java說明與支援" + "\n也感謝所有支持Green Minecraft的朋友們" + "\n管理員群與我會繼續努力下去" + "\n感恩", "致謝",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
	}

	public void InformationPage(){
		JPanel SystemInf = new JPanel();
		tabbedPane.addTab("\u7CFB\u7D71\u8CC7\u8A0A", null, SystemInf, null);

		if(System.getProperty("os.arch").toString().equals("amd64")){
			OSBit = "64位元";
		}
		if(System.getProperty("os.arch").toString().equals("x86")){
			JOptionPane.showMessageDialog(null, "提醒你，你的作業系統位元是32位元，可能會因為作業系統限制，而無法執行，建議重灌為64位元版本");
		}
		if(Float.valueOf(Runtime.getRuntime().totalMemory()) / (1000.0 * 1000.0 * 16) < 4 ){
			JOptionPane.showMessageDialog(null, "提醒你，你的記憶體是小於4G，可能會因為記憶體限制，而無法執行，建議升級記憶體或者關閉其他應用程式提供Minecraft執行");
		}
		if(System.getProperty("sun.arch.data.model").toString().equals(32+"")){
			JOptionPane.showMessageDialog(null, "提醒你，你的Java位元數是32位元，可能會因為位元限制，而無法順利執行，請更新成64位元版本");
		}		
				
		JLabel OS = new JLabel("\u4F5C\u696D\u7CFB\u7D71");
		SystemInf.add(OS);
		
		JLabel A_OS = new JLabel(System.getProperty("os.name").toString() + "  "+OSBit);
		SystemInf.add(A_OS);

		JLabel CPU = new JLabel("CPU核心數");
		SystemInf.add(CPU);
		JLabel A_CPU = new JLabel(String.valueOf(Runtime.getRuntime().availableProcessors()) + " 個核心");
		SystemInf.add(A_CPU);

		JLabel Ram = new JLabel("\u8A18\u61B6\u9AD4");
		SystemInf.add(Ram);
		JLabel A_Ram = new JLabel(String.valueOf((int)Math.floor(Float.valueOf(Runtime.getRuntime().totalMemory()) / (1000.0 * 1000.0 * 16))) + "G");
		SystemInf.add(A_Ram);

		JLabel Remain = new JLabel("\u5269\u9918\u8A18\u61B6\u9AD4");
		SystemInf.add(Remain);
		JLabel A_Remain = new JLabel(String.valueOf((int)Math.floor(Float.valueOf(Runtime.getRuntime().freeMemory()) / (1000.0 * 1000.0 * 16))) + "G");
		SystemInf.add(A_Remain);

		JLabel Version = new JLabel("Java\u7248\u672C");
		SystemInf.add(Version);
		JLabel A_Version = new JLabel(System.getProperty("java.version")+"  "+System.getProperty("sun.arch.data.model")+"位元版本");
		SystemInf.add(A_Version);
		
		JLabel Status = new JLabel("\u904A\u6232\u4F3A\u670D\u5668\u72C0\u614B");
		SystemInf.add(Status);

		JLabel A_Ststus = new JLabel("\u6E2C\u8A66\u4E2D...");
		SystemInf.add(A_Ststus);
		
		if (loadURL("http://134.208.3.130")) {
			A_Ststus.setText("正常運作中...");
		} else {
			A_Ststus.setText("已失去連線...");
		}
		
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		SystemInf.setLayout(new GridLayout(0, 2, 0, 0));
	}
	
	public void SetupPage(){
		JPanel Setup = new JPanel();
		tabbedPane.addTab("Minecraft\u5B89\u88DD\u7A0B\u5E8F", null, Setup, null);
		Setup.setLayout(new MigLayout("", "[73px][grow]", "[23px][][][][][grow][]"));

		JLabel Setup_LabelName = new JLabel("\u5B89\u88DD\u9078\u9805");
		Setup.add(Setup_LabelName, "cell 0 0 2 1,alignx center");

		JLabel Setup_SetupAddressLabel = new JLabel("\u5B89\u88DD\u8DEF\u5F91");
		
		Setup.add(Setup_SetupAddressLabel, "cell 0 1,alignx center");

		JTextField Setup_MinecraftAddress = new JTextField();
		Setup.add(Setup_MinecraftAddress, "flowx,cell 1 1,growx");
		
		JLabel Setup_DownloadPackageAddress = new JLabel("\u4E0B\u8F09\u5305\u4F4D\u7F6E");
		Setup.add(Setup_DownloadPackageAddress, "cell 0 2,alignx center");

		JFileChooser fc1 = new JFileChooser();
		JFileChooser fc2 = new JFileChooser();

		JTextField Setup_DownloadAddress = new JTextField();
		Setup.add(Setup_DownloadAddress, "flowx,cell 1 2,growx");
		
		JLabel Setup_Name7ZIP = new JLabel("7zip\u7A0B\u5F0F\u4F4D\u7F6E");
		Setup.add(Setup_Name7ZIP, "cell 0 3,alignx center");

		JTextField Setup_Path_7zip = new JTextField();
		Setup.add(Setup_Path_7zip, "flowx,cell 1 3,growx");
		
		JLabel Setup_Otherlabel = new JLabel("\u5176\u4ED6");
		Setup.add(Setup_Otherlabel, "cell 0 4,alignx center");

		JCheckBox Setup_ChangeRightVersion = new JCheckBox("\u66F4\u63DB\u70BA\u6B63\u7248\u555F\u52D5\u5668");
		Setup.add(Setup_ChangeRightVersion, "flowx,cell 1 4");

		JCheckBox Setup_CustomMod = new JCheckBox("\u9078\u7528\u6A21\u7D44");

		Setup.add(Setup_CustomMod, "cell 0 5,alignx center");

		JPanel Steup_ChooseModGUI = new JPanel();
		Setup.add(Steup_ChooseModGUI, "cell 1 5,grow");
		Steup_ChooseModGUI.setLayout(new GridLayout(1, 0, 0, 0));

		JList Setup_UnChoosedMod = new JList(Setup_unchoosedlist);
		Setup_UnChoosedMod.setEnabled(false);
		Steup_ChooseModGUI.add(Setup_UnChoosedMod);

		JPanel Setup_ButtonMid = new JPanel();
		Steup_ChooseModGUI.add(Setup_ButtonMid);
		Setup_ButtonMid.setLayout(null);

		JButton Setup_ChooseButton = new JButton(">");

		Setup_ChooseButton.setEnabled(false);
		Setup_ChooseButton.setBounds(44, 54, 87, 23);
		Setup_ButtonMid.add(Setup_ChooseButton);

		JButton Setup_UnChooseButton = new JButton("<");

		Setup_UnChooseButton.setEnabled(false);
		Setup_UnChooseButton.setBounds(44, 131, 87, 23);
		Setup_ButtonMid.add(Setup_UnChooseButton);

		JList Setup_Choosed = new JList(Setup_choosedlist);
		Setup_Choosed.setEnabled(false);
		Steup_ChooseModGUI.add(Setup_Choosed);

		JLabel Setup_Status = new JLabel("");
		Setup.add(Setup_Status, "flowx,cell 1 6,alignx right");

		JLabel Setup_StatusLabel = new JLabel("目前狀況：");
		Setup.add(Setup_StatusLabel, "cell 1 6,alignx left");

		JLabel Setup_StatusMessage = new JLabel("等待執行中...");
		Setup.add(Setup_StatusMessage, "cell 1 6,growx");

		JProgressBar Setup_ProgressBar = new JProgressBar();
		Setup.add(Setup_ProgressBar, "cell 1 6,alignx right");

		JCheckBox Setup_ALLResetup = new JCheckBox("\u91CD\u65B0\u5B89\u88DD");
		Setup.add(Setup_ALLResetup, "cell 1 6,alignx right");

		JButton Setup_ChooseAddressSetup = new JButton("\u81EA\u8A02");
	
		Setup_ChooseAddressSetup.setToolTipText("\u81EA\u8A02\u5B89\u88DD\u8DEF\u5F91");
		Setup.add(Setup_ChooseAddressSetup, "cell 1 1");

		JButton Setup_ChooseDownloadPackage = new JButton("\u9078\u64C7");
		Setup.add(Setup_ChooseDownloadPackage, "cell 1 2");

		JButton Setup_Start = new JButton("\u958B\u59CB\u5B89\u88DD");
		Setup.add(Setup_Start, "cell 1 6,alignx right");

		JCheckBox Setup_NoDownloadPackage = new JCheckBox("\u672A\u4E0B\u8F09\u5B89\u88DD\u5305");
		Setup.add(Setup_NoDownloadPackage, "cell 1 2");

		JLabel Setup_Detecting7zip = new JLabel("\u5075\u6E2C\u4E2D...");
		Setup.add(Setup_Detecting7zip, "cell 1 3,growx");

		Setup_SetupAddressLabel.setToolTipText("\u9810\u8A2D\u8DEF\u5F91\uFF0C\u53EF\u4EE5\u9078\u64C7\u81EA\u8A02\u6309\u9215\u91CD\u8A2D");
		
		Setup_MinecraftAddress.setEditable(false);
		Setup_MinecraftAddress.setText(System.getenv("APPDATA") + "/.minecraft/");
		Setup_MinecraftAddress.setColumns(10);
				
		fc1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc2.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		Setup_ALLResetup.setToolTipText("\u6CE8\u610F\uFF1A\u6B64\u9078\u9805\u5B8C\u5168\u522A\u9664\u91CD\u4F86\u5594!!!!");
		
		Setup_DownloadAddress.setEditable(false);
		Setup_DownloadAddress.setText(System.getProperty("user.home").toString() + "/Downloads/Minecraft 安裝包(請用7z解壓縮).7z");
		Setup_DownloadAddress.setColumns(10);
		
		Setup_Path_7zip.setEditable(false);
		Setup_Path_7zip.setColumns(10);
		
		Setup_ChooseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Setup_choosedlist.addElement(Setup_unchoosedlist.getElementAt(Setup_UnChoosedMod.getSelectedIndex()).toString());
				Setup_unchoosedlist.remove(Setup_UnChoosedMod.getSelectedIndex());
			}
		});

		Setup_UnChooseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Setup_unchoosedlist.addElement(Setup_choosedlist.getElementAt(Setup_Choosed.getSelectedIndex()).toString());
				Setup_choosedlist.remove(Setup_Choosed.getSelectedIndex());
			}
		});

		Setup_CustomMod.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (Setup_CustomMod.isSelected() == true) {
					Steup_ChooseModGUI.setEnabled(true);
					Setup_UnChoosedMod.setEnabled(true);
					Setup_ChooseButton.setEnabled(true);
					Setup_UnChooseButton.setEnabled(true);
					Setup_Choosed.setEnabled(true);
				} else {
					Steup_ChooseModGUI.setEnabled(false);
					Setup_UnChoosedMod.setEnabled(false);
					Setup_ChooseButton.setEnabled(false);
					Setup_UnChooseButton.setEnabled(false);
					Setup_Choosed.setEnabled(false);
				}
			}
		});
		
		Setup_ChooseAddressSetup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc1.showOpenDialog(Minecraft.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc1.getSelectedFile();
					Setup_MinecraftAddress.setText(file.getPath().toString());
				}
			}
		});
		
		Setup_ChooseDownloadPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int returnVal = fc2.showOpenDialog(Minecraft.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc2.getSelectedFile();
					Setup_DownloadAddress.setText(file.getPath().toString());
				}
			}
		});
		
		File existFiles = new File(System.getenv("ProgramFiles") + "/7-Zip/7z.exe");
		if (!existFiles.exists()) {
			Setup_Path_7zip.setText("Not Install");
			Setup_Path_7zip.setEnabled(false);

			Setup_Detecting7zip.setText("將會自動下載與安裝!!!");
		} else {
			Setup_Path_7zip.setText(System.getenv("ProgramFiles") + "/7-Zip/7z.exe");
			Setup_Detecting7zip.setText("您已經安裝完畢了!!!");
		}
		
		Setup_Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ex) {
				//安裝位置
				Setup_MinecraftAddress.getText();
				//7zip安裝
				if(Setup_Path_7zip.getText().equals("Not Install")){
					String strUrl="";
					if(System.getProperty("os.arch").toString().equals("amd64")){
						strUrl = "http://www.7-zip.org/a/7z920-x64.msi";
					}else{
						strUrl = "http://www.7-zip.org/a/7z920.msi";
					}					
					try {
						URL source = new URL(strUrl);
						String theStrDestDir = System.getProperty("user.home").toString()+"/temp/"; //你要下載的檔案目的資料夾
						File theStockDest = new File(theStrDestDir);
						FileUtils.forceMkdir(theStockDest);
						
						File destination = new File(theStrDestDir + "7zip.msi");
						FileUtils.copyURLToFile(source, destination);
						 
					} catch (MalformedURLException e) {
						// TODO 自動產生的 catch 區塊
						e.printStackTrace();
					} catch (IOException e) {
						// TODO 自動產生的 catch 區塊
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	public void Update(){
		
	}
	
	public Minecraft() {
		setTitle("Green Minecraft Setup Launcher");
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
					
		Prepare();
		MenuBar();
		InformationPage();
		SetupPage();
		Update();
		
		JPanel Update = new JPanel();
		tabbedPane.addTab("Minecraft\u66F4\u65B0\u7A0B\u5E8F", null, Update, null);
		Update.setLayout(new MigLayout("", "[grow]", "[][][grow][]"));

		JLabel label_2 = new JLabel("\u66F4\u65B0\u9078\u9805");
		Update.add(label_2, "cell 0 0,alignx center");

		JLabel lblmod = new JLabel("\u66F4\u65B0Mod\u4F4D\u7F6E");
		Update.add(lblmod, "flowx,cell 0 1");

		JPanel Update_page = new JPanel();
		Update.add(Update_page, "cell 0 2,grow");
		Update_page.setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBounds(7, 7, 313, 281);
		Update_page.add(panel);
		panel.setLayout(new MigLayout("", "[grow][]", "[][][grow]"));
		
		JLabel label_4 = new JLabel("保留模組選擇");
		panel.add(label_4, "flowx,cell 0 0");
		
		JCheckBox checkBox_1 = new JCheckBox("額外模組");
		panel.add(checkBox_1, "cell 0 1");
		
		JButton btnNewButton = new JButton("選擇");
		panel.add(btnNewButton, "cell 0 0");
		
		JScrollPane ExtraMod = new JScrollPane();
		panel.add(ExtraMod, "cell 0 2 2 1,grow");
		
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(369, 7, 218, 281);
		Update_page.add(panel_1);

		JCheckBox checkBox_3 = new JCheckBox("\u91CD\u65B0\u5B89\u88DD\u6A21\u7D44");
		Update.add(checkBox_3, "flowx,cell 0 3,alignx right");

		JButton StartUpdate = new JButton("\u958B\u59CB\u66F4\u65B0");
		Update.add(StartUpdate, "cell 0 3");

		JTextField Mod_path = new JTextField();
		Update.add(Mod_path, "cell 0 1,growx");
		Mod_path.setColumns(10);

		JButton button_3 = new JButton("\u81EA\u8A02");
		Update.add(button_3, "cell 0 1,alignx center");
		tabbedPane.setEnabledAt(2, true);
		
	}
}
