package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

import com.toedter.calendar.JDateChooser;

import custom.ReadOnlyTableModel;
import custom.StatusCellRenderer;
import net.miginfocom.swing.MigLayout;

public class pnlAccountManagment extends JPanel
		implements ActionListener, MouseListener, ItemListener, PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(pnlAccountManagment.class.getName());

//	private EmployeeTimekeeping chamCong = null;
	private ReadOnlyTableModel tableModel2;
	
	private JTextField txtName;
	private JDateChooser txtDate;
	private JTextField txtID;
	private JButton btnUpdate;
	private JComboBox<Time> cmbCheckIn;
	private JComboBox<Time> cmbCheckOut;
	private JTextField txtSearch;
	private JTextField txtTotalCount;
	private JTextField txtOnduty;
	private JTextField txtOffDuty;
	private JTextField txtOnLeave;
	private JTextField txtRole;
	private JComboBox<String> cmbSelectedDepartment = null;

	private int selectedRow = -1;

	private JTable tbl;
	private JComboBox<String> cmbStatus;
	private JTextField txtTimeOverHours;

	/**
	 * Create the panel.
	 */
	@SuppressWarnings("deprecation")
	public pnlAccountManagment() {
		int width = 1920;
		int height = (width * 9) / 16;
		setSize(width, height);
		setLayout(new BorderLayout(0, 0));

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		add(contentPane, BorderLayout.CENTER);
		contentPane.setLayout(new BorderLayout(0, 0));

		JPanel pnlNorth = new JPanel();
		pnlNorth.setBackground(Color.CYAN);
		contentPane.add(pnlNorth, BorderLayout.NORTH);

		JLabel lblTitle = new JLabel("QUẢN LÝ TÀI KHOẢN");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 30));
		pnlNorth.add(lblTitle);

		JPanel pnlCenter = new JPanel();
		contentPane.add(pnlCenter, BorderLayout.CENTER);
		pnlCenter.setLayout(new BorderLayout(0, 0));

		JPanel pnlEmployeesContainer = new JPanel();
		pnlEmployeesContainer.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 0, 5),
				new TitledBorder(
						new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)),
						"Danh sách nhân viên", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0))));
		pnlCenter.add(pnlEmployeesContainer, BorderLayout.CENTER);
		pnlEmployeesContainer.setLayout(new BorderLayout(0, 0));

		JPanel pnlEmployeeList = new JPanel();
		pnlEmployeesContainer.add(pnlEmployeeList, BorderLayout.CENTER);

		pnlEmployeeList.setLayout(new BorderLayout(0, 0));
		String[] header2 = { "#", "Mã", "Họ", "Tên", "Chức Vụ", "Trạng Thái", "Giờ Vào", "Giờ Ra", "Số Giờ Làm Thêm" };
		tableModel2 = new ReadOnlyTableModel(header2, 0);
		tbl = new JTable(tableModel2);
		tbl.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());
		tbl.setAutoCreateRowSorter(true);
		tbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JScrollPane scr = new JScrollPane(tbl);
		scr.setFont(new Font("Tahoma", Font.PLAIN, 14));
		pnlEmployeeList.add(scr);

		JPanel pnlTimekeepingContainer = new JPanel();
		pnlTimekeepingContainer
				.setBorder(new CompoundBorder(new EmptyBorder(5, 5, 0, 5),
						new TitledBorder(
								new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
										new Color(160, 160, 160)),
								"Thông tin chấm công nhân viên", TitledBorder.LEADING, TitledBorder.TOP, null,
								new Color(0, 0, 0))));
		pnlCenter.add(pnlTimekeepingContainer, BorderLayout.EAST);
		pnlTimekeepingContainer.setLayout(new BorderLayout(0, 0));

		JPanel pnlTimekeepingInfo = new JPanel();
		pnlTimekeepingInfo.setBorder(new EmptyBorder(0, 30, 0, 30));
		pnlTimekeepingContainer.add(pnlTimekeepingInfo, BorderLayout.NORTH);
		pnlTimekeepingInfo.setLayout(new MigLayout("", "[][grow]", "[][][][][][][]"));

		JLabel lblID = new JLabel("Mã nhân viên:");
		lblID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(lblID, "cell 0 0,alignx trailing");

		txtID = new JTextField();
		txtID.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(txtID, "cell 1 0,growx");
		txtID.setColumns(10);
		txtID.setEditable(false);

		JLabel lblName = new JLabel("Họ và tên:");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(lblName, "cell 0 1,alignx trailing");

		txtName = new JTextField();
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(txtName, "cell 1 1,growx");
		txtName.setColumns(20);
		txtName.setEditable(false);

		JLabel lblRole = new JLabel("Chức Vụ:");
		lblRole.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(lblRole, "cell 0 2,alignx trailing");

		txtRole = new JTextField();
		txtRole.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(txtRole, "cell 1 2,growx");
		txtRole.setColumns(10);
		txtRole.setEditable(false);

		JLabel lblStatus = new JLabel("Trạng Thái:");
		lblStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(lblStatus, "cell 0 3,alignx trailing");

		cmbStatus = new JComboBox<>();
		cmbStatus.setEnabled(false);
		cmbStatus.addItemListener(this);
		cmbStatus.setModel(new DefaultComboBoxModel<>(new String[] { "Đi làm", "Nghỉ phép", "Vắng" }));
		cmbStatus.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(cmbStatus, "cell 1 3,growx");

		JLabel lblCheckIn = new JLabel("Giờ đến:");
		lblCheckIn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(lblCheckIn, "cell 0 4,alignx trailing");

		Vector<Time> checkInDate = new Vector<>();
		Vector<Time> checkOutDate = new Vector<>();
		for (int i = 8; i <= 20; i++) {
			checkInDate.add(new Time(i, 0, 0));
			checkOutDate.add(new Time(i, 0, 0));
		}

		cmbCheckIn = new JComboBox<>();
		cmbCheckIn.addItemListener(this);
		cmbCheckIn.setEnabled(false);

		cmbCheckIn.setModel(new DefaultComboBoxModel<>(checkInDate));
		cmbCheckIn.setSelectedIndex(0);
		cmbCheckIn.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(cmbCheckIn, "cell 1 4,growx");

		JLabel lblCheckOut = new JLabel("Giờ về:");
		lblCheckOut.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(lblCheckOut, "cell 0 5,alignx trailing");

		cmbCheckOut = new JComboBox<>();
		cmbCheckOut.addItemListener(this);
		cmbCheckOut.setEnabled(false);
		cmbCheckOut.setModel(new DefaultComboBoxModel<>(checkOutDate));
		cmbCheckOut.setSelectedIndex(9);
		cmbCheckOut.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(cmbCheckOut, "cell 1 5,growx");

		JLabel lblOT = new JLabel("Số giờ làm thêm:");
		lblOT.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(lblOT, "cell 0 6,alignx trailing");

		txtTimeOverHours = new JTextField();
		txtTimeOverHours.setEditable(false);
		txtTimeOverHours.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingInfo.add(txtTimeOverHours, "cell 1 6,growx");
		txtTimeOverHours.setColumns(10);

		JPanel pnlTimekeepingUpdate = new JPanel();
		pnlTimekeepingUpdate.setBorder(new EmptyBorder(30, 10, 100, 10));
		pnlTimekeepingContainer.add(pnlTimekeepingUpdate, BorderLayout.CENTER);

		btnUpdate = new JButton("Cập Nhật");
		btnUpdate.addActionListener(this);
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 24));
		pnlTimekeepingUpdate.add(btnUpdate);

		JPanel pnlTimekeepingStatictis = new JPanel();
		pnlTimekeepingContainer.add(pnlTimekeepingStatictis, BorderLayout.SOUTH);
		pnlTimekeepingStatictis.setLayout(new MigLayout("", "[][grow]", "[][][][]"));

		JLabel lblTotalCount = new JLabel("Tổng:");
		lblTotalCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(lblTotalCount, "cell 0 0,alignx trailing");

		txtTotalCount = new JTextField();
		txtTotalCount.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(txtTotalCount, "cell 1 0,growx");
		txtTotalCount.setColumns(10);

		JLabel lblOnDuty = new JLabel("Đi Làm:");
		lblOnDuty.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(lblOnDuty, "cell 0 1,alignx trailing");

		txtOnduty = new JTextField();
		txtOnduty.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(txtOnduty, "cell 1 1,growx");
		txtOnduty.setColumns(10);

		JLabel lblOffDuty = new JLabel("Vắng:");
		lblOffDuty.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(lblOffDuty, "cell 0 2,alignx trailing");

		txtOffDuty = new JTextField();
		txtOffDuty.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(txtOffDuty, "cell 1 2,growx");
		txtOffDuty.setColumns(10);

		JLabel lblOnLeave = new JLabel("Phép:");
		lblOnLeave.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(lblOnLeave, "cell 0 3,alignx trailing");

		txtOnLeave = new JTextField();
		txtOnLeave.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlTimekeepingStatictis.add(txtOnLeave, "cell 1 3,growx");
		txtOnLeave.setColumns(10);

		JPanel pnlEmployeeFunc = new JPanel();
		FlowLayout fl_pnlEmployeeFunc = (FlowLayout) pnlEmployeeFunc.getLayout();
		fl_pnlEmployeeFunc.setAlignment(FlowLayout.LEFT);
		pnlEmployeesContainer.add(pnlEmployeeFunc, BorderLayout.NORTH);

		txtDate = new JDateChooser();
		txtDate.setDateFormatString("dd/MM/yyyy");
		txtDate.setDate(new Date());
		txtDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtDate.setPreferredSize(new Dimension(128, 28));
		txtDate.addPropertyChangeListener(this);
		pnlEmployeeFunc.add(txtDate);

		// TODO:
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		txtDate.setMinSelectableDate(cal.getTime());
		cal.setTime(new Date());
		txtDate.setMaxSelectableDate(cal.getTime());

		cmbSelectedDepartment = new JComboBox<>();
		cmbSelectedDepartment.setModel(new DefaultComboBoxModel<>(new String[] { "Tất Cả Phòng Ban" }));
		
