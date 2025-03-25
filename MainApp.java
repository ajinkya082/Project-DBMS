package ajinkya;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

// Main Application Class
public class MainApp extends Frame implements ActionListener {
    private Button btnPatient, btnDonor, btnOrganRegistration, btnHospital, btnDoctor;

    public MainApp() {
        // Create buttons
        btnPatient = new Button("Patient");
        btnDonor = new Button("Donor");
        btnOrganRegistration = new Button("Organ Registration");
        btnHospital = new Button("Hospital");
        btnDoctor = new Button("Doctor");

        // Set layout
        setLayout(new FlowLayout());

        // Add buttons to frame
        add(btnPatient);
        add(btnDonor);
        add(btnOrganRegistration);
        add(btnHospital);
        add(btnDoctor);

        // Add action listeners
        btnPatient.addActionListener(this);
        btnDonor.addActionListener(this);
        btnOrganRegistration.addActionListener(this);
        btnHospital.addActionListener(this);
        btnDoctor.addActionListener(this);

        // Frame settings
        setTitle("Oragan donation system");
        setSize(300, 200);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnPatient) {
            new PatientForm();
        } else if (e.getSource() == btnDonor) {
            new DonorForm();
        } else if (e.getSource() == btnOrganRegistration) {
            new OrganRegistrationForm();
        } else if (e.getSource() == btnHospital) {
            new HospitalForm();
        } else if (e.getSource() == btnDoctor) {
            new DoctorForm();
        }
    }

    public static void main(String[] args) {
        new MainApp();
    }
}



//Patient Form Class
class PatientForm extends Frame implements ActionListener {
 private TextField txtP_ID, txtName, txtOrganType, txtDOB, txtAge;
 private Button btnCreate, btnRead, btnUpdate, btnDelete;

 public PatientForm() {
     setLayout(new BorderLayout()); // Use BorderLayout for overall layout

     // Panel for input fields using GridLayout
     Panel inputPanel = new Panel(new GridLayout(5, 2, 10, 10));

     // Panel for buttons using FlowLayout
     Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 10));

     // Initialize components
     txtP_ID = new TextField();
     txtName = new TextField();
     txtOrganType = new TextField();
     txtDOB = new TextField();
     txtAge = new TextField();

     // Add input fields to input panel
     inputPanel.add(new Label("P_ID:"));
     inputPanel.add(txtP_ID);
     inputPanel.add(new Label("Name:"));
     inputPanel.add(txtName);
     inputPanel.add(new Label("Organ Type:"));
     inputPanel.add(txtOrganType);
     inputPanel.add(new Label("DOB (YYYY-MM-DD):"));
     inputPanel.add(txtDOB);
     inputPanel.add(new Label("Age:"));
     inputPanel.add(txtAge);

     // Initialize buttons
     btnCreate = new Button("Create");
     btnRead = new Button("Read");
     btnUpdate = new Button("Update");
     btnDelete = new Button("Delete");

     // Add buttons to button panel
     buttonPanel.add(btnCreate);
     buttonPanel.add(btnRead);
     buttonPanel.add(btnUpdate);
     buttonPanel.add(btnDelete);

     // Add panels to frame
     add(inputPanel, BorderLayout.CENTER);  // Input panel goes in the center
     add(buttonPanel, BorderLayout.SOUTH);  // Buttons go at the bottom (south)

     // Add action listeners to buttons
     btnCreate.addActionListener(this);
     btnRead.addActionListener(this);
     btnUpdate.addActionListener(this);
     btnDelete.addActionListener(this);

     // Frame settings
     setTitle("Patient CRUD");
     setSize(400, 300);
     setVisible(true);
     addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent we) {
             dispose();
         }
     });
 }

 public void actionPerformed(ActionEvent e) {
     if (e.getSource() == btnCreate) {
         createPatient();
     } else if (e.getSource() == btnRead) {
         readPatient();
     } else if (e.getSource() == btnUpdate) {
         updatePatient();
     } else if (e.getSource() == btnDelete) {
         deletePatient();
     }
 }


    private void createPatient() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Patient (P_ID, Name, Organ_Type, DOB, Age) VALUES (?, ?, ?, ?, ?)")) {
            
            pstmt.setInt(1, Integer.parseInt(txtP_ID.getText()));
            pstmt.setString(2, txtName.getText());
            pstmt.setString(3, txtOrganType.getText());
            pstmt.setDate(4, Date.valueOf(txtDOB.getText())); // Ensure the format is YYYY-MM-DD
            pstmt.setInt(5, Integer.parseInt(txtAge.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Patient created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating patient: " + e.getMessage());
        }
    }

    private void readPatient() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Patient WHERE P_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtP_ID.getText()));
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                txtName.setText(rs.getString("Name"));
                txtOrganType.setText(rs.getString("Organ_Type"));
                txtDOB.setText(rs.getDate("DOB").toString());
                txtAge.setText(String.valueOf(rs.getInt("Age")));
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading patient: " + e.getMessage());
        }
    }


    private void updatePatient() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Patient SET Name = ?, Organ_Type = ?, DOB = ?, Age = ? WHERE P_ID = ?")) {
            
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtOrganType.getText());
            pstmt.setDate(3, Date.valueOf(txtDOB.getText())); // Ensure the format is YYYY-MM-DD
            pstmt.setInt(4, Integer.parseInt(txtAge.getText()));
            pstmt.setInt(5, Integer.parseInt(txtP_ID.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Patient updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating patient: " + e.getMessage());
        }
    }


    private void deletePatient() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Patient WHERE P_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtP_ID.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Patient deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Patient not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting patient: " + e.getMessage());
        }
    }

}

