package debugUI.paintIt;
import Swarm.map.Cell;
import Swarm.models.Map;
import Swarm.objects.*;
import debugUI.control.Json;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

import static java.util.Objects.hash;

/*
written by miladink
 */
public class MapEditorPanel extends JPanel{

    private MapEditorFrame frame = null;
    private Map gameMap;
    private int cellSize;
    private int theme = 0;
    private int themeNumbers = 4;
    private boolean teleport_Phase2 = false;
    private boolean sick = false;
    private boolean queen = false;
    private int team = 0;
    private int color = 0;
    private  int direction = 0;
    private Cell lastCell;
    private int teleportCounter = -1;


    MapEditorPanel(MapEditorFrame frame, Map gameMap){
        this.frame = frame;
        //---enter the map and the first shot will be taken
        this.setMap(gameMap);
        lastCell = gameMap.getCells()[0][0];
        setCellSize();
        this.setFocusable(true);
        this.requestFocus();
        MapEditorPanel thisMap = this;
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 39){
                    direction+=3;
                    direction%=4;
                }
                else if(e.getKeyCode() == 37){
                    direction+=1;
                    direction%=4;
                }else if(e.getKeyChar() == 's'){
                    sick = !sick;
                }else if(e.getKeyChar() == 'q'){
                    queen = !queen;
                }else if(e.getKeyChar() == 't'){
                    team = 1 - team;
                }else if(e.getKeyChar() == 'c'){
                    color = 1 - color;
                }
                //it is they are!
                if(lastCell.getContent() instanceof Fish){
                    Fish fish = new Fish(0,lastCell, team, direction, color, queen);
                    fish.setSick(sick);
                    lastCell.setContent(fish);
                }
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("hello");
                super.mouseClicked(e);
                thisMap.setFocusable(true);
                thisMap.requestFocus();
                int y = e.getX();//it is true but look weird I know
                int x = e.getY();//it is true but look weird I know
                x = x/cellSize;
                y = y/cellSize;
                Cell cell = thisMap.getGameMap().getCells()[x][y];
                System.out.println("x is" + x + "y is" + y);
                String selected = frame.getSelected();
                System.out.println("selected is" + selected);
                if(!selected.equals("Teleport")) {
                    if(teleport_Phase2) {
                        teleport_Phase2 = false;
                        lastCell.setTeleport(null);
                    }
                }
                if(teleport_Phase2){
                    if(cell.getTeleport() == null) {
                        lastCell.getTeleport().setPair(cell);
                        cell.setTeleport(new Teleport(teleportCounter--, cell, lastCell));
                        teleport_Phase2 = false;
                    }
                }else
                {
                    switch (selected) {
                        case "Beetle":
                            if(cell.getContent() instanceof  Fish){
                                Fish fish = (Fish)cell.getContent();
                                team = fish.getTeamNumber();
                                direction = fish.getDirection();
                                color = fish.getColorNumber();
                                queen = fish.isQueen();
                                sick = fish.isSick();
                            }
                            Fish fish = new Fish(0, cell, team, direction, color, queen);
                            fish.setSick(sick);
                            cell.setContent(fish);

                            break;
                        case "Trash":
                            cell.setContent(new Trash(0, cell));
                            break;
                        case "Food":
                            cell.setContent(new Food(0, cell));
                            break;
                        case "Teleport":
                            if (cell.getTeleport() == null) {
                                cell.setTeleport(new Teleport(teleportCounter--, cell, cell));
                                teleport_Phase2 = true;
                            }
                            break;
                        case "Slipper":
                            cell.setNet(new Net(0, cell));
                            break;
                        case "Eraser":
                            cell.setContent(null);
                            if (cell.getTeleport() != null) {
                                Cell cell1 = cell.getTeleport().getPair();
                                cell1.setTeleport(null);
                                cell.setTeleport(null);
                            }
                            cell.setNet(null);
                            break;
                    }
                }
                lastCell = cell;
                thisMap.repaint();
            }
        });
    }
    @Override
    protected synchronized void paintComponent(Graphics g){
        super.paintComponent(g);
        //---draw the image which sign is on, on the panel
        Graphics2D g2d = (Graphics2D)g;
        g2d.drawImage(draw(gameMap), null, null);
        //---the resulted image is now drawn on the panel
    }
    public void setMap(Map gameMap){
        this.gameMap = gameMap;
        setCellSize();
        CellPainter.changeTheme();
        this.repaint();
    }
    private BufferedImage draw(Map gameMap){
        //---we will draw on this image and then draw this image on the JPanel
        BufferedImage shot = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        Graphics gTemp = shot.createGraphics();
        Graphics2D gTemp2d = (Graphics2D)gTemp;
        super.paintComponent(gTemp);
        //---set the settings for g2d
        gTemp2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        gTemp2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);
        //---settings set
        //---draw each cell individually
        Cell cells[][] = gameMap.getCells();
        ArrayList<Cell> cells_net = new ArrayList<>();
        for(int i = 0; i<cells.length;i++) {
            for (int j = 0; j < cells[0].length; j++) {
                gTemp2d.translate(j * cellSize, i * cellSize);
                CellPainter.paint(cells[i][j], cellSize, gTemp2d, theme);
                gTemp2d.translate(-j * cellSize, -i * cellSize);
                if(cells[i][j].getNet()!=null)
                    cells_net.add(cells[i][j]);
            }
        }

        for(Cell cell_temp: cells_net) {
            gTemp2d.translate(cell_temp.getColumn() * cellSize, cell_temp.getRow() * cellSize);
           // CellPainter.drawNet(cells[cell_temp.getRow()][cell_temp.getColumn()], cellSize, gTemp2d, theme);
            CellPainter.drawNet(hash(cell_temp.getColumn()+cell_temp.getRow())%4, cellSize,gameMap.getW(),gameMap.getH(), gTemp2d, theme);
            gTemp2d.translate(-cell_temp.getColumn() * cellSize, -cell_temp.getRow() * cellSize);
        }
        //---each cell is drawn
        return shot;
    }

    void saveMap(ArrayList<Double> constants){
        FileDialog fileDialog = new FileDialog((Frame) null, "Save Recorded Images", FileDialog.SAVE);
        fileDialog.setFilenameFilter((dir, name) -> name.matches(".*\\.zip"));
        fileDialog.setMultipleMode(false);
        fileDialog.setFile("1.map");
        fileDialog.setVisible(true);
        File[] files = fileDialog.getFiles();
        if (files.length != 1)
            return;
        File f = files[0];
        MapJsonExtra jsonExtra = new MapJsonExtra(gameMap, constants);
        String json = Json.GSON.toJson(jsonExtra);
        try {
            PrintWriter printWriter = new PrintWriter(f);
            printWriter.print(json);
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCellSize(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int cellWidth = Math.min(80, (int)(screenSize.getWidth()*0.8)/gameMap.getW());//to be sure that width will not violate 600
        int cellHeight = Math.min(80, (int)(screenSize.getHeight()*0.8)/gameMap.getH());//to be sure that height will not violate 800
        cellSize = Math.min(cellWidth, cellHeight);
        int width = gameMap.getW() * cellSize;
        int height = gameMap.getH() * cellSize;
        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width,height));
    }



    void changeTheme(){
        theme+=1;
        theme = theme%themeNumbers;
        repaint();
    }

    Color getThemeBackGround(){
        Color colors[] = new Color[Math.max(themeNumbers,5)];
        colors[0] = Color.decode("#606c68");
        colors[1] = Color.decode("#606c68");
        colors[2] = Color.decode("#e9cef3");
        colors[3] = Color.decode("#e9cef3");
        colors[4] = Color.decode("#757575");
        if(colors.length>theme)
            return colors[theme];
        else
            return Color.BLACK;
    }


    public static void main(String[] args) {
        MapEditorFrame frame = new MapEditorFrame();
    }

    public Map getGameMap() {
        return gameMap;
    }
}
