import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.DecimalFormat;

/**
* StopWatch
* @author Santhosh Reddy Mandadi
* @since 08-Jun-2009
* @version 1.0
* 
* Revisions
* Date          Programmer                      Description
* 01/16/2020    https://github.com/redninja2    For keeping track of time, used diff between start and end System.nanoTime() which 
*                                                   is more accurate than just sleeping 10ms and incrementing 1, which could not be the same
*                                                   10ms every time, depends on system speed/load. 
*                                               Changed GUI to be more flexible/organized. 
* 08/20/2021	https://github.com/redninja2    Added program icon.
*
*/
public class StopWatch extends JFrame implements ActionListener,Runnable
{   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JPanel buttonPanel;
    JLabel disp;
    JButton btn;
    JButton resetBtn;
    long startTime;
    long endTime;
    boolean stop=false;
    DecimalFormat nf;
    DecimalFormat msNf;
    
    public StopWatch()
    {
        nf = new DecimalFormat("00");
        msNf = new DecimalFormat("000");
        buttonPanel = new JPanel(new GridLayout(1,2,5,5));
        disp=new JLabel();
        btn=new JButton("Start");
        resetBtn = new JButton("Reset");
        disp.setFont(new Font("Helvetica",Font.PLAIN,20));
        disp.setBackground(Color.cyan);
        disp.setForeground(Color.red);
        disp.setText("   "+nf.format(0)+":"+nf.format(0)+":"+nf.format(0)+"."+msNf.format(0));
        Container c=getContentPane();
        c.setLayout(new GridLayout(2,1,5,5));
        buttonPanel.add(btn); buttonPanel.add(resetBtn);
        c.add(disp); c.add(buttonPanel);
        btn.addActionListener(this);
        resetBtn.addActionListener(new ResetButtonActionListener());
    }
    
    public void run()
    {
        long h, m, s, ms, diff;
        for(;;)
        {
            if(stop)
            {
                break;
            }
            
            endTime = System.nanoTime();
            diff = endTime - startTime;
            h = diff/3600000000000l;
            diff %= 3600000000000l;
            m = diff/60000000000l;
            diff %= 60000000000l;
            s = diff/1000000000l;
            diff %= 1000000000l;
            ms = diff/1000000l;
            disp.setText("   "+nf.format(h)+":"+nf.format(m)+":"+nf.format(s)+"."+msNf.format(ms));

            try {
                Thread.sleep(10);
            } catch(Exception e){}
        }
    }
    
    public void actionPerformed(ActionEvent ae)
    {
        stop=false;
        Thread t=new Thread(this);
        
        if(ae.getActionCommand().equals("Start"))
        {
            startTime = System.nanoTime();
            t.start();
            btn.setText("Stop");
        }
        else
        {
            stop=true;
        }
    }
    
    public static void main(String[] args) throws IOException 
    {
        StopWatch s=new StopWatch();
        s.setSize(250,100);
        s.setLocationRelativeTo(null);
        s.setTitle("StopWatch");
        s.setDefaultCloseOperation(EXIT_ON_CLOSE);
        s.setIconImage(ImageIO.read(StopWatch.class.getClassLoader().getResourceAsStream("clock.png")));
        s.setVisible(true);
    }

    class ResetButtonActionListener implements ActionListener {
        public void actionPerformed(ActionEvent ae)
        {  
            stop=true;
            disp.setText("   00:00:00.000");
            btn.setText("Start");
        }
    }
}