class DonorForm extends Frame implements ActionListener {
    private TextField txtD_ID, txtH_ID, txtName, txtAge, txtDOB;
    private Button btnCreate, btnRead, btnUpdate, btnDelete;

    public DonorForm() {
        setLayout(new BorderLayout()); // Use BorderLayout for the overall layout

        // Panel for input fields using GridLayout
        Panel inputPanel = new Panel(new GridLayout(5, 2, 10, 10));

        // Panel for buttons using FlowLayout
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Initialize components
        txtD_ID = new TextField();
        txtH_ID = new TextField();
        txtName = new TextField();
        txtAge = new TextField();
        txtDOB = new TextField();

        // Add input fields to the input panel
        inputPanel.add(new Label("D_ID:"));
        inputPanel.add(txtD_ID);
        inputPanel.add(new Label("H_ID:"));
        inputPanel.add(txtH_ID);
        inputPanel.add(new Label("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new Label("Age:"));
        inputPanel.add(txtAge);
        inputPanel.add(new Label("DOB (YYYY-MM-DD):"));
        inputPanel.add(txtDOB);

        // Initialize buttons
        btnCreate = new Button("Create");
        btnRead = new Button("Read");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");

        // Add buttons to the button panel
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnRead);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);  // Input fields in the center
        add(buttonPanel, BorderLayout.SOUTH);  // Buttons at the bottom

        // Add action listeners to the buttons
        btnCreate.addActionListener(this);
        btnRead.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);

