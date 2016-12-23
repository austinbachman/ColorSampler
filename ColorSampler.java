import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.io.*;
import java.util.ArrayList;
import java.lang.Math.*;

public class ColorSampler extends JFrame
{
    protected JButton buttonSave, buttonReset, redUp, redDn,
              grnUp, grnDn, bluUp, bluDn;
    protected JPanel buttonPanel, selectPanel, leftPanel;
    protected JTextField red, green, blue;
    protected JList<String> listColors;
    protected DrawColor draw;
    protected ColorType currentColor;
    protected ArrayList<ColorType> colorSet;
    protected String colorNames[];
    protected int colorIndex, listSize;
    static ColorSampler program;
    
    public static void main (String[] args)
    {
        try
        {
            program = new ColorSampler("Color Sampler");
        }
        catch (IOException e)
        {
            System.out.println("File not found");
            System.exit(0);
        }       
    }
    
    public ColorSampler(String title) throws IOException
    {
        super(title);
        setBounds(100, 100, 450, 350);
        addWindowListener(new WindowDestroyer());
        
        listColors = new JList<String>();
        listColors.addListSelectionListener( new ListHandler());
        
        buttonSave = new JButton("Save");
        buttonReset = new JButton("Reset");
        redUp = new JButton("+");
        redDn = new JButton("-");
        grnUp = new JButton("+");
        grnDn = new JButton("-");
        bluUp = new JButton("+");
        bluDn = new JButton("-");
        buttonSave.addActionListener( new ActionHandler() );
        buttonReset.addActionListener( new ActionHandler() );
        redUp.addActionListener( new ActionHandler() );
        redDn.addActionListener( new ActionHandler() );
        grnUp.addActionListener( new ActionHandler() );
        grnDn.addActionListener( new ActionHandler() );
        bluUp.addActionListener( new ActionHandler() );
        bluDn.addActionListener( new ActionHandler() );
        
        draw = new DrawColor();
        draw.setBounds(10, 10, 200, 200);
        
        buttonPanel = new JPanel();
        leftPanel = new JPanel();
        selectPanel = new JPanel();
        
        File infile = new File("colors.txt");
        if( !infile.exists() )
        {
            throw new IOException("File not found");
        }
        FileInputStream stream = new FileInputStream(infile);
        InputStreamReader reader = new InputStreamReader(stream);
        StreamTokenizer tokens = new StreamTokenizer(reader);
        
        colorSet = new ArrayList<ColorType>();
        
        while( tokens.nextToken() != tokens.TT_EOF)
        {
            currentColor = new ColorType();

            currentColor.name = (String)tokens.sval;
            tokens.nextToken();
            currentColor.r = (int)tokens.nval;
            tokens.nextToken();
            currentColor.g = (int)tokens.nval;
            tokens.nextToken();
            currentColor.b = (int)tokens.nval;
            colorSet.add(currentColor);
            colorIndex++;
        }
        
        stream.close();
        
        colorIndex--;
        listSize = colorIndex + 1;
        colorIndex = 0;
        currentColor = new ColorType(colorSet.get(colorIndex));
        colorNames = new String[listSize];
        for( int i = 0; i < listSize; i++ )
        {
            colorNames[i] = (colorSet.get(i)).name;
        }
        
        listColors.setListData(colorNames);
        
        selectPanel.setLayout( new GridLayout(3, 4, 5, 5));
        
        buttonPanel.add(buttonSave);
        buttonPanel.add(buttonReset);
        red = new JTextField(String.valueOf(currentColor.r));
        green = new JTextField(String.valueOf(currentColor.g));
        blue = new JTextField(String.valueOf(currentColor.b));
        selectPanel.add(new JLabel("Red:"));
        selectPanel.add(red);
        selectPanel.add(redDn);
        selectPanel.add(redUp);
        selectPanel.add(new JLabel("Green:"));
        selectPanel.add(green);
        selectPanel.add(grnDn);
        selectPanel.add(grnUp);
        selectPanel.add(new JLabel("Blue:"));
        selectPanel.add(blue);
        selectPanel.add(bluDn);
        selectPanel.add(bluUp);
        leftPanel.setLayout(null);
        leftPanel.add(draw);
        leftPanel.add(selectPanel);
        leftPanel.add(buttonPanel);
        draw.setBounds(10, 10, 250, 150);
        selectPanel.setBounds(10, 170, 250, 100);
        buttonPanel.setBounds(50, 290, 150, 100 );
        getContentPane().setLayout(null);
        
        getContentPane().add(leftPanel);
        getContentPane().add(listColors);
        leftPanel.setBounds(10, 10, 270, 400);
        listColors.setBounds(300, 20, 130, 310);

        setVisible(true);
        colorIndex = 0;
    }
    
