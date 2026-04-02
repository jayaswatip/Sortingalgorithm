import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.Random;

@FunctionalInterface
interface Sorter {
    void sort() throws InterruptedException;
}

public class VisualPanel extends JPanel {
    public int[] array;
    public Color[] colors;
    public int speed = 30;
    public int arraySize = 80;
    
    // Statistics
    public int comparisons = 0;
    public int swaps = 0;
    public long startTime = 0;
    public boolean isSorting = false;
    
    // UI Components
    private JLabel statusLabel;
    private JLabel comparisonLabel;
    private JLabel swapLabel;
    private JLabel timeLabel;
    private JTextArea infoArea;
    
    // Color scheme
    private final Color BACKGROUND = new Color(240, 242, 245);
    private final Color PRIMARY = new Color(52, 152, 219);
    private final Color SUCCESS = new Color(46, 204, 113);
    private final Color WARNING = new Color(241, 196, 15);
    private final Color DANGER = new Color(231, 76, 60);
    private final Color SORTED = new Color(155, 89, 182);
    private final Color PANEL_BG = Color.WHITE;
    
    public VisualPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(BACKGROUND);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        generateArray();
        
        // Top Panel - Title and Controls
        add(createTopPanel(), BorderLayout.NORTH);
        
        // Center Panel - Visualization
        add(createVisualizationPanel(), BorderLayout.CENTER);
        
        // Right Panel - Info and Stats
        add(createInfoPanel(), BorderLayout.EAST);
        
        // Bottom Panel - Algorithm Buttons
        add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(BACKGROUND);
        
        // Title
        JLabel titleLabel = new JLabel("🎯 Sorting Algorithm Visualizer", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        
        // Control Panel with sliders
        JPanel controlPanel = new JPanel(new GridLayout(2, 4, 15, 8));
        controlPanel.setBackground(PANEL_BG);
        controlPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        
        // Speed Slider
        JLabel speedLabel = new JLabel("⚡ Speed", JLabel.CENTER);
        speedLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JSlider speedSlider = new JSlider(1, 100, speed);
        speedSlider.setBackground(PANEL_BG);
        styleSlider(speedSlider);
        
        // Size Slider
        JLabel sizeLabel = new JLabel("📊 Array Size", JLabel.CENTER);
        sizeLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        JSlider sizeSlider = new JSlider(10, 200, arraySize);
        sizeSlider.setBackground(PANEL_BG);
        styleSlider(sizeSlider);
        
        controlPanel.add(speedLabel);
        controlPanel.add(speedSlider);
        controlPanel.add(sizeLabel);
        controlPanel.add(sizeSlider);
        
        // Status labels
        statusLabel = new JLabel("Ready to sort!", JLabel.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        statusLabel.setForeground(SUCCESS);
        
        comparisonLabel = new JLabel("Comparisons: 0", JLabel.CENTER);
        comparisonLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        swapLabel = new JLabel("Swaps: 0", JLabel.CENTER);
        swapLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        timeLabel = new JLabel("Time: 0.00s", JLabel.CENTER);
        timeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        controlPanel.add(statusLabel);
        controlPanel.add(comparisonLabel);
        controlPanel.add(swapLabel);
        controlPanel.add(timeLabel);
        
        // Slider listeners
        speedSlider.addChangeListener(e -> {
            speed = 101 - speedSlider.getValue(); // Invert so right = faster
        });
        
        sizeSlider.addChangeListener(e -> {
            if (!isSorting) {
                arraySize = sizeSlider.getValue();
                generateArray();
                repaint();
            }
        });
        
        topPanel.add(titleLabel, BorderLayout.NORTH);
        topPanel.add(controlPanel, BorderLayout.CENTER);
        
        return topPanel;
    }
    
    private JPanel createVisualizationPanel() {
        JPanel vizPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int barWidth = Math.max(1, (width - 20) / array.length);
                int gap = Math.max(1, barWidth / 10);
                
                for (int i = 0; i < array.length; i++) {
                    int barHeight = (int)((array[i] / 320.0) * (height - 40));
                    int x = 10 + i * barWidth;
                    int y = height - barHeight - 10;
                    
                    // Draw shadow
                    g2d.setColor(new Color(0, 0, 0, 20));
                    g2d.fillRoundRect(x + 2, y + 2, barWidth - gap, barHeight, 4, 4);
                    
                    // Draw bar
                    g2d.setColor(colors[i]);
                    g2d.fillRoundRect(x, y, barWidth - gap, barHeight, 4, 4);
                    
                    // Draw border
                    g2d.setColor(colors[i].darker());
                    g2d.drawRoundRect(x, y, barWidth - gap, barHeight, 4, 4);
                }
            }
        };
        
