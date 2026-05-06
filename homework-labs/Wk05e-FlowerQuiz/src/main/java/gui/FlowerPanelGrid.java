package gui;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class FlowerPanelGrid extends JPanel {

    // ====== Basic grid config ======
    private static final int ROWS = 8;
    private static final int COLS = 8;
    private static final int PADDING = 12;

    // State for click-to-place
    private final boolean[][] selected = new boolean[ROWS][COLS];
    private int hoverR = -1, hoverC = -1;

    // Mode flags
    private boolean carrotMode = false;
    private boolean checkerboardBg = false;
    private boolean diagonalBg = false;

    // Status label (counter + mode hints)
    private JLabel statusLabel;

    private final Random rng = new Random(42);

    // ====== Timer for animation ======
    private final Timer timer;

    // Falling flowers
    private static class FallingFlower {
        double x, y, vy;
        Color petalColor;
        FallingFlower(double x, double y, double vy, Color c) {
            this.x = x; this.y = y; this.vy = vy; this.petalColor = c;
        }
    }
    private final List<FallingFlower> falling = new ArrayList<>();

    public FlowerPanelGrid() {
        setFocusable(true);
        setOpaque(true);
        setBackground(Color.white);

        // Mouse interaction
        MouseAdapter mouse = new MouseAdapter() {
            @Override public void mouseMoved(MouseEvent e) { updateHover(e.getPoint()); }
            @Override public void mouseExited(MouseEvent e) { hoverR = hoverC = -1; repaint(); }
            @Override public void mouseClicked(MouseEvent e) { toggleCellAt(e.getPoint()); }
            @Override public void mouseDragged(MouseEvent e) { updateHover(e.getPoint()); }
        };
        addMouseMotionListener(mouse);
        addMouseListener(mouse);

        // Keys for modes
        addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) {
                char ch = e.getKeyChar();
                if (ch == 'c' || ch == 'C') {
                    carrotMode = !carrotMode;
                    updateStatus(); repaint();
                }
                if (ch == 'x' || ch == 'X') { clearAll(); }
                if (ch == 'b' || ch == 'B') { checkerboardBg = !checkerboardBg; repaint(); }
                if (ch == 'd' || ch == 'D') { diagonalBg = !diagonalBg; repaint(); }
                if (ch == 'r' || ch == 'R') { randomize(0.25); }
            }
        });

        // Timer for animation
        timer = new Timer(33, evt -> {
            stepDemo();
            repaint();
        });
        timer.start();
    }

    public void setStatusLabel(JLabel label) {
        this.statusLabel = label;
        updateStatus();
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(640, 640);
    }

    // ====== Mouse helpers ======
    private void updateHover(Point p) {
        int[] rc = pointToCell(p);
        hoverR = rc[0]; hoverC = rc[1];
        repaint();
    }

    private void toggleCellAt(Point p) {
        int[] rc = pointToCell(p);
        int r = rc[0], c = rc[1];
        if (r >= 0 && c >= 0) {
            selected[r][c] = !selected[r][c];
            updateStatus();
            repaint();
        }
        requestFocusInWindow();
    }

    // ====== Mapping helpers ======
    private int[] pointToCell(Point p) {
        Insets in = getInsets();
        int w = getWidth() - in.left - in.right - 2 * PADDING;
        int h = getHeight() - in.top - in.bottom - 2 * PADDING;
        if (w <= 0 || h <= 0) return new int[]{-1, -1};
        int cellW = w / COLS;
        int cellH = h / ROWS;
        int x0 = in.left + PADDING;
        int y0 = in.top + PADDING;
        int c = (p.x - x0) / cellW;
        int r = (p.y - y0) / cellH;
        if (c < 0 || c >= COLS || r < 0 || r >= ROWS) return new int[]{-1, -1};
        return new int[]{r, c};
    }

    private Rectangle cellBounds(int r, int c) {
        Insets in = getInsets();
        int w = getWidth() - in.left - in.right - 2 * PADDING;
        int h = getHeight() - in.top - in.bottom - 2 * PADDING;
        int cellW = w / COLS;
        int cellH = h / ROWS;
        int x0 = in.left + PADDING;
        int y0 = in.top + PADDING;
        return new Rectangle(x0 + c * cellW, y0 + r * cellH, cellW, cellH);
    }

    // ====== Status and utility ======
    private void updateStatus() {
        if (statusLabel == null) return;
        int count = 0;
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                if (selected[r][c]) count++;
        statusLabel.setText(
                "Selected: " + count +
                        " • Mode: " + (carrotMode ? "Carrot" : "Flower") +
                        " • Keys: [C] carrot, [B] checker, [D] diagonals, [R] randomize, [X] clear"
        );
    }

    private void clearAll() {
        for (int r = 0; r < ROWS; r++)
            Arrays.fill(selected[r], false);
        updateStatus();
        repaint();
    }

    private void randomize(double p) {
        for (int r = 0; r < ROWS; r++)
            for (int c = 0; c < COLS; c++)
                selected[r][c] = rng.nextDouble() < p;
        updateStatus();
        repaint();
    }

    // ====== Paint ======
    @Override protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Insets in = getInsets();
            int w = getWidth() - in.left - in.right - 2 * PADDING;
            int h = getHeight() - in.top - in.bottom - 2 * PADDING;
            int x0 = in.left + PADDING;
            int y0 = in.top + PADDING;
            if (w <= 0 || h <= 0) return;

            int cellW = w / COLS;
            int cellH = h / ROWS;
            int size = Math.min(cellW, cellH);

            // Background variations
            if (checkerboardBg) {
                drawCheckerboard(g2, x0, y0, cellW, cellH,
                        new Color(255, 0, 0), new Color(0, 0, 0));
            }
            if (diagonalBg) {
                drawDiagonalPattern(g2, x0, y0, w, h, 20);
            }

            // Grid lines
            g2.setColor(new Color(220, 220, 220));
            for (int r = 0; r <= ROWS; r++) {
                int y = y0 + r * cellH;
                g2.drawLine(x0, y, x0 + COLS * cellW, y);
            }
            for (int c = 0; c <= COLS; c++) {
                int x = x0 + c * cellW;
                g2.drawLine(x, y0, x, y0 + ROWS * cellH);
            }

            // Draw placed flowers or carrots
            Color[] petalPalette = {
                    new Color(255, 192, 203),
                    new Color(255, 105, 180),
                    new Color(255, 165, 0),
                    new Color(173, 216, 230),
                    new Color(144, 238, 144)
            };

            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (!selected[r][c]) continue;
                    if ((r % 2 != 0) || (c % 2 != 0)) continue; // structured placement
                    Rectangle cb = cellBounds(r, c);
                    int cx = cb.x + cb.width / 2;
                    int cy = cb.y + cb.height / 2;

                    int jitterX = rng.nextInt(7) - 3;
                    int jitterY = rng.nextInt(7) - 3;
                    Color petalColor = petalPalette[rng.nextInt(petalPalette.length)];

                    if (carrotMode) {
                        drawCarrot(g2, cx + jitterX, cy + jitterY, (int)(size * 0.35));
                    } else {
                        drawFlower(g2, cx + jitterX, cy + jitterY, (int)(size * 0.35), petalColor);
                    }
                }
            }

            // Falling flowers
            for (FallingFlower f : falling) {
                drawFlower(g2, (int)f.x, (int)f.y, 12, f.petalColor);
            }

            // Hover highlight
            if (hoverR >= 0 && hoverC >= 0) {
                Rectangle hb = cellBounds(hoverR, hoverC);
                g2.setColor(new Color(0, 234, 255, 30));
                g2.fillRect(hb.x, hb.y, hb.width, hb.height);
                g2.setColor(new Color(0, 255, 198, 80));
                g2.drawRect(hb.x, hb.y, hb.width, hb.height);
            }

        } finally {
            g2.dispose();
        }
    }

    // ====== Animation step ======
    private void stepDemo() {
        // Update falling flowers
        for (Iterator<FallingFlower> it = falling.iterator(); it.hasNext();) {
            FallingFlower f = it.next();
            f.y += f.vy;
            if (f.y > getHeight()) it.remove();
        }
        if (rng.nextDouble() < 0.05) spawnFlower();
    }

    private void spawnFlower() {
        int w = getWidth() - 2 * PADDING;
        if (w <= 0) return;
        int x = PADDING + rng.nextInt(w);
        Color c = new Color(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
        falling.add(new FallingFlower(x, 0, 2 + rng.nextDouble() * 3, c));
    }

    // ====== Background helpers ======
    private void drawCheckerboard(Graphics2D g2, int x0, int y0, int cellW, int cellH, Color a, Color b) {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                g2.setColor(((r + c) & 1) == 0 ? a : b);
                Rectangle cb = cellBounds(r, c);
                g2.fillRect(cb.x, cb.y, cb.width, cb.height);
            }
        }
    }

    private void drawDiagonalPattern(Graphics2D g2, int x0, int y0, int w, int h, int spacing) {
        g2.setColor(new Color(230, 230, 230));
        for (int x = -h; x < w; x += spacing) {
            g2.drawLine(x0 + x, y0, x0 + x + h, y0 + h);
        }
    }

    // ====== Drawing helpers ======
    private void drawFlower(Graphics2D g2, int cx, int cy, int r, Color petalColor) {
        int pr = (int)(r * 0.65);
        int[][] offs = { {0,-r}, { r,0}, {0, r}, {-r,0} };
        g2.setStroke(new BasicStroke(1.2f));
        for (int i = 0; i < offs.length; i++) {
            int px = cx + offs[i][0];
            int py = cy + offs[i][1];
            g2.setColor(petalColor);
            g2.fillOval(px - pr, py - pr, 2 * pr, 2 * pr);
            g2.setColor(new Color(150, 120, 130));
            g2.drawOval(px - pr, py - pr, 2 * pr, 2 * pr);
        }
        int cr = (int)(r * 0.55);
        g2.setColor(new Color(255, 215, 0));
        g2.fillOval(cx - cr, cy - cr, 2 * cr, 2 * cr);
        g2.setColor(new Color(160, 140, 0));
        g2.drawOval(cx - cr, cy - cr, 2 * cr, 2 * cr);
    }

    private void drawCarrot(Graphics2D g2, int cx, int cy, int s) {
        Polygon body = new Polygon();
        body.addPoint(cx, cy - s);
        body.addPoint(cx - s / 3, cy + s);
        body.addPoint(cx + s / 3, cy + s);
        g2.setStroke(new BasicStroke(1.2f));
        g2.setColor(new Color(255, 140, 0));
        g2.fillPolygon(body);
        g2.setColor(new Color(170, 90, 0));
        g2.drawPolygon(body);
        g2.setColor(new Color(34, 139, 34));
        int gy = cy - s - s / 4;
        g2.drawLine(cx, cy - s, cx - s / 2, gy);
        g2.drawLine(cx, cy - s, cx + s / 2, gy);
        g2.drawLine(cx, cy - s, cx, gy - s / 6);
    }
}
