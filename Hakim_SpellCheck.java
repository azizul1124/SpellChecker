// Author Name: Azizul Hakim                            
// Date: 4/14/19 
// Program Name: Hakim_SpellCheck

import java.awt.BorderLayout;          
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Hakim_SpellCheck extends JPanel implements ActionListener 
{

	private static final long serialVersionUID = 1L;
	static private final String newline = "\n";
    JButton btnDictionary, btnSource, btnProcess;
    static JTextArea log;
    JFileChooser fc;
    private static ArrayList<String> dictionaryList = new ArrayList<String>();
	private static ArrayList<String> testStatesList = new ArrayList<String>();


	public static void main(String[] args) throws Exception 
	{
		getConnection();
		
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run() 
            {
                UIManager.put("swing.boldMetal", Boolean.FALSE); 
                createAndShowGUI();
            }
        });

	}
	
	public static Connection getConnection() throws Exception 
	{
		try 
		{
			String driver = "com.mysql.jdbc.Driver";
			String url = "jdbc:mysql://localhost:3306/Spelling";
			String username = "*username*";
			String password = "*password*";
			Class.forName(driver);
			
			Connection conn = DriverManager.getConnection(url,username,password);
			System.out.println("Connected");
			
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from word");

			while (resultSet.next())
			{
			
				String dictionary = resultSet.getString("dictionary");
				String testwords = resultSet.getString("testwords");
				System.out.println( dictionary + ", " + testwords);
				
				if(!dictionary.equals(testwords))
				{
					
					System.out.println("Not a match");
					System.out.println();
				}
			}
			

			conn.close();
			return conn;
			 
		}
		
		
		catch(Exception e)
		{
			System.out.println(e);
		}
		
		return null;
	}
	
	
	public Hakim_SpellCheck()
	{
		super(new BorderLayout());

        log = new JTextArea(5,20);
        log.setMargin(new Insets(5,5,5,5));
        log.setEditable(false);
        JScrollPane logScrollPane = new JScrollPane(log);
        
        fc = new JFileChooser();        
        fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        
        btnDictionary = new JButton("Set Dictionary File...");
        btnDictionary.addActionListener(this);

        btnSource = new JButton("Open Source File...");
        btnSource.addActionListener(this);
        
        btnProcess = new JButton("Spell Check Source...");
        btnProcess.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 3, 0, 0));
        buttonPanel.add(btnDictionary);
        buttonPanel.add(btnSource);
        buttonPanel.add(btnProcess);

        add(buttonPanel, BorderLayout.WEST);
        add(logScrollPane, BorderLayout.CENTER);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{

        if (e.getSource() == btnDictionary)
        {
            int returnVal = fc.showOpenDialog(Hakim_SpellCheck.this);

            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();
                log.append("Opening: " + file.getName() + "." + newline);
                loadFile(file, dictionaryList);
            } 
            else 
            {
                log.append("Set Dictionary command cancelled by user." + newline);
            }
            log.setCaretPosition(log.getDocument().getLength());


        } 
        else if (e.getSource() == btnSource) 
        {
            int returnVal = fc.showSaveDialog(Hakim_SpellCheck.this);
            if (returnVal == JFileChooser.APPROVE_OPTION)
            {
                File file = fc.getSelectedFile();
                log.append("Opening: " + file.getName() + "." + newline);
                loadFile(file, testStatesList);
            } 
            else
                log.append("Load Source file command cancelled by user." + newline);
            
            log.setCaretPosition(log.getDocument().getLength());
        }  
        
        else if (e.getSource() == btnProcess)
        {  	
            log.append("Processing Source file....." +  newline);
            spellCheck();
            log.setCaretPosition(log.getDocument().getLength());
        }
		
	}
	
	
	static void spellCheck()
	{
		for(String w : testStatesList)
		{
			if(!dictionaryList.contains(w))
				log.append("unknown word: "+ w + newline);
		}
		log.append("Processing Source File Complete. " +  newline);
	}
	
	
	private static void loadFile(File file, ArrayList arrayList)
	{
		try
		{
		     Scanner sc = new Scanner(file);
		     
		     while (sc.hasNext()) 
		    	 arrayList.add(sc.next());
		     
		     log.append(file.getName() +" loaded" + newline);
		        
		}
	    catch(FileNotFoundException e) 
		{
	    	log.append("Something went wrong loading "+ file.getName() + newline);
	    	e.printStackTrace();
	    }
	}

    
    private static void createAndShowGUI() 
    {
        JFrame frame = new JFrame("Azizul Hakim's Spell Checker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(new Hakim_SpellCheck());
        frame.pack();
        frame.setVisible(true);
    }
    

}
