
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.YearMonth;
import java.util.List;
import java.util.Locale;

public class PayrollGUI extends JFrame {

    private final EmployeeDAO employeeDAO = new EmployeeDAO();
    private final PayrollDAO payrollDAO = new PayrollDAO();

    private final Color NAVY = new Color(18, 30, 55);
    private final Color BLUE = new Color(52, 105, 230);
    private final Color BG = new Color(245, 247, 251);
    private final Color WHITE = Color.WHITE;
    private final Color TEXT = new Color(35, 48, 70);
    private final Color MUTED = new Color(120, 132, 151);
    private final Color BORDER = new Color(229, 234, 242);
    private final Color TEAL = new Color(20, 155, 133);
    private final Color PURPLE = new Color(128, 91, 218);
    private final Color ORANGE = new Color(226, 147, 58);

    private final CardLayout pageLayout = new CardLayout();
    private final JPanel pageContainer = new JPanel(pageLayout);
    private final JLabel pageTitle = new JLabel("Dashboard Overview");
    private final JLabel welcomeSubtitle = new JLabel(
            "Your workforce and payroll, all in one place");
    private final JLabel employeeValue = new JLabel("—");
    private final JLabel payrollValue = new JLabel("—");
    private final JLabel grossValue = new JLabel("—");
    private final JLabel netValue = new JLabel("—");
    private final JLabel statusLabel = new JLabel("●  SYSTEM READY");
    private final NumberFormat currency =
            NumberFormat.getCurrencyInstance(Locale.forLanguageTag("en-IN"));

    private JButton dashboardNav;
    private JButton employeesNav;
    private JButton payrollNav;
    private JButton reportsNav;

    public PayrollGUI() {
        setTitle("PayFlow Pro | Employee Payroll Management");
        setSize(1280, 800);
        setMinimumSize(new Dimension(1000, 680));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            // Keep the default Swing look and feel.
        }

        UIManager.put("Button.focus", new Color(0, 0, 0, 0));
        UIManager.put("Component.arc", 12);