        vizPanel.setBackground(PANEL_BG);
        vizPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        return vizPanel;
    }
    
    private JPanel createInfoPanel() {
        JPanel infoPanel = new JPanel(new BorderLayout(5, 5));
        infoPanel.setPreferredSize(new Dimension(300, 0));
        infoPanel.setBackground(BACKGROUND);
        
        JLabel infoTitle = new JLabel("📚 Algorithm Info", JLabel.CENTER);
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoTitle.setForeground(new Color(44, 62, 80));
        infoTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        infoArea = new JTextArea();
        infoArea.setEditable(false);
        infoArea.setLineWrap(true);
        infoArea.setWrapStyleWord(true);
        infoArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        infoArea.setBackground(PANEL_BG);
        infoArea.setBorder(new EmptyBorder(15, 15, 15, 15));
        infoArea.setText(getWelcomeText());
        
        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setBorder(new LineBorder(new Color(189, 195, 199), 1, true));
        
        // Legend Panel
        JPanel legendPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        legendPanel.setBackground(PANEL_BG);
        legendPanel.setBorder(new CompoundBorder(
            new LineBorder(new Color(189, 195, 199), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        addLegendItem(legendPanel, PRIMARY, "Unsorted");
        addLegendItem(legendPanel, DANGER, "Comparing");
        addLegendItem(legendPanel, SUCCESS, "Selected/Pivot");
        addLegendItem(legendPanel, WARNING, "Swapping");
        addLegendItem(legendPanel, SORTED, "Sorted");
        
        infoPanel.add(infoTitle, BorderLayout.NORTH);
        infoPanel.add(scrollPane, BorderLayout.CENTER);
        infoPanel.add(legendPanel, BorderLayout.SOUTH);
        
        return infoPanel;
    }
    
    private void addLegendItem(JPanel panel, Color color, String text) {
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        colorBox.setBorder(new LineBorder(color.darker(), 1));
        
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        panel.add(colorBox);
        panel.add(label);
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        buttonPanel.setBackground(BACKGROUND);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        
        // Create styled buttons
        JButton bubbleBtn = createStyledButton("🔵 Bubble Sort", new Color(52, 152, 219));
        JButton selectionBtn = createStyledButton("🟢 Selection Sort", new Color(46, 204, 113));
        JButton insertionBtn = createStyledButton("🟡 Insertion Sort", new Color(241, 196, 15));
        JButton mergeBtn = createStyledButton("🟣 Merge Sort", new Color(155, 89, 182));
        JButton quickBtn = createStyledButton("🔴 Quick Sort", new Color(231, 76, 60));
        JButton resetBtn = createStyledButton("🔄 Reset", new Color(52, 73, 94));
        
        buttonPanel.add(bubbleBtn);
        buttonPanel.add(selectionBtn);
        buttonPanel.add(insertionBtn);
        buttonPanel.add(mergeBtn);
        buttonPanel.add(quickBtn);
        buttonPanel.add(resetBtn);
        
        // Button actions
        bubbleBtn.addActionListener(e -> {
            infoArea.setText(getBubbleSortInfo());
            run(() -> SortingAlgorithms.bubbleSort(array, this));
        });
        
        selectionBtn.addActionListener(e -> {
            infoArea.setText(getSelectionSortInfo());
            run(() -> SortingAlgorithms.selectionSort(array, this));
        });
        
        insertionBtn.addActionListener(e -> {
            infoArea.setText(getInsertionSortInfo());
            run(() -> SortingAlgorithms.insertionSort(array, this));
        });
        
        mergeBtn.addActionListener(e -> {
            infoArea.setText(getMergeSortInfo());
            run(() -> SortingAlgorithms.mergeSort(array, 0, array.length - 1, this));
        });
        
        quickBtn.addActionListener(e -> {
            infoArea.setText(getQuickSortInfo());
            run(() -> SortingAlgorithms.quickSort(array, 0, array.length - 1, this));
        });
        
        resetBtn.addActionListener(e -> {
            if (!isSorting) {
                generateArray();
                resetStats();
                infoArea.setText(getWelcomeText());
                repaint();
            }
        });
        
        return buttonPanel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(150, 50));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void styleSlider(JSlider slider) {
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(20);
        slider.setFont(new Font("Segoe UI", Font.PLAIN, 10));
    }
    
    private void run(Sorter sorter) {
        if (isSorting) return;
        
        new Thread(() -> {
            try {
                isSorting = true;
                resetStats();
                startTime = System.nanoTime();
                statusLabel.setText("⚙️ Sorting...");
                statusLabel.setForeground(WARNING);
                
                sorter.sort();
                
                statusLabel.setText("✅ Sorted!");
                statusLabel.setForeground(SUCCESS);
                isSorting = false;
            } catch (InterruptedException ex) {
                ex.printStackTrace();
                isSorting = false;
            }
        }).start();
    }
    
    public void generateArray() {
        array = new int[arraySize];
        colors = new Color[arraySize];
        Random rand = new Random();
        for (int i = 0; i < arraySize; i++) {
            array[i] = rand.nextInt(280) + 20;
            colors[i] = PRIMARY;
        }
    }
    
    public void resetColors() {
        for (int i = 0; i < colors.length; i++) {
            if (!colors[i].equals(SORTED)) {
                colors[i] = PRIMARY;
            }
        }
    }
    
    public void markSorted(int index) {
        if (index >= 0 && index < colors.length) {
            colors[index] = SORTED;
        }
    }
    
    public void resetStats() {
        comparisons = 0;
        swaps = 0;
        startTime = 0;
        updateStats();
    }
    
    public void updateStats() {
        SwingUtilities.invokeLater(() -> {
            comparisonLabel.setText("Comparisons: " + comparisons);
            swapLabel.setText("Swaps: " + swaps);
            if (startTime > 0) {
                double elapsed = (System.nanoTime() - startTime) / 1_000_000_000.0;
                timeLabel.setText(String.format("Time: %.2fs", elapsed));
            }
        });
    }
    
    // Info text methods
    private String getWelcomeText() {
        return "Welcome to the Sorting Algorithm Visualizer!\n\n" +
               "Select any sorting algorithm to see how it works in real-time.\n\n" +
               "Features:\n" +
               "• Visual step-by-step sorting\n" +
               "• Real-time statistics\n" +
               "• Adjustable speed & array size\n" +
               "• Color-coded operations\n\n" +
               "Choose an algorithm to begin!";
    }
    
    private String getBubbleSortInfo() {
        return "🔵 BUBBLE SORT\n\n" +
               "Time Complexity:\n" +
               "• Best: O(n)\n" +
               "• Average: O(n²)\n" +
               "• Worst: O(n²)\n\n" +
               "Space: O(1)\n\n" +
               "How it works:\n" +
               "Repeatedly compares adjacent elements and swaps them if they're in wrong order. " +
               "Larger elements 'bubble' to the end with each pass.\n\n" +
               "Best for: Small datasets, nearly sorted data, educational purposes.";
    }
    
    private String getSelectionSortInfo() {
        return "🟢 SELECTION SORT\n\n" +
               "Time Complexity:\n" +
               "• Best: O(n²)\n" +
               "• Average: O(n²)\n" +
               "• Worst: O(n²)\n\n" +
               "Space: O(1)\n\n" +
               "How it works:\n" +
               "Finds the minimum element from unsorted portion and places it at the beginning. " +
               "Repeats until array is sorted.\n\n" +
               "Best for: Small datasets where minimizing swaps is important.";
    }
    
    private String getInsertionSortInfo() {
        return "🟡 INSERTION SORT\n\n" +
               "Time Complexity:\n" +
               "• Best: O(n)\n" +
               "• Average: O(n²)\n" +
               "• Worst: O(n²)\n\n" +
               "Space: O(1)\n\n" +
               "How it works:\n" +
               "Builds sorted array one element at a time by inserting each element into its correct position.\n\n" +
               "Best for: Small datasets, nearly sorted arrays, online sorting (data arrives in real-time).";
    }
    
    private String getMergeSortInfo() {
        return "🟣 MERGE SORT\n\n" +
               "Time Complexity:\n" +
               "• Best: O(n log n)\n" +
               "• Average: O(n log n)\n" +
               "• Worst: O(n log n)\n\n" +
               "Space: O(n)\n\n" +
               "How it works:\n" +
               "Divides array into halves, recursively sorts them, then merges sorted halves back together.\n\n" +
               "Best for: Large datasets, guaranteed O(n log n), stable sorting needed.";
    }
    
    private String getQuickSortInfo() {
        return "🔴 QUICK SORT\n\n" +
               "Time Complexity:\n" +
               "• Best: O(n log n)\n" +
               "• Average: O(n log n)\n" +
               "• Worst: O(n²)\n\n" +
               "Space: O(log n)\n\n" +
               "How it works:\n" +
               "Picks a pivot element, partitions array around it, then recursively sorts the partitions.\n\n" +
               "Best for: Large datasets, average case performance, in-place sorting.";
    }
}