        // Frame settings
        setTitle("Donor CRUD");
        setSize(400, 300);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCreate) {
            createDonor();
        } else if (e.getSource() == btnRead) {
            readDonor();
        } else if (e.getSource() == btnUpdate) {
            updateDonor();
        } else if (e.getSource() == btnDelete) {
            deleteDonor();
        }
    }
    private void createDonor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Donor (D_ID, H_ID, Name, Age, DOB) VALUES (?, ?, ?, ?, ?)")) {
            
            pstmt.setInt(1, Integer.parseInt(txtD_ID.getText()));
            pstmt.setInt(2, Integer.parseInt(txtH_ID.getText()));
            pstmt.setString(3, txtName.getText());
            pstmt.setInt(4, Integer.parseInt(txtAge.getText()));
            pstmt.setDate(5, Date.valueOf(txtDOB.getText())); // Ensure the format is YYYY-MM-DD
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Donor created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating donor: " + e.getMessage());
        }
    }

    private void readDonor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Donor WHERE D_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtD_ID.getText()));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                txtH_ID.setText(String.valueOf(rs.getInt("H_ID")));
                txtName.setText(rs.getString("Name"));
                txtAge.setText(String.valueOf(rs.getInt("Age")));
                txtDOB.setText(rs.getDate("DOB").toString());
            } else {
                JOptionPane.showMessageDialog(this, "No donor found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading donor: " + e.getMessage());
        }
    }


    private void updateDonor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Donor SET H_ID = ?, Name = ?, Age = ?, DOB = ? WHERE D_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtH_ID.getText()));
            pstmt.setString(2, txtName.getText());
            pstmt.setInt(3, Integer.parseInt(txtAge.getText()));
            pstmt.setDate(4, Date.valueOf(txtDOB.getText()));
            pstmt.setInt(5, Integer.parseInt(txtD_ID.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Donor updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No donor found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating donor: " + e.getMessage());
        }
    }

    private void deleteDonor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Donor WHERE D_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtD_ID.getText()));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Donor deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No donor found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting donor: " + e.getMessage());
        }
    }

}

// Organ Registration Form Class
class OrganRegistrationForm extends Frame implements ActionListener {
    private TextField txtOrgan_ID, txtDonor_ID;
    private Button btnCreate, btnRead, btnUpdate, btnDelete;

    public OrganRegistrationForm() {
        setLayout(new BorderLayout()); // Use BorderLayout for the overall layout

        // Panel for input fields using GridLayout
        Panel inputPanel = new Panel(new GridLayout(2, 2, 10, 10));

        // Panel for buttons using FlowLayout
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Initialize components
        txtOrgan_ID = new TextField();
        txtDonor_ID = new TextField();

        // Add input fields to the input panel
        inputPanel.add(new Label("Organ_ID:"));
        inputPanel.add(txtOrgan_ID);
        inputPanel.add(new Label("Donor_ID:"));
        inputPanel.add(txtDonor_ID);

        // Initialize buttons
        btnCreate = new Button("Create");
        btnRead = new Button("Read");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");

        // Add buttons to the button panel
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnRead);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);  // Input fields in the center
        add(buttonPanel, BorderLayout.SOUTH);  // Buttons at the bottom

        // Add action listeners to the buttons
        btnCreate.addActionListener(this);
        btnRead.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);

        // Frame settings
        setTitle("Organ Registration CRUD");
        setSize(400, 200);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCreate) {
            createOrganRegistration();
        } else if (e.getSource() == btnRead) {
            readOrganRegistration();
        } else if (e.getSource() == btnUpdate) {
            updateOrganRegistration();
        } else if (e.getSource() == btnDelete) {
            deleteOrganRegistration();
        }
    }


    private void createOrganRegistration() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO OrganRegistration (Organ_ID, Donor_ID) VALUES (?, ?)")) {
            
            pstmt.setInt(1, Integer.parseInt(txtOrgan_ID.getText()));
            pstmt.setInt(2, Integer.parseInt(txtDonor_ID.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Organ Registration created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating organ registration: " + e.getMessage());
        }
    }

    private void readOrganRegistration() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM OrganRegistration WHERE Reg_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtReg_ID.getText())); // Assuming txtReg_ID is a text field for the registration ID
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                txtDonor_ID.setText(String.valueOf(rs.getInt("Donor_ID"))); // Assuming txtDonor_ID is a text field for donor ID
                txtOrganType.setText(rs.getString("OrganType")); // Assuming txtOrganType is a text field for organ type
                txtRegDate.setText(rs.getDate("RegDate").toString()); // Assuming txtRegDate is a text field for registration date
            } else {
                JOptionPane.showMessageDialog(this, "No organ registration found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading organ registration: " + e.getMessage());
        }
    }


    private void updateOrganRegistration() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE OrganRegistration SET Donor_ID = ?, OrganType = ?, RegDate = ? WHERE Reg_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtDonor_ID.getText())); // Donor ID
            pstmt.setString(2, txtOrganType.getText()); // Organ Type
            pstmt.setDate(3, Date.valueOf(txtRegDate.getText())); // Registration Date
            pstmt.setInt(4, Integer.parseInt(txtReg_ID.getText())); // Registration ID
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Organ registration updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No organ registration found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating organ registration: " + e.getMessage());
        }
    }


    private void deleteOrganRegistration() {
        String url = "jdbc:mysql://localhost:3306/hopital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM OrganRegistration WHERE Reg_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtReg_ID.getText())); // Registration ID
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Organ registration deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No organ registration found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting organ registration: " + e.getMessage());
        }
    }

}