//		ArrayList<Department> dSPB = DepartmentDAO.getAllDePartment();
//		for (Department p : dSPB) {
//			cmbSelectedDepartment.addItem(p.getDepName());
//		}
		
		cmbSelectedDepartment.addItemListener(this);
		cmbSelectedDepartment.setFont(new Font("Tahoma", Font.PLAIN, 16));
		pnlEmployeeFunc.add(cmbSelectedDepartment);

		JLabel lblSearch = new JLabel("Tìm Kiếm:");
		pnlEmployeeFunc.add(lblSearch);
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));

		txtSearch = new JTextField();
		pnlEmployeeFunc.add(txtSearch);
		txtSearch.setFont(new Font("Tahoma", Font.PLAIN, 16));
		txtSearch.setColumns(10);

		tbl.addMouseListener(this);
		cmbCheckIn.addItemListener(this);
		cmbCheckOut.addItemListener(this);
		btnUpdate.setEnabled(false);

		alignTable();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		selectedRow = tbl.getSelectedRow();
		
//		chamCong = EmployeeTimekeepingDAO.getEmployeeTimeKeeping(tableModel2.getValueAt(selectedRow, 1).toString(),
//				txtDate.getDate());

		if (selectedRow >= 0 && selectedRow < tbl.getRowCount()) {

			cmbStatus.setEnabled(true);
			cmbEnable(true);

			String status = tableModel2.getValueAt(selectedRow, 5).toString();
			if (status.equals("Đi làm")) {
				cmbStatus.setSelectedIndex(0);
			}
			if (status.equals("Nghỉ phép")) {
				cmbStatus.setSelectedIndex(1);
			}
			if (status.equals("Vắng")) {
				cmbStatus.setSelectedIndex(2);
			}

//			EmployeeTimekeeping a = EmployeeTimekeepingDAO
//					.getEmployeeTimeKeeping(String.valueOf(tableModel2.getValueAt(selectedRow, 1)), txtDate.getDate());

			btnUpdate.setEnabled(true);

//			txtID.setText(a.getE().getId());
//			txtName.setText(a.getE().getlName() + " " + a.getE().getfName());
//			txtRole.setText(a.getE().getRole().getRole());

			cmbCheckIn.setSelectedItem(tbl.getValueAt(selectedRow, 6));
			cmbCheckOut.setSelectedItem(tbl.getValueAt(selectedRow, 7));
			txtTimeOverHours.setText(tbl.getValueAt(selectedRow, 8) + "");

		} else {
			selectedRow = -1;
			cmbStatus.setEnabled(false);
			cmbEnable(false);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnUpdate)) {
			Time checkInTime = (Time) cmbCheckIn.getSelectedItem();
			Time checkOutTime = (Time) cmbCheckOut.getSelectedItem();
			int onduty = (int) ((checkOutTime.getTime() - checkInTime.getTime()) / (60 * 60 * 1000));
			
			if (onduty > 0 || cmbStatus.getSelectedIndex() == 2 ) {
				String id = tableModel2.getValueAt(selectedRow, 1).toString();

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				LocalTime localTime = LocalTime.parse(tableModel2.getValueAt(selectedRow, 7).toString(), formatter);
				Time sqlTime = Time.valueOf(localTime);
				LocalTime localTime2 = LocalTime.parse(tableModel2.getValueAt(selectedRow, 6).toString(), formatter);
				Time sqlTime2 = Time.valueOf(localTime2);

//				EmployeeTimekeeping a = new EmployeeTimekeeping(new Empolyee(tableModel2.getValueAt(selectedRow, 1).toString()),
//						txtDate.getDate(), cmbStatus.getSelectedIndex() + 1, sqlTime2, sqlTime,
//						Integer.parseInt(tableModel2.getValueAt(selectedRow, 8).toString()), new Empolyee("E2023001"));
//
//				EmployeeTimekeepingDAO.updateEmployeeTimekeeping(a);
				JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
				LOGGER.info("Update");
				return;
			}
			JOptionPane.showMessageDialog(null, "Vui lòng chọn giờ đến và giờ về hợp lệ!");
			
			
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			handleItemSelected(e);
		}

	}