    private class WindowDestroyer extends WindowAdapter
    {
        public void windowClosing(WindowEvent e)
        {
            try
            {
                output();
            }
            catch (IOException ex)
            {
                System.out.println("File not found");
            }  
            System.exit(0);
        }
        
        public void output() throws IOException
        {
            File ofile = new File("colors.txt");
            if( !ofile.exists() )
            {
                throw new IOException("File not found");
            }
            FileOutputStream ostream = new FileOutputStream(ofile);
            PrintWriter writer = new PrintWriter(ostream, true);
            for( int i = 0; i < listSize; i++ )
            {
                writer.println((colorSet.get(i)).toString());
            }
            ostream.close();
        }
    }
    
    private class ListHandler implements ListSelectionListener
    {
        public void valueChanged( ListSelectionEvent e )
        {
            if( e.getSource() == listColors )
                if( !e.getValueIsAdjusting() )
                {
                    colorIndex = listColors.getSelectedIndex();
                    currentColor = new ColorType(colorSet.get(colorIndex));
                    red.setText(String.valueOf(currentColor.r));
                    green.setText(String.valueOf(currentColor.g));
                    blue.setText(String.valueOf(currentColor.b));
                    draw.repaint();
                    program.setTitle("Color Sampler");
                }
        }
    }
    
    private class ActionHandler implements ActionListener
    {
        public void actionPerformed( ActionEvent e )
        {
            if( e.getSource() == buttonSave )
            {
                colorSet.set(colorIndex, new ColorType(currentColor));
                program.setTitle("Color Sampler");
            }
            else if( e.getSource() == buttonReset )
            {
                currentColor = new ColorType(colorSet.get(colorIndex));
                program.setTitle("Color Sampler");
            }
            else if( e.getSource() == redUp )
            {
                currentColor.r = Math.min( 255, currentColor.r+5 );
                program.setTitle("Color Sampler*");
            }
            else if( e.getSource() == redDn )
            {
                currentColor.r = Math.max( 0, currentColor.r-5 );
                program.setTitle("Color Sampler*");
            }
            else if( e.getSource() == grnUp )
            {
                currentColor.g = Math.min( 255, currentColor.g+5 );
                program.setTitle("Color Sampler*");
            }
            else if( e.getSource() == grnDn )
            {
                currentColor.g = Math.max( 0, currentColor.g-5 );
                program.setTitle("Color Sampler*");
            }
            else if( e.getSource() == bluUp )
            {
                currentColor.b = Math.min( 255, currentColor.b+5 );
                program.setTitle("Color Sampler*");
            }
            else if( e.getSource() == bluDn )
            {
                currentColor.b = Math.max( 0, currentColor.b-5 );
                program.setTitle("Color Sampler*");
            }
            
            red.setText(String.valueOf(currentColor.r));
            green.setText(String.valueOf(currentColor.g));
            blue.setText(String.valueOf(currentColor.b));
            draw.repaint();
        }            
    }
    
    private class ColorType
    {
        public int r;
        public int g;
        public int b;
        public String name;
        ColorType()
        {
            r = 0;
            g = 0;
            b = 0;
            name = "";
        }
        public ColorType( ColorType another )
        {
            this.r = another.r;
            this.g = another.g;
            this.b = another.b;
            this.name = another.name;
        }
        public String toString()
        {
            return name + "\t\t" + r + "\t" + g + "\t" + b;
        }
    }
    
    private class DrawColor extends JComponent
    {
        public void paint(Graphics graph)
        {
            Dimension d = getSize();
            
            graph.setColor( new Color(currentColor.r, currentColor.g, currentColor.b));
            graph.fillRect(1, 1, d.width-2, d.height-2);
        }
    }
}