// Hospital Form Class
class HospitalForm extends Frame implements ActionListener {
    private TextField txtH_ID, txtName, txtDean;
    private Button btnCreate, btnRead, btnUpdate, btnDelete;

    public HospitalForm() {
        setLayout(new BorderLayout()); // Set BorderLayout for the frame

        // Panel for input fields
        Panel inputPanel = new Panel(new GridLayout(3, 2, 10, 10));

        // Panel for buttons
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        // Initialize input components
        txtH_ID = new TextField();
        txtName = new TextField();
        txtDean = new TextField();

        // Initialize buttons
        btnCreate = new Button("Create");
        btnRead = new Button("Read");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");

        // Add input components to the input panel
        inputPanel.add(new Label("H_ID:"));
        inputPanel.add(txtH_ID);
        inputPanel.add(new Label("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new Label("Dean:"));
        inputPanel.add(txtDean);

        // Add buttons to the button panel
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnRead);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);  // Input fields in the center
        add(buttonPanel, BorderLayout.SOUTH);  // Buttons at the bottom

        // Add action listeners to buttons
        btnCreate.addActionListener(this);
        btnRead.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);

        // Frame settings
        setTitle("Hospital CRUD");
        setSize(400, 250);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCreate) {
            createHospital();
        } else if (e.getSource() == btnRead) {
            readHospital();
        } else if (e.getSource() == btnUpdate) {
            updateHospital();
        } else if (e.getSource() == btnDelete) {
            deleteHospital();
        }
    }


    private void createHospital() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Hospital (H_ID, Name, Dean) VALUES (?, ?, ?)")) {
            
            pstmt.setInt(1, Integer.parseInt(txtH_ID.getText()));
            pstmt.setString(2, txtName.getText());
            pstmt.setString(3, txtDean.getText());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Hospital created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating hospital: " + e.getMessage());
        }
    }

    private void readHospital() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Hospital WHERE H_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtH_ID.getText()));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                txtName.setText(rs.getString("Name"));
                txtDean.setText(rs.getString("Dean"));
            } else {
                JOptionPane.showMessageDialog(this, "No hospital found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading hospital: " + e.getMessage());
        }
    }


    private void updateHospital() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Hospital SET Name = ?, Dean = ? WHERE H_ID = ?")) {
            
            pstmt.setString(1, txtName.getText());
            pstmt.setString(2, txtDean.getText());
            pstmt.setInt(3, Integer.parseInt(txtH_ID.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Hospital updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No hospital found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating hospital: " + e.getMessage());
        }
    }

    private void deleteHospital() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Hospital WHERE H_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtH_ID.getText()));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Hospital deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No hospital found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting hospital: " + e.getMessage());
        }
    }

    }

// Doctor Form Class
class DoctorForm extends Frame implements ActionListener {
    private TextField txtD_ID, txtH_ID, txtName, txtSpecialization;
    private Button btnCreate, btnRead, btnUpdate, btnDelete;