//	@Override
//	public void propertyChange(PropertyChangeEvent evt) {
//		Object o = evt.getSource();
//		if (o.equals(txtDate)) {
//			int a = EmployeeDAO.getEmployeeCount(txtDate.getDate());
//			int b = EmployeeTimekeepingDAO.countEmployeeTimekeeping(txtDate.getDate());
//			if (b == 0) {
//				EmployeeTimekeepingDAO.addNewTimekeeping(txtDate.getDate());
//				UpdateEmployeesTimekeepingTable(EmployeeTimekeepingDAO.getAllEmployeeTimekeeping(txtDate.getDate()));
//			} else if (a == b)
//				UpdateEmployeesTimekeepingTable(EmployeeTimekeepingDAO.getAllEmployeeTimekeeping(txtDate.getDate()));
//			else if (b < a) {
//				EmployeeTimekeepingDAO.insertNewTimekeeping(txtDate.getDate());
//				UpdateEmployeesTimekeepingTable(EmployeeTimekeepingDAO.getAllEmployeeTimekeeping(txtDate.getDate()));
//			}
//		}
//	}

//	private void UpdateEmployeesTimekeepingTable(List<EmployeeTimekeeping> list) {
//		tableModel2.setRowCount(0);
//		for (EmployeeTimekeeping a : list) {
//			Empolyee e = a.getE();
//			tableModel2.addRow(new Object[] { tbl.getRowCount() + 1, e.getId(), e.getlName(), e.getfName(), e.getRole(),
//					getStatus(a.getStatus()), a.getStart(), a.getEnd(), a.getOt() });
//		}
//	}

	private String getStatus(int i) {
		String x = null;
		if (i == 1) {
			x = "Đi làm";
		}
		if (i == 2) {
			x = "Nghỉ phép";
		}
		if (i == 3) {
			x = "Vắng";
		}
		return x;
	}

	private void handleItemSelected(ItemEvent e) {
		Object source = e.getSource();

//		if (handleDepartmentSelection(source)) {
//			if (cmbSelectedDepartment != null && cmbSelectedDepartment.getSelectedIndex() != 0) {
//			}
//			return;
//		}

		if (selectedRow == -1) {
			return;
		}

		if (source == cmbStatus) {
			handleCmbStatusSelection();
			tbl.setValueAt(0, selectedRow, 8);
		}

		if (source == cmbCheckIn && (cmbCheckIn.getSelectedIndex() > -1)) {
			getTimeOverHours();
			handleCmbCheckInSelection();

		}

		if (source == cmbCheckOut && (cmbCheckOut.getSelectedIndex() > -1)) {
			getTimeOverHours();
			handleCmbCheckOutSelection();
		}

	}

	private void getTimeOverHours() {
		int zero = 0;
		Time checkInTime = (Time) cmbCheckIn.getSelectedItem();
		Time checkOutTime = (Time) cmbCheckOut.getSelectedItem();
		int overtimeHours = (int) ((checkOutTime.getTime() - checkInTime.getTime()) / (60 * 60 * 1000)) - 8;
		if (overtimeHours > 0) {
			zero = overtimeHours;
		}
		txtTimeOverHours.setText(zero + "");
		tbl.setValueAt(zero, selectedRow, 8);

	}