        buildApplication();
        refreshStatistics();
    }

    private void buildApplication() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(BG);

        root.add(buildSidebar(), BorderLayout.WEST);
        root.add(buildWorkspace(), BorderLayout.CENTER);

        setContentPane(root);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setPreferredSize(new Dimension(238, 0));
        sidebar.setBackground(NAVY);
        sidebar.setBorder(new EmptyBorder(24, 17, 18, 17));

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.setBorder(new EmptyBorder(7, 8, 36, 0));

        JLabel logo = new JLabel("PAYFLOW PRO");
        logo.setFont(new Font("SansSerif", Font.BOLD, 23));
        logo.setForeground(WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("HR & PAYROLL");
        tagline.setFont(new Font("SansSerif", Font.PLAIN, 11));
        tagline.setForeground(new Color(163, 180, 207));
        tagline.setBorder(new EmptyBorder(8, 1, 0, 0));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        brand.add(logo);
        brand.add(tagline);
        sidebar.add(brand, BorderLayout.NORTH);

        JPanel navigation = new JPanel();
        navigation.setOpaque(false);
        navigation.setLayout(new BoxLayout(navigation, BoxLayout.Y_AXIS));

        JLabel section = new JLabel("WORKSPACE");
        section.setFont(new Font("SansSerif", Font.BOLD, 10));
        section.setForeground(new Color(139, 158, 190));
        section.setBorder(new EmptyBorder(0, 9, 12, 0));
        section.setAlignmentX(Component.LEFT_ALIGNMENT);

        navigation.add(section);

        dashboardNav = navButton("▦   Dashboard", true);
        employeesNav = navButton("♙   Employee Records", false);
        payrollNav = navButton("▤   Payroll Generation", false);
        reportsNav = navButton("▥   Salary Reports", false);

        navigation.add(dashboardNav);
        navigation.add(Box.createVerticalStrut(7));
        navigation.add(employeesNav);
        navigation.add(Box.createVerticalStrut(7));
        navigation.add(payrollNav);
        navigation.add(Box.createVerticalStrut(7));
        navigation.add(reportsNav);

        dashboardNav.addActionListener(e -> showDashboard());
        employeesNav.addActionListener(e -> showEmployeesPage());
        payrollNav.addActionListener(e -> showPayrollForm());
        reportsNav.addActionListener(e -> showReportsPage());

        sidebar.add(navigation, BorderLayout.CENTER);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(new EmptyBorder(15, 7, 4, 0));

        JLabel divider = new JLabel("────────────────────");
        divider.setForeground(new Color(58, 74, 105));

        JLabel product = new JLabel("PayFlow Pro Desktop");
        product.setFont(new Font("SansSerif", Font.BOLD, 11));
        product.setForeground(new Color(213, 223, 239));
        product.setBorder(new EmptyBorder(15, 0, 5, 0));

        JLabel technology = new JLabel("Java Swing  •  MySQL");
        technology.setFont(new Font("SansSerif", Font.PLAIN, 10));
        technology.setForeground(new Color(143, 161, 190));

        bottom.add(divider);
        bottom.add(product);
        bottom.add(technology);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JButton navButton(String text, boolean active) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFont(new Font("SansSerif", Font.PLAIN, 12));
        button.setForeground(active ? WHITE : new Color(202, 213, 231));
        button.setBackground(active ? BLUE : NAVY);
        button.setBorder(new EmptyBorder(14, 13, 14, 8));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 47));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void setActiveNav(JButton selected) {
        JButton[] buttons = {
                dashboardNav, employeesNav, payrollNav, reportsNav
        };

        for (JButton button : buttons) {
            boolean active = button == selected;
            button.setBackground(active ? BLUE : NAVY);
            button.setForeground(
                    active ? WHITE : new Color(202, 213, 231));
        }
    }

    private JPanel buildWorkspace() {
        JPanel workspace = new JPanel(new BorderLayout());
        workspace.setBackground(BG);
        workspace.setBorder(new EmptyBorder(0, 30, 0, 30));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(25, 0, 22, 0));

        JPanel headingGroup = new JPanel();
        headingGroup.setOpaque(false);
        headingGroup.setLayout(new BoxLayout(headingGroup, BoxLayout.Y_AXIS));

        pageTitle.setFont(new Font("SansSerif", Font.BOLD, 27));
        pageTitle.setForeground(TEXT);

        welcomeSubtitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        welcomeSubtitle.setForeground(MUTED);
        welcomeSubtitle.setBorder(new EmptyBorder(7, 1, 0, 0));

        headingGroup.add(pageTitle);
        headingGroup.add(welcomeSubtitle);

        statusLabel.setOpaque(true);
        statusLabel.setBackground(new Color(224, 247, 240));
        statusLabel.setForeground(new Color(22, 128, 103));
        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 10));
        statusLabel.setBorder(new EmptyBorder(13, 15, 13, 15));

        header.add(headingGroup, BorderLayout.WEST);
        header.add(statusLabel, BorderLayout.EAST);

        pageContainer.setOpaque(false);
        pageContainer.add(buildDashboard(), "dashboard");
        pageContainer.add(buildEmployeesPage(), "employees");
        pageContainer.add(buildReportsPage(), "reports");

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(13, 0, 13, 0));

        JLabel footerText = new JLabel(
                "PAYFLOW PRO    |    Employee Payroll Management");
        footerText.setFont(new Font("SansSerif", Font.PLAIN, 10));
        footerText.setForeground(MUTED);

        JLabel footerTech = new JLabel("JAVA  •  MYSQL");
        footerTech.setFont(new Font("SansSerif", Font.BOLD, 9));
        footerTech.setForeground(MUTED);

        footer.add(footerText, BorderLayout.WEST);
        footer.add(footerTech, BorderLayout.EAST);

        workspace.add(header, BorderLayout.NORTH);
        workspace.add(pageContainer, BorderLayout.CENTER);
        workspace.add(footer, BorderLayout.SOUTH);

        return workspace;
    }

    private JPanel buildDashboard() {
        JPanel dashboard = new JPanel(new BorderLayout(0, 21));
        dashboard.setOpaque(false);

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        JPanel stats = new JPanel(new GridLayout(1, 4, 14, 14));
        stats.setOpaque(false);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 132));

        stats.add(statCard("TOTAL EMPLOYEES", employeeValue,
                "Registered workforce", BLUE, "♙"));
        stats.add(statCard("PAYROLL RECORDS", payrollValue,
                "Saved payroll entries", TEAL, "▤"));
        stats.add(statCard("MONTHLY GROSS", grossValue,
                "Selected current month", PURPLE, "↗"));
        stats.add(statCard("MONTHLY NET", netValue,
                "After deductions", ORANGE, "₹"));

        content.add(stats);
        content.add(Box.createVerticalStrut(24));

        JPanel sectionHeader = new JPanel(new BorderLayout());
        sectionHeader.setOpaque(false);
        sectionHeader.setMaximumSize(
                new Dimension(Integer.MAX_VALUE, 34));

        JLabel quickTitle = new JLabel("Quick Actions");
        quickTitle.setFont(new Font("SansSerif", Font.BOLD, 19));
        quickTitle.setForeground(TEXT);

        JLabel quickSub = new JLabel("Manage your daily payroll tasks");
        quickSub.setFont(new Font("SansSerif", Font.PLAIN, 11));
        quickSub.setForeground(MUTED);

        sectionHeader.add(quickTitle, BorderLayout.WEST);
        sectionHeader.add(quickSub, BorderLayout.EAST);

        content.add(sectionHeader);
        content.add(Box.createVerticalStrut(13));

        JPanel actions = new JPanel(new GridLayout(2, 2, 15, 15));
        actions.setOpaque(false);

        actions.add(actionCard("01", "Employee Registration",
                "Add a new employee and save their details.",
                BLUE, "REGISTER EMPLOYEE", this::showEmployeeForm));

        actions.add(actionCard("02", "Employee Records",
                "Browse, search and sort registered employees.",
                TEAL, "VIEW EMPLOYEES", this::showEmployeesPage));

        actions.add(actionCard("03", "Payroll Generation",
                "Generate a monthly payroll entry for an employee.",
                PURPLE, "GENERATE PAYROLL", this::showPayrollForm));

        actions.add(actionCard("04", "Salary Reports",
                "Review saved payroll and salary information.",
                ORANGE, "OPEN REPORTS", this::showReportsPage));

        content.add(actions);
        dashboard.add(content, BorderLayout.NORTH);

        return dashboard;
    }

    private JPanel statCard(String heading, JLabel value,
                            String description, Color accent, String symbol) {
        JPanel card = new JPanel(new BorderLayout(8, 5));
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(15, 15, 14, 13)));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel label = new JLabel(heading);
        label.setFont(new Font("SansSerif", Font.BOLD, 9));
        label.setForeground(MUTED);

        JLabel icon = new JLabel(symbol, SwingConstants.CENTER);
        icon.setOpaque(true);
        icon.setBackground(new Color(
                accent.getRed(), accent.getGreen(), accent.getBlue(), 24));
        icon.setForeground(accent);
        icon.setFont(new Font("SansSerif", Font.BOLD, 16));
        icon.setPreferredSize(new Dimension(34, 32));

        top.add(label, BorderLayout.WEST);
        top.add(icon, BorderLayout.EAST);

        value.setFont(new Font("SansSerif", Font.BOLD, 22));
        value.setForeground(TEXT);

        JLabel detail = new JLabel(description);
        detail.setFont(new Font("SansSerif", Font.PLAIN, 10));
        detail.setForeground(MUTED);

        card.add(top, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        card.add(detail, BorderLayout.SOUTH);

        return card;
    }

    private JPanel actionCard(String number, String title,
                              String description, Color accent,
                              String buttonText, Runnable action) {
        JPanel card = new JPanel(new BorderLayout(10, 12));
        card.setBackground(WHITE);
        card.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(17, 18, 16, 18)));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        JLabel numberLabel = new JLabel(number);
        numberLabel.setFont(new Font("SansSerif", Font.BOLD, 11));
        numberLabel.setForeground(accent);

        JPanel accentMark = new JPanel();
        accentMark.setBackground(accent);
        accentMark.setPreferredSize(new Dimension(30, 4));

        top.add(numberLabel, BorderLayout.WEST);
        top.add(accentMark, BorderLayout.EAST);

        JPanel middle = new JPanel();
        middle.setOpaque(false);
        middle.setLayout(new BoxLayout(middle, BoxLayout.Y_AXIS));

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 15));
        heading.setForeground(TEXT);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel detail = new JLabel(
                "<html><div style='width:270px'>"
                        + description + "</div></html>");
        detail.setFont(new Font("SansSerif", Font.PLAIN, 11));
        detail.setForeground(MUTED);
        detail.setBorder(new EmptyBorder(7, 0, 0, 0));
        detail.setAlignmentX(Component.LEFT_ALIGNMENT);

        middle.add(heading);
        middle.add(detail);

        JButton button = new JButton(buttonText + "   →");
        button.setFont(new Font("SansSerif", Font.BOLD, 10));
        button.setForeground(accent);
        button.setBackground(WHITE);
        button.setBorder(new CompoundBorder(
                new LineBorder(new Color(
                        accent.getRed(), accent.getGreen(),
                        accent.getBlue(), 90), 1, true),
                new EmptyBorder(9, 12, 9, 12)));
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> action.run());

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonRow.setOpaque(false);
        buttonRow.add(button);

        card.add(top, BorderLayout.NORTH);
        card.add(middle, BorderLayout.CENTER);
        card.add(buttonRow, BorderLayout.SOUTH);

        return card;
    }

    private void refreshStatistics() {
        try {
            employeeValue.setText(
                    String.valueOf(employeeDAO.getEmployeeCount()));
            payrollValue.setText(
                    String.valueOf(payrollDAO.getPayrollCount()));

            String month = YearMonth.now().toString();
            BigDecimal gross = payrollDAO.getMonthlyGrossPayroll(month);
            BigDecimal net = payrollDAO.getMonthlyNetPayroll(month);

            grossValue.setText(compactCurrency(gross));
            netValue.setText(compactCurrency(net));

            statusLabel.setText("●  SYSTEM READY");
        } catch (Exception ex) {
            employeeValue.setText("—");
            payrollValue.setText("—");
            grossValue.setText("—");
            netValue.setText("—");
            statusLabel.setText("●  CHECK DATABASE");
            statusLabel.setBackground(new Color(255, 239, 220));
            statusLabel.setForeground(new Color(160, 91, 25));
            System.err.println(
                    "Dashboard statistics could not be refreshed: "
                            + ex.getMessage());
        }
    }

    private String compactCurrency(BigDecimal amount) {
        if (amount == null) {
            return currency.format(0);
        }
        return currency.format(amount);
    }

    private void showDashboard() {
        pageTitle.setText("Dashboard Overview");
        welcomeSubtitle.setText(
                "Your workforce and payroll, all in one place");
        setActiveNav(dashboardNav);
        refreshStatistics();
        pageLayout.show(pageContainer, "dashboard");
    }

    private void showEmployeesPage() {
        pageTitle.setText("Employee Directory");
        welcomeSubtitle.setText(
                "Search and review your registered workforce");
        setActiveNav(employeesNav);
        refreshEmployeesPage();
        pageLayout.show(pageContainer, "employees");
    }

    private void showReportsPage() {
        pageTitle.setText("Salary Reports");
        welcomeSubtitle.setText(
                "Review payroll entries saved in your database");
        setActiveNav(reportsNav);
        refreshReportsPage();
        pageLayout.show(pageContainer, "reports");
    }

    private JPanel buildEmployeesPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);

        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);

        JLabel caption = new JLabel("EMPLOYEE DIRECTORY");
        caption.setFont(new Font("SansSerif", Font.BOLD, 11));
        caption.setForeground(MUTED);

        JTextField search = new JTextField();
        search.setFont(new Font("SansSerif", Font.PLAIN, 12));
        search.setPreferredSize(new Dimension(260, 38));
        search.setToolTipText("Search employees by name, department or ID");

        JButton addButton = primaryButton("+  Add Employee");
        addButton.addActionListener(e -> showEmployeeForm());

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 9, 0));
        right.setOpaque(false);
        right.add(search);
        right.add(addButton);

        toolbar.add(caption, BorderLayout.WEST);
        toolbar.add(right, BorderLayout.EAST);

        String[] columns = {
                "ID", "Employee Name", "Department",
                "Designation", "Basic Salary"
        };

        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = createStyledTable(model);
        JScrollPane scroll = new JScrollPane(table);
        styleScrollPane(scroll);

        search.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
                    private void filter() {
                        javax.swing.RowFilter<DefaultTableModel, Object> rf =
                                search.getText().trim().isEmpty()
                                ? null
                                : javax.swing.RowFilter.regexFilter(
                                        "(?i)" + java.util.regex.Pattern.quote(
                                                search.getText().trim()));
                        javax.swing.table.TableRowSorter<DefaultTableModel>
                                sorter = new javax.swing.table.TableRowSorter<>(
                                        model);
                        table.setRowSorter(sorter);
                        sorter.setRowFilter(rf);
                    }

                    @Override
                    public void insertUpdate(
                            javax.swing.event.DocumentEvent e) {
                        filter();
                    }

                    @Override
                    public void removeUpdate(
                            javax.swing.event.DocumentEvent e) {
                        filter();
                    }

                    @Override
                    public void changedUpdate(
                            javax.swing.event.DocumentEvent e) {
                        filter();
                    }
                });

        JPanel tableCard = new JPanel(new BorderLayout());
        tableCard.setBackground(WHITE);
        tableCard.setBorder(new LineBorder(BORDER, 1, true));
        tableCard.add(scroll, BorderLayout.CENTER);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(tableCard, BorderLayout.CENTER);

        panel.putClientProperty("employeeModel", model);

        return panel;
    }

    private void refreshEmployeesPage() {
        JPanel page = (JPanel) pageContainer.getComponent(1);
        DefaultTableModel model =
                (DefaultTableModel) page.getClientProperty("employeeModel");
        if (model == null) {
            return;
        }

        model.setRowCount(0);
        List<Employee> employees = employeeDAO.getAllEmployees();

        for (Employee employee : employees) {
            model.addRow(new Object[]{
                    employee.getEmployeeId(),
                    employee.getEmployeeName(),
                    employee.getDepartment(),
                    employee.getDesignation(),
                    currency.format(employee.getBasicSalary())
            });
        }
        refreshStatistics();
    }

    private JPanel buildReportsPage() {
        JPanel panel = new JPanel(new BorderLayout(0, 14));
        panel.setOpaque(false);

        JPanel toolbar = new JPanel(new BorderLayout());
        toolbar.setOpaque(false);

        JLabel caption = new JLabel("PAYROLL HISTORY");
        caption.setFont(new Font("SansSerif", Font.BOLD, 11));
        caption.setForeground(MUTED);

        JButton refresh = secondaryButton("↻  Refresh Report");
        refresh.addActionListener(e -> refreshReportsPage());

        toolbar.add(caption, BorderLayout.WEST);
        toolbar.add(refresh, BorderLayout.EAST);

        JTextArea report = new JTextArea();
        report.setEditable(false);
        report.setFont(new Font("Monospaced", Font.PLAIN, 12));
        report.setForeground(TEXT);
        report.setBackground(WHITE);
        report.setBorder(new EmptyBorder(18, 18, 18, 18));
        report.setLineWrap(false);

        JScrollPane scroll = new JScrollPane(report);
        styleScrollPane(scroll);

        JPanel reportCard = new JPanel(new BorderLayout());
        reportCard.setBackground(WHITE);
        reportCard.setBorder(new LineBorder(BORDER, 1, true));
        reportCard.add(scroll, BorderLayout.CENTER);

        panel.add(toolbar, BorderLayout.NORTH);
        panel.add(reportCard, BorderLayout.CENTER);
        panel.putClientProperty("reportText", report);

        return panel;
    }

    private void refreshReportsPage() {
        JPanel page = (JPanel) pageContainer.getComponent(2);
        JTextArea report = (JTextArea) page.getClientProperty("reportText");
        if (report == null) {
            return;
        }

        java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();
        java.io.PrintStream original = System.out;

        try (java.io.PrintStream capture =
                     new java.io.PrintStream(output)) {
            System.setOut(capture);
            payrollDAO.viewPayroll();
        } catch (Exception ex) {
            report.setText("Unable to retrieve payroll report.\n"
                    + ex.getMessage());
            return;
        } finally {
            System.setOut(original);
        }

        String text = output.toString();
        report.setText(text.trim().isEmpty()
                ? "No payroll report data was returned."
                : text);
        report.setCaretPosition(0);
        refreshStatistics();
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(38);
        table.setFont(new Font("SansSerif", Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setBackground(WHITE);
        table.setSelectionBackground(new Color(226, 237, 255));
        table.setSelectionForeground(TEXT);
        table.setGridColor(BORDER);
        table.setShowVerticalLines(false);
        table.setIntercellSpacing(new Dimension(0, 1));
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);

        table.getTableHeader().setFont(
                new Font("SansSerif", Font.BOLD, 11));
        table.getTableHeader().setForeground(MUTED);
        table.getTableHeader().setBackground(
                new Color(247, 249, 252));
        table.getTableHeader().setPreferredSize(new Dimension(0, 40));
        table.getTableHeader().setBorder(
                new MatteBorder(0, 0, 1, 0, BORDER));

        return table;
    }

    private void styleScrollPane(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(WHITE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
    }

    private JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setForeground(WHITE);
        button.setBackground(BLUE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(12, 15, 12, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 11));
        button.setForeground(TEXT);
        button.setBackground(WHITE);
        button.setFocusPainted(false);
        button.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(10, 13, 10, 13)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void showEmployeeForm() {
        JTextField name = new JTextField(20);
        JTextField department = new JTextField(20);
        JTextField designation = new JTextField(20);
        JTextField salary = new JTextField(20);

        styleInput(name);
        styleInput(department);
        styleInput(designation);
        styleInput(salary);

        JPanel form = new JPanel(new GridLayout(4, 2, 12, 15));
        form.setBorder(new EmptyBorder(15, 12, 15, 12));
        form.setBackground(WHITE);

        addFormField(form, "Employee Name", name);
        addFormField(form, "Department", department);
        addFormField(form, "Designation", designation);
        addFormField(form, "Basic Salary (₹)", salary);

        int result = JOptionPane.showConfirmDialog(
                this, form, "Register New Employee",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try {
            String employeeName = name.getText().trim();
            String employeeDepartment = department.getText().trim();
            String employeeDesignation = designation.getText().trim();
            double basicSalary = Double.parseDouble(salary.getText().trim());

            if (employeeName.isEmpty()
                    || employeeDepartment.isEmpty()
                    || employeeDesignation.isEmpty()
                    || !Double.isFinite(basicSalary)
                    || basicSalary <= 0) {
                showError("Enter all employee details and a positive salary.");
                return;
            }

            Employee employee = new Employee(
                    employeeName, employeeDepartment,
                    employeeDesignation, basicSalary);

            if (employeeDAO.addEmployee(employee)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Employee registered successfully.",
                        "Registration Complete",
                        JOptionPane.INFORMATION_MESSAGE);
                refreshStatistics();
                if (pageLayout != null) {
                    refreshEmployeesPage();
                }
            } else {
                showError("Employee registration failed. Check the terminal.");
            }
        } catch (NumberFormatException ex) {
            showError("Basic salary must be a valid number.");
        }
    }

    private void addFormField(JPanel panel, String label, JTextField field) {
        JLabel title = new JLabel(label);
        title.setFont(new Font("SansSerif", Font.BOLD, 12));
        title.setForeground(TEXT);
        panel.add(title);
        panel.add(field);
    }

    private void styleInput(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 13));
        field.setBorder(new CompoundBorder(
                new LineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
    }

    private void showPayrollForm() {
        setActiveNav(payrollNav);

        List<Employee> employees = employeeDAO.getAllEmployees();
        if (employees.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Please register an employee first.",
                    "No Employees", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JComboBox<Employee> employeeBox = new JComboBox<>(
                employees.toArray(new Employee[0]));

        employeeBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                if (value instanceof Employee) {
                    Employee employee = (Employee) value;
                    setText(employee.getEmployeeId() + "  •  "
                            + employee.getEmployeeName());
                }
                return this;
            }
        });

        JTextField monthField =
                new JTextField(YearMonth.now().toString(), 12);
        styleInput(monthField);

        JPanel panel = new JPanel(new GridLayout(0, 2, 12, 15));
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(15, 12, 15, 12));

        panel.add(new JLabel("Select Employee"));
        panel.add(employeeBox);
        panel.add(new JLabel("Payroll Month (YYYY-MM)"));
        panel.add(monthField);

        int choice = JOptionPane.showConfirmDialog(
                this, panel, "Generate Monthly Payroll",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (choice != JOptionPane.OK_OPTION) {
            return;
        }

        Employee selected = (Employee) employeeBox.getSelectedItem();
        String month = monthField.getText().trim();

        if (selected == null
                || !month.matches("\\d{4}-(0[1-9]|1[0-2])")) {
            showError("Select an employee and enter a valid month (YYYY-MM).");
            return;
        }

        boolean success = payrollDAO.generatePayroll(
                selected.getEmployeeId(),
                month,
                selected.getBasicSalary());

        if (success) {
            JOptionPane.showMessageDialog(
                    this,
                    "Payroll generated for " + selected.getEmployeeName()
                            + "\nMonth: " + month,
                    "Payroll Generated",
                    JOptionPane.INFORMATION_MESSAGE);
            refreshStatistics();
        } else {
            showError("Payroll generation failed. Check the terminal.");
        }
    }

    

    private void showError(String message) {
        JOptionPane.showMessageDialog(
                this, message, "PayFlow Pro",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PayrollGUI gui = new PayrollGUI();
            gui.setVisible(true);
        });
    }
}