    public DoctorForm() {
        setLayout(new BorderLayout());  // Set BorderLayout for the frame

        // Panel for input fields
        Panel inputPanel = new Panel(new GridLayout(4, 2, 10, 10)); // 4 rows, 2 columns for input fields

        // Panel for buttons
        Panel buttonPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 10)); // FlowLayout for buttons

        // Initialize input components
        txtD_ID = new TextField();
        txtH_ID = new TextField();
        txtName = new TextField();
        txtSpecialization = new TextField();

        // Initialize buttons
        btnCreate = new Button("Create");
        btnRead = new Button("Read");
        btnUpdate = new Button("Update");
        btnDelete = new Button("Delete");

        // Add input components to the input panel
        inputPanel.add(new Label("D_ID:"));
        inputPanel.add(txtD_ID);
        inputPanel.add(new Label("H_ID:"));
        inputPanel.add(txtH_ID);
        inputPanel.add(new Label("Name:"));
        inputPanel.add(txtName);
        inputPanel.add(new Label("Specialization:"));
        inputPanel.add(txtSpecialization);

        // Add buttons to the button panel
        buttonPanel.add(btnCreate);
        buttonPanel.add(btnRead);
        buttonPanel.add(btnUpdate);
        buttonPanel.add(btnDelete);

        // Add panels to the frame
        add(inputPanel, BorderLayout.CENTER);  // Input fields in the center
        add(buttonPanel, BorderLayout.SOUTH);  // Buttons at the bottom

        // Add action listeners to buttons
        btnCreate.addActionListener(this);
        btnRead.addActionListener(this);
        btnUpdate.addActionListener(this);
        btnDelete.addActionListener(this);

        // Frame settings
        setTitle("Doctor CRUD");
        setSize(400, 250);
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnCreate) {
            createDoctor();
        } else if (e.getSource() == btnRead) {
            readDoctor();
        } else if (e.getSource() == btnUpdate) {
            updateDoctor();
        } else if (e.getSource() == btnDelete) {
            deleteDoctor();
        }
    }

    private void createDoctor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Doctor (D_ID, H_ID, Name, Specialization) VALUES (?, ?, ?, ?)")) {
            
            pstmt.setInt(1, Integer.parseInt(txtD_ID.getText()));
            pstmt.setInt(2, Integer.parseInt(txtH_ID.getText()));
            pstmt.setString(3, txtName.getText());
            pstmt.setString(4, txtSpecialization.getText());
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Doctor created successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error creating doctor: " + e.getMessage());
        }
    }

    private void readDoctor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Doctor WHERE D_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtD_ID.getText()));
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                txtH_ID.setText(String.valueOf(rs.getInt("H_ID")));
                txtName.setText(rs.getString("Name"));
                txtSpecialization.setText(rs.getString("Specialization"));
            } else {
                JOptionPane.showMessageDialog(this, "No doctor found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error reading doctor: " + e.getMessage());
        }
    }


    private void updateDoctor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("UPDATE Doctor SET H_ID = ?, Name = ?, Specialization = ? WHERE D_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtH_ID.getText()));
            pstmt.setString(2, txtName.getText());
            pstmt.setString(3, txtSpecialization.getText());
            pstmt.setInt(4, Integer.parseInt(txtD_ID.getText()));
            
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Doctor updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No doctor found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating doctor: " + e.getMessage());
        }
    }


    private void deleteDoctor() {
        String url = "jdbc:mysql://localhost:3306/hospital"; // Update with your database
        String user = "root"; // Your database username
        String password = "root"; // Your database password

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Doctor WHERE D_ID = ?")) {
            
            pstmt.setInt(1, Integer.parseInt(txtD_ID.getText()));
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Doctor deleted successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No doctor found with this ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting doctor: " + e.getMessage());
        }
    }

}