//	private boolean handleDepartmentSelection(Object source) {
//		if (cmbSelectedDepartment != null) {
//			if (cmbSelectedDepartment.getSelectedIndex() == 0 && source.equals(cmbSelectedDepartment)) {
//				UpdateEmployeesTimekeepingTable(EmployeeTimekeepingDAO.getAllEmployeeTimekeeping(txtDate.getDate()));
//				return true;
//			}
//			if (source.equals(cmbSelectedDepartment)) {
//				String department = DepartmentDAO.getDepartmentByName(cmbSelectedDepartment.getSelectedItem() + "")
//						.getDepID();
//				UpdateEmployeesTimekeepingTable(
//						EmployeeTimekeepingDAO.getEmployeeTimekeepingByDepartment(department, txtDate.getDate()));
//				return true;
//			}
//		}
//		return false;
//	}

	private void handleCmbStatusSelection() {
		tableModel2.setValueAt(cmbStatus.getSelectedItem(), selectedRow, 5);
		if (cmbStatus.getSelectedIndex() == 0) {
			cmbCheckIn.setSelectedIndex(0);
			cmbCheckOut.setSelectedIndex(8);
			tableModel2.setValueAt(cmbCheckIn.getSelectedItem(), selectedRow, 6);
			tableModel2.setValueAt(cmbCheckOut.getSelectedItem(), selectedRow, 7);
			tableModel2.setValueAt(txtTimeOverHours.getText(), selectedRow, 8);
		}
		
		
		if (cmbStatus.getSelectedIndex() > 0) {
			cmbCheckIn.setEnabled(false);
			cmbCheckOut.setEnabled(false);

			if (cmbStatus.getSelectedIndex() == 1) {
				handleStatus1Selection();
			} else if (cmbStatus.getSelectedIndex() == 2) {
				handleStatus2Selection();
			}

			return;
		}

		cmbCheckIn.setEnabled(true);
		cmbCheckOut.setEnabled(true);
	}

	private void handleStatus1Selection() {
		cmbCheckIn.setSelectedIndex(0);
		cmbCheckOut.setSelectedIndex(8);
		tableModel2.setValueAt(cmbCheckIn.getSelectedItem(), selectedRow, 6);
		tableModel2.setValueAt(cmbCheckOut.getSelectedItem(), selectedRow, 7);
		tableModel2.setValueAt(0, selectedRow, 8);
	}

	private void handleStatus2Selection() {
		cmbCheckIn.setSelectedIndex(0);
		cmbCheckOut.setSelectedIndex(0);
		tableModel2.setValueAt(cmbCheckIn.getSelectedItem(), selectedRow, 6);
		tableModel2.setValueAt(cmbCheckOut.getSelectedItem(), selectedRow, 7);
		tableModel2.setValueAt(0, selectedRow, 8);
	}

	private void handleCmbCheckInSelection() {
		tableModel2.setValueAt(cmbCheckIn.getSelectedItem(), selectedRow, 6);
	}

	private void handleCmbCheckOutSelection() {
		tableModel2.setValueAt(cmbCheckOut.getSelectedItem(), selectedRow, 7);
	}

	private void cmbEnable(boolean a) {
		cmbCheckIn.setEnabled(a);
		cmbCheckOut.setEnabled(a);
	}

	private void alignTable() {
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tbl.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tbl.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		tbl.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		tbl.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		tbl.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
}
