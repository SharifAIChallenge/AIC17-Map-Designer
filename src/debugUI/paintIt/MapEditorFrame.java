package debugUI.paintIt;

import Swarm.map.Cell;
import Swarm.models.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;

import debugUI.paintIt.EditorUtilSet;

/**
 * Created by miladink on 2/6/17.
 */
public class MapEditorFrame extends JFrame {
    private MapEditorPanel mapPanel;
    private Map map;
    private ButtonGroup buttonGroup = new ButtonGroup();
    ArrayList<JRadioButton> jRadioButtons = new ArrayList<>();
    public MapEditorFrame(){
        String message = JOptionPane.showInputDialog(null, "width,height");
        message = message.replaceAll("\n ", "");
        message = message.replaceAll(" ", "");
        int commaPlace = message.indexOf(',');
        String str1 = message.substring(0, commaPlace);
        String str2 = message.substring(commaPlace+1);
        //make a map for the frame to be edited
        setMap(Integer.valueOf(str1),Integer.valueOf(str2));

        //prepare the frame
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        //making the radio buttons for the editor
        JPanel optionPanel = new JPanel(new GridBagLayout());
        final JRadioButton roach = new JRadioButton("Beetle", true);
        final JRadioButton trash = new JRadioButton("Trash", false);
        final JRadioButton food = new JRadioButton("Food", false);
        final JRadioButton slipper = new JRadioButton("Slipper", false);
        final JRadioButton teleport = new JRadioButton("Teleport", false);
        final JRadioButton eraser = new JRadioButton("Eraser", false);

        EditorUtilSet.addComponentX(0, 1, roach, optionPanel);
        EditorUtilSet.addComponentX(1, 1, trash, optionPanel);
        EditorUtilSet.addComponentX(2, 1, food, optionPanel);
        EditorUtilSet.addComponentX(3, 1, slipper, optionPanel);
        EditorUtilSet.addComponentX(4, 1, teleport, optionPanel);
        EditorUtilSet.addComponentX(5, 1, eraser, optionPanel);
        buttonGroup.add(roach);
        jRadioButtons.add(roach);
        buttonGroup.add(trash);
        jRadioButtons.add(trash);
        buttonGroup.add(food);
        jRadioButtons.add(food);
        buttonGroup.add(slipper);
        jRadioButtons.add(slipper);
        buttonGroup.add(teleport);
        jRadioButtons.add(teleport);
        buttonGroup.add(eraser);
        jRadioButtons.add(eraser);

        //making the motherPanel for JFrame
        JPanel motherPanel = new JPanel();
        motherPanel.setLayout(new GridBagLayout());

        //TODO
        JPanel constantsPanel = new JPanel(new GridBagLayout());
        constantsPanel.setVisible(false);
        ArrayList<String> consts = new ArrayList<>();
        consts.add("turnTimeOut");
        consts.add("foodProb");
        consts.add("trashProb");
        consts.add("netProb");
        consts.add("netValidTime");
        consts.add("color cost");
        consts.add("sick cost");
        consts.add("update cost");
        consts.add("detMovCost");
        consts.add("killQueenScore");
        consts.add("killBothQueenScore");
        consts.add("killFishScore");
        consts.add("queenCollisionScore");
        consts.add("fishFoodScore");
        consts.add("queenFoodScore");
        consts.add("sickLifeTime");
        consts.add("powerRatio");
        consts.add("endRatio");
        consts.add("disobeyNum ");
        consts.add("foodValidTime");
        consts.add("trashValidTime");
        ArrayList<Double> initals =  new ArrayList<>();
        initals.add(500.0);//turnTimeOut
        initals.add(0.005);//foodProb
        initals.add(0.003);//trashProb
        initals.add(0.001);//netProb
        initals.add(3.0);//netValidTime
        initals.add(30.0);//color cost
        initals.add(50.0);//sick cost
        initals.add(40.0);//update cost
        initals.add(300.0);//detMovCost
        initals.add(1500.0);//killQueenScore
        initals.add(1000.0);//killBothQueenScore
        initals.add(300.0);//killFishScore
        initals.add(600.0);//queenCollisionScore
        initals.add(15.0);//fishFoodScore
        initals.add(25.0);//queenFoodScore
        initals.add(10.0);//sickLifeTime
        initals.add(2.0);// powerRatio,
        initals.add(70.0);//endRatio
        initals.add(1.0);// disobeyNum//TODO:for debug turn it off
        initals.add(6.0);// foodValidTime
        initals.add(10.0);// trashValidTime]
        ArrayList<JTextField> texts = new ArrayList<>();
        for(int i = 0; i<consts.size(); i++){
            JPanel valuePanel = new JPanel(new GridBagLayout());
            JLabel label = new JLabel(consts.get(i));
            JTextField text = new JTextField(Double.toString(initals.get(i)));
            texts.add(text);
            EditorUtilSet.addComponentX(0,50,label,valuePanel);
            EditorUtilSet.addComponentX(1,50,text,valuePanel);
            EditorUtilSet.addComponentY(i,50,valuePanel,constantsPanel);
        }
        //adding the choosePanel to choose between constants changing and map editing
        JPanel choosePanel = new JPanel(new GridBagLayout());
        final JRadioButton constants = new JRadioButton("Constants", false);
        final JRadioButton editor = new JRadioButton("Editor", true);

        EditorUtilSet.addComponentX(1, 1, constants, choosePanel);
        EditorUtilSet.addComponentX(0, 1, editor, choosePanel);
        ButtonGroup buttonGroup1 = new ButtonGroup();
        buttonGroup1.add(constants);
        buttonGroup1.add(editor);

        constants.addActionListener(e -> {
            constantsPanel.setVisible(true);
            mapPanel.setVisible(false);
        });
        editor.addActionListener(e -> {
            mapPanel.setVisible(true);
            constantsPanel.setVisible(false);
        });

        JPanel optionsPanel = new JPanel(new GridBagLayout());
        EditorUtilSet.addComponentX(0,1,choosePanel, optionsPanel);
        EditorUtilSet.addComponentY(1,1,optionPanel, optionsPanel);



        mapPanel = new MapEditorPanel(this, map);//it is the frame to set
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        JButton button3 = new JButton("load map");
        button3.addActionListener(e -> {
            FileDialog fileDialog = new FileDialog((Frame) null, "load a map", FileDialog.LOAD);
            fileDialog.setMultipleMode(false);
            fileDialog.setVisible(true);
            File[] files = fileDialog.getFiles();
            if (files.length != 1)
                return;
            File f = files[0];
            Map temp = new Map(f);
            MapJsonExtra.killIds(temp);
            mapPanel.setMap(temp);
            mapPanel.repaint();
            this.repaint();
            this.pack();
        });
        JButton button4 = new JButton("save map");
        button4.addActionListener(e -> {
            ArrayList<Double> values = new ArrayList<>();
            for(int i = 0; i<texts.size(); i++){
                values.add(Double.parseDouble(texts.get(i).getText()));
            }
            mapPanel.saveMap(values);
            mapPanel.setFocusable(true);
            mapPanel.requestFocus();
        });
        JButton button5 = new JButton("change theme");
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        JPanel bPanel1 = new JPanel();
        JPanel bPanel2 = new JPanel();
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapPanel.changeTheme();
                bPanel1.setBackground(mapPanel.getThemeBackGround());
                bPanel2.setBackground(mapPanel.getThemeBackGround());
                buttonPanel.setBackground(mapPanel.getThemeBackGround());
                CellPainter.changeTheme();
                mapPanel.requestFocus();
            }
        });

        EditorUtilSet.addComponentX(2, 1, button3, buttonPanel);
        EditorUtilSet.addComponentX(3, 1, button4, buttonPanel);
        EditorUtilSet.addComponentX(4, 1, button5, buttonPanel);

        //EditorUtilSet.addComponentY(0, 1, textPanel, motherPanel);
        //EditorUtilSet.addComponentY(1, 1, mapPanel, motherPanel);
        //EditorUtilSet.addComponentY(2, 1, buttonPanel, motherPanel);
        buttonPanel.setBackground(mapPanel.getThemeBackGround());
        bPanel1.setBackground(mapPanel.getThemeBackGround());
        bPanel2.setBackground(mapPanel.getThemeBackGround());
        EditorUtilSet.addComponentX(0, 50, bPanel1, centerPanel);
        EditorUtilSet.addComponentX(1, 1, mapPanel, centerPanel);
        EditorUtilSet.addComponentX(2,10,constantsPanel,centerPanel);
        EditorUtilSet.addComponentX(3, 50, bPanel2, centerPanel);
        motherPanel.setLayout(new BorderLayout());
        motherPanel.add(optionsPanel, BorderLayout.NORTH);
        motherPanel.add(centerPanel, BorderLayout.CENTER);
        motherPanel.add(buttonPanel, BorderLayout.SOUTH);
        //the end
        this.setContentPane(motherPanel);
        this.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        this.pack();
        System.out.println(getSelected());

    }

    public void setMap(int w, int h){
        if(w<1 || h<1)
            return;
        map = new Map();
        Cell cells[][] = new Cell[h][w];
        for(int i = 0; i<h; i++)
            for(int j = 0; j<w; j++){
                cells[i][j] = new Cell(i, j, null, null, null);
            }
        map.setCells(cells);
        map.setW(w);
        map.setH(h);
        this.mapPanel = new MapEditorPanel(this, map);
    }

    public String getSelected(){
        for(JRadioButton jRadioButton: jRadioButtons)
            if(jRadioButton.isSelected())
                return jRadioButton.getText();
        return "it is a bug";
    }

    public Map getMap() {
        return map;
    }
}
