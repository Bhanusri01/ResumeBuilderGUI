import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

public class ResumeBuilderApp {
    private JFrame window;
    private JTabbedPane tabPanel;

    private JTextField nameField, emailField, contactField, addressBox, linkedinLink;

    private JList<String> educationList;
    private DefaultListModel<String> eduEntries;
    private JTextField degreeField, schoolBox, yearFinished, gpaField;

    private JList<String> workList;
    private DefaultListModel<String> expData;
    private JTextField jobTitleField, companyField, yearsWorkedField, responsibilitiesBox;

    private JList<String> projList;
    private DefaultListModel<String> projEntries;
    private JTextField projectNameBox, projectSummary, techUsedField;

    private JComboBox<String> jobRoleBox;
    private JList<String> skillsDisplay;
    private DefaultListModel<String> skillModel;

    private JList<String> certList;
    private DefaultListModel<String> certModel;
    private JTextField certNameInput, certOrgInput, certYearInput;

    private JTextArea previewBox;
    private JComboBox<String> styleDropdown;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ResumeBuilderApp());
    }

    public ResumeBuilderApp() {
        buildUI();
    }

    private void buildUI() {
        window = new JFrame("Professional Resume Builder");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(950, 700);
        window.setLocationRelativeTo(null);

        tabPanel = new JTabbedPane();
        tabPanel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        tabPanel.add("Personal Info", createTabWithNavigation(makePersonalTab(), false, true));
        tabPanel.add("Education", createTabWithNavigation(makeEducationTab(), true, true));
        tabPanel.add("Experience", createTabWithNavigation(makeExperienceTab(), true, true));
        tabPanel.add("Projects", createTabWithNavigation(makeProjectsTab(), true, true));
        tabPanel.add("Skills", createTabWithNavigation(makeSkillsTab(), true, true));
        tabPanel.add("Certificates", createTabWithNavigation(makeCertificatesTab(), true, true));
        tabPanel.add("Preview", createTabWithNavigation(makePreviewTab(), true, false));

        window.add(tabPanel);
        window.setVisible(true);
    }

    private JPanel createTabWithNavigation(JPanel contentPanel, boolean showBackButton, boolean showNextButton) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(contentPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 15, 5));
        
        if (showBackButton) {
            JButton backButton = makeBtn("◄ Back", new Color(70, 70, 70), e -> {
                int currentIndex = tabPanel.getSelectedIndex();
                if (currentIndex > 0) {
                    tabPanel.setSelectedIndex(currentIndex - 1);
                }
            });
            buttonPanel.add(backButton);
        }
        
        if (showNextButton) {
            JButton nextButton = makeBtn("Next ►", new Color(0, 102, 204), e -> {
                int currentIndex = tabPanel.getSelectedIndex();
                if (currentIndex < tabPanel.getTabCount() - 1) {
                    tabPanel.setSelectedIndex(currentIndex + 1);
                }
            });
            buttonPanel.add(nextButton);
        } else {
            JButton finishButton = makeBtn("Finish & Save", new Color(0, 153, 76), e -> {
                makePreview();
                int option = JOptionPane.showConfirmDialog(window, 
                    "Would you like to save your resume to a file?", 
                    "Resume Completed", 
                    JOptionPane.YES_NO_OPTION);
                
                if (option == JOptionPane.YES_OPTION) {
                    writeOut();
                }
            });
            buttonPanel.add(finishButton);
        }
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JTextField makeField(int width) {
        JTextField tf = new JTextField(width);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return tf;
    }

    private JButton makeBtn(String text, Color color, ActionListener action) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.addActionListener(action);
        
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        
        return btn;
    }

    private void dropField(JPanel panel, GridBagConstraints gbc, int y, String label, JTextField field) {
        gbc.gridx = 0; gbc.gridy = y;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lbl, gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JPanel makePersonalTab() {
        JPanel personalPanel = new JPanel(new GridBagLayout());
        personalPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        personalPanel.setBackground(new Color(248, 248, 248));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        JLabel heading = new JLabel("Enter Your Personal Information");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(new Color(50, 50, 50));
        personalPanel.add(heading, gbc);

        gbc.gridwidth = 1;
        dropField(personalPanel, gbc, 1, "Full Name:", nameField = makeField(25));
        dropField(personalPanel, gbc, 2, "Email:", emailField = makeField(25));
        dropField(personalPanel, gbc, 3, "Phone:", contactField = makeField(25));
        dropField(personalPanel, gbc, 4, "Address:", addressBox = makeField(25));
        dropField(personalPanel, gbc, 5, "LinkedIn:", linkedinLink = makeField(25));

        return personalPanel;
    }

    private JPanel makeEducationTab() {
        JPanel eduPanel = new JPanel(new BorderLayout(10, 10));
        eduPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        eduPanel.setBackground(new Color(248, 248, 248));

        JLabel sectionTitle = new JLabel("Education History", JLabel.CENTER);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(50, 50, 50));
        eduPanel.add(sectionTitle, BorderLayout.NORTH);

        eduEntries = new DefaultListModel<>();
        educationList = new JList<>(eduEntries);
        educationList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        educationList.setBackground(Color.WHITE);
        educationList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        eduPanel.add(new JScrollPane(educationList), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add Education Entry"));
        form.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dropField(form, gbc, 0, "Degree:", degreeField = makeField(20));
        dropField(form, gbc, 1, "University:", schoolBox = makeField(20));
        dropField(form, gbc, 2, "Graduation Year:", yearFinished = makeField(20));
        dropField(form, gbc, 3, "GPA:", gpaField = makeField(20));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(new Color(240, 240, 240));
        buttons.add(makeBtn("Add Entry", new Color(76, 175, 80), e -> addEdu()));
        buttons.add(makeBtn("Remove Selected", new Color(244, 67, 54), e -> removeEdu()));

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(form, BorderLayout.NORTH);
        southPanel.add(buttons, BorderLayout.SOUTH);
        southPanel.setBackground(new Color(240, 240, 240));

        eduPanel.add(southPanel, BorderLayout.SOUTH);
        return eduPanel;
    }

    private JPanel makeExperienceTab() {
        JPanel expPanel = new JPanel(new BorderLayout(10, 10));
        expPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        expPanel.setBackground(new Color(248, 248, 248));

        expData = new DefaultListModel<>();
        workList = new JList<>(expData);
        workList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        workList.setBackground(Color.WHITE);
        workList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel sectionTitle = new JLabel("Work Experience", JLabel.CENTER);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(50, 50, 50));
        expPanel.add(sectionTitle, BorderLayout.NORTH);
        expPanel.add(new JScrollPane(workList), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add Work Experience"));
        form.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dropField(form, gbc, 0, "Job Title:", jobTitleField = makeField(20));
        dropField(form, gbc, 1, "Company:", companyField = makeField(20));
        dropField(form, gbc, 2, "Year(s):", yearsWorkedField = makeField(20));
        dropField(form, gbc, 3, "Responsibilities:", responsibilitiesBox = makeField(20));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(new Color(240, 240, 240));
        buttons.add(makeBtn("Add Experience", new Color(76, 175, 80), e -> addExp()));
        buttons.add(makeBtn("Remove Selected", new Color(244, 67, 54), e -> removeExp()));

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.NORTH);
        south.add(buttons, BorderLayout.SOUTH);
        south.setBackground(new Color(240, 240, 240));

        expPanel.add(south, BorderLayout.SOUTH);
        return expPanel;
    }

    private JPanel makeProjectsTab() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(new Color(248, 248, 248));

        projEntries = new DefaultListModel<>();
        projList = new JList<>(projEntries);
        projList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        projList.setBackground(Color.WHITE);
        projList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel sectionTitle = new JLabel("Projects Portfolio", JLabel.CENTER);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(50, 50, 50));
        panel.add(sectionTitle, BorderLayout.NORTH);
        panel.add(new JScrollPane(projList), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add New Project"));
        form.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dropField(form, gbc, 0, "Project Name:", projectNameBox = makeField(20));
        dropField(form, gbc, 1, "Description:", projectSummary = makeField(20));
        dropField(form, gbc, 2, "Technologies:", techUsedField = makeField(20));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRow.setBackground(new Color(240, 240, 240));
        btnRow.add(makeBtn("Add Project", new Color(76, 175, 80), e -> addProj()));
        btnRow.add(makeBtn("Remove Selected", new Color(244, 67, 54), e -> removeProj()));

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.NORTH);
        south.add(btnRow, BorderLayout.SOUTH);
        south.setBackground(new Color(240, 240, 240));

        panel.add(south, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel makeSkillsTab() {
        JPanel skillsPanel = new JPanel(new BorderLayout(10, 10));
        skillsPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        skillsPanel.setBackground(new Color(248, 248, 248));

        jobRoleBox = new JComboBox<>(new String[] {"Software Developer", "Web Designer", "Data Analyst", "Project Manager", "UX/UI Designer", "Business Analyst"});
        jobRoleBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        skillModel = new DefaultListModel<>();
        skillsDisplay = new JList<>(skillModel);
        skillsDisplay.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        skillsDisplay.setBackground(Color.WHITE);

        JLabel sectionTitle = new JLabel("Skills & Expertise", JLabel.CENTER);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(50, 50, 50));
        skillsPanel.add(sectionTitle, BorderLayout.NORTH);

        JPanel role = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        role.setBackground(new Color(240, 240, 240));
        JLabel roleLabel = new JLabel("Primary Role:");
        roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        role.add(roleLabel);
        role.add(jobRoleBox);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(new Color(240, 240, 240));
        buttons.add(makeBtn("Add Skill", new Color(76, 175, 80), e -> askSkill()));
        buttons.add(makeBtn("Remove Selected", new Color(244, 67, 54), e -> dropSkill()));

        skillsPanel.add(role, BorderLayout.NORTH);
        skillsPanel.add(new JScrollPane(skillsDisplay), BorderLayout.CENTER);
        skillsPanel.add(buttons, BorderLayout.SOUTH);

        return skillsPanel;
    }

    private JPanel makeCertificatesTab() {
        JPanel certPanel = new JPanel(new BorderLayout(10, 10));
        certPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        certPanel.setBackground(new Color(248, 248, 248));

        certModel = new DefaultListModel<>();
        certList = new JList<>(certModel);
        certList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        certList.setBackground(Color.WHITE);
        certList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JLabel sectionTitle = new JLabel("Certificates", JLabel.CENTER);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        sectionTitle.setForeground(new Color(50, 50, 50));
        certPanel.add(sectionTitle, BorderLayout.NORTH);
        certPanel.add(new JScrollPane(certList), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Add Certificate"));
        form.setBackground(new Color(240, 240, 240));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        dropField(form, gbc, 0, "Certificate:", certNameInput = makeField(20));
        dropField(form, gbc, 1, "Organization:", certOrgInput = makeField(20));
        dropField(form, gbc, 2, "Year Earned:", certYearInput = makeField(20));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setBackground(new Color(240, 240, 240));
        buttons.add(makeBtn("Add Certificate", new Color(76, 175, 80), e -> addCert()));
        buttons.add(makeBtn("Remove Selected", new Color(244, 67, 54), e -> removeCert()));

        JPanel south = new JPanel(new BorderLayout());
        south.add(form, BorderLayout.NORTH);
        south.add(buttons, BorderLayout.SOUTH);
        south.setBackground(new Color(240, 240, 240));

        certPanel.add(south, BorderLayout.SOUTH);
        return certPanel;
    }

    private JPanel makePreviewTab() {
        JPanel previewPanel = new JPanel(new BorderLayout(10, 10));
        previewPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        previewPanel.setBackground(new Color(248, 248, 248));

        previewBox = new JTextArea();
        previewBox.setEditable(false);
        previewBox.setFont(new Font("Consolas", Font.PLAIN, 14));
        previewBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        previewBox.setBackground(Color.WHITE);

        styleDropdown = new JComboBox<>(new String[] {"Modern", "Classic", "Professional", "Creative"});
        styleDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        controlPanel.setBackground(new Color(240, 240, 240));

        controlPanel.add(new JLabel("Resume Style:"));
        controlPanel.add(styleDropdown);
        controlPanel.add(makeBtn("Generate Preview", new Color(0, 120, 200), e -> makePreview()));
        controlPanel.add(makeBtn("Save to File", new Color(0, 160, 100), e -> writeOut()));
        controlPanel.add(makeBtn("Print Resume", new Color(150, 100, 200), e -> printResume()));

        previewPanel.add(controlPanel, BorderLayout.NORTH);
        previewPanel.add(new JScrollPane(previewBox), BorderLayout.CENTER);
        
        SwingUtilities.invokeLater(() -> makePreview());
        
        return previewPanel;
    }

    private void printResume() {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setJobName("Resume Printout");
            
            if (job.printDialog()) {
                job.setPrintable((Graphics g, PageFormat pf, int page) -> {
                    if (page > 0) {
                        return Printable.NO_SUCH_PAGE;
                    }
                    
                    Graphics2D g2d = (Graphics2D)g;
                    g2d.translate(pf.getImageableX(), pf.getImageableY());
                    
                    double scale = Math.min(
                        pf.getImageableWidth() / previewBox.getWidth(),
                        pf.getImageableHeight() / previewBox.getHeight()
                    );
                    g2d.scale(scale, scale);
                    
                    previewBox.printAll(g2d);
                    return Printable.PAGE_EXISTS;
                });
                
                job.print();
            }
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(window, 
                "Printing failed: " + e.getMessage(), 
                "Print Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clear(JTextField... fields) {
        for (JTextField f : fields) f.setText("");
    }

    private void addEdu() {
        if (degreeField.getText().isEmpty() || schoolBox.getText().isEmpty()) {
            JOptionPane.showMessageDialog(window, 
                "Degree and University fields are required", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String entry = String.format("%s - %s (%s)%s", 
            degreeField.getText(), 
            schoolBox.getText(), 
            yearFinished.getText().isEmpty() ? "Present" : yearFinished.getText(),
            gpaField.getText().isEmpty() ? "" : ", GPA: " + gpaField.getText());
            
        eduEntries.addElement(entry);
        clear(degreeField, schoolBox, yearFinished, gpaField);
    }

    private void removeEdu() {
        int idx = educationList.getSelectedIndex();
        if (idx >= 0) {
            eduEntries.remove(idx);
        } else {
            JOptionPane.showMessageDialog(window, 
                "Please select an item to remove", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addExp() {
        if (jobTitleField.getText().isEmpty() || companyField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(window, 
                "Job Title and Company fields are required", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String entry = String.format("%s at %s (%s): %s", 
            jobTitleField.getText(), 
            companyField.getText(), 
            yearsWorkedField.getText().isEmpty() ? "Present" : yearsWorkedField.getText(),
            responsibilitiesBox.getText());
            
        expData.addElement(entry);
        clear(jobTitleField, companyField, yearsWorkedField, responsibilitiesBox);
    }

    private void removeExp() {
        int idx = workList.getSelectedIndex();
        if (idx >= 0) {
            expData.remove(idx);
        } else {
            JOptionPane.showMessageDialog(window, 
                "Please select an item to remove", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addProj() {
        if (projectNameBox.getText().isEmpty()) {
            JOptionPane.showMessageDialog(window, 
                "Project name is required", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String entry = String.format("%s: %s [%s]", 
            projectNameBox.getText(), 
            projectSummary.getText(),
            techUsedField.getText());
            
        projEntries.addElement(entry);
        clear(projectNameBox, projectSummary, techUsedField);
    }

    private void removeProj() {
        int idx = projList.getSelectedIndex();
        if (idx >= 0) {
            projEntries.remove(idx);
        } else {
            JOptionPane.showMessageDialog(window, 
                "Please select an item to remove", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void addCert() {
        if (certNameInput.getText().isEmpty() || certOrgInput.getText().isEmpty()) {
            JOptionPane.showMessageDialog(window, 
                "Certificate name and organization are required", 
                "Missing Information", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String entry = String.format("%s from %s (%s)", 
            certNameInput.getText(), 
            certOrgInput.getText(),
            certYearInput.getText().isEmpty() ? "Present" : certYearInput.getText());
            
        certModel.addElement(entry);
        clear(certNameInput, certOrgInput, certYearInput);
    }

    private void removeCert() {
        int idx = certList.getSelectedIndex();
        if (idx >= 0) {
            certModel.remove(idx);
        } else {
            JOptionPane.showMessageDialog(window, 
                "Please select a certificate to remove", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    private void askSkill() {
        String newSkill = JOptionPane.showInputDialog(window, 
            "Enter a skill to add:", 
            "Add Skill", 
            JOptionPane.PLAIN_MESSAGE);
            
        if (newSkill != null && !newSkill.trim().isEmpty()) {
            skillModel.addElement(newSkill.trim());
        }
    }

    private void dropSkill() {
        int[] indices = skillsDisplay.getSelectedIndices();
        if (indices.length == 0) {
            JOptionPane.showMessageDialog(window, 
                "Please select skill(s) to remove", 
                "No Selection", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        for (int i = indices.length - 1; i >= 0; i--) {
            skillModel.remove(indices[i]);
        }
    }

    private void makePreview() {
        StringBuilder sb = new StringBuilder();
        
        String style = (String)styleDropdown.getSelectedItem();
        
        if ("Modern".equals(style)) {
            sb.append("╔══════════════════════════════════════╗\n");
            sb.append("║            RESUME - MODERN            ║\n");
            sb.append("╚══════════════════════════════════════╝\n\n");
        } else if ("Professional".equals(style)) {
            sb.append("========================================\n");
            sb.append("          PROFESSIONAL RESUME           \n");
            sb.append("========================================\n\n");
        } else if ("Creative".equals(style)) {
            sb.append("✦•······················•✦\n");
            sb.append("        CREATIVE RESUME     \n");
            sb.append("✦•······················•✦\n\n");
        } else {
            sb.append("----------------------------------------\n");
            sb.append("              RESUME                    \n");
            sb.append("----------------------------------------\n\n");
        }

        sb.append("CONTACT INFORMATION\n");
        sb.append("Name:    ").append(nameField.getText()).append("\n");
        sb.append("Email:   ").append(emailField.getText()).append("\n");
        sb.append("Phone:   ").append(contactField.getText()).append("\n");
        if (!addressBox.getText().isEmpty()) {
            sb.append("Address: ").append(addressBox.getText()).append("\n");
        }
        if (!linkedinLink.getText().isEmpty()) {
            sb.append("LinkedIn: ").append(linkedinLink.getText()).append("\n");
        }
        sb.append("\n");

        if (eduEntries.size() > 0) {
            sb.append("EDUCATION\n");
            for (int i = 0; i < eduEntries.size(); i++) {
                sb.append("• ").append(eduEntries.get(i)).append("\n");
            }
            sb.append("\n");
        }

        if (expData.size() > 0) {
            sb.append("WORK EXPERIENCE\n");
            for (int i = 0; i < expData.size(); i++) {
                sb.append("• ").append(expData.get(i)).append("\n");
            }
            sb.append("\n");
        }

        if (projEntries.size() > 0) {
            sb.append("PROJECTS\n");
            for (int i = 0; i < projEntries.size(); i++) {
                sb.append("• ").append(projEntries.get(i)).append("\n");
            }
            sb.append("\n");
        }

        if (certModel.size() > 0) {
            sb.append("CERTIFICATES\n");
            for (int i = 0; i < certModel.size(); i++) {
                sb.append("• ").append(certModel.get(i)).append("\n");
            }
            sb.append("\n");
        }

        if (skillModel.size() > 0) {
            sb.append("SKILLS (").append(jobRoleBox.getSelectedItem()).append(")\n");
            for (int i = 0; i < skillModel.size(); i++) {
                sb.append("• ").append(skillModel.get(i));
                if (i < skillModel.size() - 1) sb.append(", ");
                if (i > 0 && i % 5 == 0) sb.append("\n");
            }
            sb.append("\n");
        }

        previewBox.setText(sb.toString());
    }

    private void writeOut() {
        JFileChooser saver = new JFileChooser();
        saver.setDialogTitle("Save Resume As...");
        
        String defaultName = nameField.getText().replaceAll("\\s+", "_") + "_Resume.txt";
        saver.setSelectedFile(new java.io.File(defaultName));
        
        int choice = saver.showSaveDialog(window);

        if (choice == JFileChooser.APPROVE_OPTION) {
            try {
                java.io.File outFile = saver.getSelectedFile();
                if (!outFile.getName().toLowerCase().endsWith(".txt")) {
                    outFile = new java.io.File(outFile.getAbsolutePath() + ".txt");
                }

                try (java.io.PrintWriter writer = new java.io.PrintWriter(outFile)) {
                    writer.println(previewBox.getText());
                }

                JOptionPane.showMessageDialog(window, 
                    "Resume saved successfully to:\n" + outFile.getAbsolutePath(), 
                    "Save Complete", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(window, 
                    "Error saving file: " + e.getMessage(), 
                    "Save Failed", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}