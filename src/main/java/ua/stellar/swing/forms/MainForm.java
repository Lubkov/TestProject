package ua.stellar.swing.forms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import ua.stellar.swing.domain.OrderRow;

public class MainForm extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
	private static final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss"); 
	
	private static final String[] COLUMN_NAMES = {"#", "Наименование", "Кол-во", "Цена", "Сумма"};
	private static ArrayList<OrderRow> orderData;
	
	//Главная панель - разделение главного окна BorderLayout
	private JPanel mainPanel;
	private JPanel westPanel;
	private JPanel eastPanel;
	
	//Верхний Тулбар
	private JToolBar topTollbar;
	private JButton btnSystem;
	private JButton btnFavorite;
	
	private JLabel lblTime;
	
	private JTable table = new JTable();
	private JScrollPane scrollTable = new JScrollPane(table);
	
	private Timer timerTime;
	private ImageIcon icon;
	
	public MainForm(){
		super();
	
		orderData = createOrderList();
		
		this.setTitle("Cash v. 1.01b  -  Билетная касса");
	    this.setBounds(100, 100, 1000, 500);
	    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container container = this.getContentPane();
	    container.setLayout(new BorderLayout());
	    
	    JPanel mainPanel = new JPanel();
	    mainPanel.setLayout(new BorderLayout());

	    topTollbar = new JToolBar();
	    topTollbar.setFloatable(false);
	    topTollbar.addSeparator();  
	    
	    btnSystem = new JButton();
	    btnSystem.addActionListener(this);
	    btnSystem.setActionCommand("system");
	    btnSystem.setFocusPainted(false);
	    icon = new ImageIcon("resources/images/system.png");
	    btnSystem.setIcon(icon);
	    topTollbar.add(btnSystem);
	    
	    btnFavorite = new JButton();
	    btnFavorite.addActionListener(this);
	    btnFavorite.setActionCommand("favorite");
	    btnFavorite.setFocusPainted(false);
	    icon = new ImageIcon("resources/images/favorite.png");
	    btnFavorite.setIcon(icon);
	    topTollbar.add(btnFavorite);
	        
	    mainPanel.add(topTollbar, BorderLayout.NORTH);
	    
	    westPanel = new JPanel();
	    //westPanel.setBackground(Color.blue);
	    westPanel.setPreferredSize(new Dimension(100, 1)); 
	    	    
	    lblTime = new JLabel();
	    lblTime.setText("00:00:00");
	    westPanel.add(lblTime);
	    
	    mainPanel.add(westPanel, BorderLayout.WEST);
	    
	 // Описываем модель таблицы
	    TableModel myDataModel = new AbstractTableModel() {
	    	// тут все просто - это количество столбцов
	    	@Override
	    	public int getColumnCount() { 	    	
	    		return COLUMN_NAMES.length; 
	    	}
	    
	    	//Количество строк (длина нашего ArrayList с данными)
	    	@Override
	    	public int getRowCount() {
	    		return orderData.size();
	    	}
	    	
	    	// здесь уже интереснее - метод возвращает данные для ячейки таблицы
	    	@Override
	    	public Object getValueAt(int row, int col) {
	    		OrderRow order = (OrderRow) orderData.get(row);
	    			    		
	    	    switch (col) {
	    	    	case 0: return order.getId();
	       	        case 1: return order.getName();
	    	        case 2: return order.getCount();
	    	        case 3: return order.getPrice();
	    	        case 4: return order.getSum();
	    	        default: return "";
	    	    }
	    	}
	      
	    	// метод возвращает имена столбцов
	    	@Override
	    	public String getColumnName(int column) {	    	
	    		return COLUMN_NAMES[column];
	        }
	    	
	    	// запрещаем редактирование данных в таблице, этим займемся позже
	    	@Override
	    	public boolean isCellEditable(int row, int col) {
	    		return false;
	    	}
	    	
	    	// оставляем как есть, потом вернемся
	    	@Override
	    	public void setValueAt(Object aValue, int row, int column) {
	    		
	    	}
	    	
	    	@Override
	    	public Class getColumnClass(int c) {
	    		return (String.class);
	    	}
	    };
	    
	    table.setModel(myDataModel);
	   	table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
	    table.getColumnModel().getColumn(0).setMaxWidth(50);	  
	    table.getColumnModel().getColumn(1).setPreferredWidth(300);
	    
	    
	    alignRight(table, 0);
	    alignRight(table, 2);
	    alignRight(table, 3);
	    alignRight(table, 4);
	    mainPanel.add(scrollTable, BorderLayout.CENTER);
	    
	    
	    eastPanel = new JPanel();
	    eastPanel.setPreferredSize(new Dimension(400, 1)); 
	    mainPanel.add(eastPanel, BorderLayout.EAST);
	    
	    
	    
	    
	    
	    
        //JPanel northPanel = new JPanel();
        //northPanel.setBackground(Color.blue);
        //northPanel.setPreferredSize(new Dimension(10, 100));   
        //mainPanel.add(northPanel, BorderLayout.NORTH);
        
        //JButton southButton = new JButton("SOUTH (PAGE_END)");
        //panel.add(southButton, BorderLayout.SOUTH);
        
        //
        
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.addSeparator();
        toolBar.add(new JLabel("Статус бар - строка состояния"));
        mainPanel.add(toolBar, BorderLayout.SOUTH);
        
        container.add(mainPanel);
        
     // Создание таймера, вызывающего обработчик каждые 10 секунд
        timerTime = new Timer(1000, this);
        timerTime.setActionCommand("updateTime");
        timerTime.start();
	}
	
	private void alignRight(JTable table, int column) {
	    DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
	    rightRenderer.setHorizontalAlignment(JLabel.RIGHT);
	    table.getColumnModel().getColumn(column).setCellRenderer(rightRenderer);
	}
	

	@Override
	public void actionPerformed(ActionEvent event) {
		
		if ("updateTime".equals(event.getActionCommand())) {
			lblTime.setText("" + timeFormat.format(new Date()));
		}
		
		if ("system".equals(event.getActionCommand())) {
			System.out.println("Button click = system");
			orderData.remove(0);
			table.updateUI();
		}
		
		if ("favorite".equals(event.getActionCommand())) {
			System.out.println("Button click = favorite");
			
			OrderRow row = new OrderRow();
			row.setId(2);
			row.setName("Билет 100грн");
			row.setCount(2);
			row.setPrice(100);
			row.setSum(200);
			orderData.add(row);
		}
	}
	
	public ArrayList<OrderRow> createOrderList(){
		ArrayList<OrderRow> data = new ArrayList<OrderRow>();		
		OrderRow row = new OrderRow();
		
		row.setId(0);
		row.setName("Билет 0 грн");
		row.setCount(10);
		row.setPrice(0);
		row.setSum(0);
		data.add(row);
		
		row = new OrderRow();
		row.setId(1);
		row.setName("Билет 100грн");
		row.setCount(2);
		row.setPrice(100);
		row.setSum(200);
		data.add(row);
		
		row = new OrderRow();
		row.setId(2);
		row.setName("Билет 50грн");
		row.setCount(5);
		row.setPrice(50);
		row.setSum(250);
		data.add(row);
		
		return data;
		
		
	}
}
