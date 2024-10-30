import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;

public class Minesweeper {
    JFrame frame=new JFrame("MINESWEEPER");
    private int tileSize=70;
    private int rows=8;
    private int columns=8;
    private int width=columns*tileSize;
    private int height=rows*tileSize;
    private JLabel minelb=new JLabel();
    private JPanel minejp=new JPanel();
    private JPanel boardjp=new JPanel();
    private MineButton mineButtons [][] = new MineButton[rows][columns];
    private ArrayList<MineButton> mineList=new ArrayList<MineButton>();
    int clickedbuttons=0;
    boolean gameover=false;
    int numberOfMines=5;
    Random random=new Random();

    public Minesweeper() {
        frame.setSize(width,height);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);

        minelb.setHorizontalAlignment(JLabel.CENTER);
        minelb.setFont(new Font("Arial",Font.BOLD,25));
        minelb.setText("MINESWEEPER");
        minelb.setOpaque(true);

        boardjp.setLayout(new GridLayout(rows,columns));

        this.makeMap(rows,columns);
        minejp.setLayout(new BorderLayout());
        minejp.add(minelb);
        frame.add(minejp,BorderLayout.NORTH);
        frame.add(boardjp);



        this.setMines();
        frame.setVisible(true);
    }
    public void makeMap(int rows,int columns)
    {
        for (int i=0; i<rows;i++)
        {
            for (int j=0;j<columns;j++)
            {
                MineButton mn= new MineButton(i,j);
                mineButtons[i][j]=mn;
                //mn.setMargin(new Insets(0,0,0,0));
                mn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameover)
                            return;
                        MineButton mb=(MineButton) e.getSource();
                        if (e.getButton()==MouseEvent.BUTTON1)
                        {
                            if (mb.getText()=="")
                            {
                                if (mineList.contains(mb))
                                {
                                    revealMines();
                                }
                                else
                                {
                                    checkMine(mb.rNumber,mb.cNumber);
                                }
                            }
                        }
                        else if(e.getButton()==MouseEvent.BUTTON3)
                        {
                            if(mb.getText()=="" && mb.isEnabled())
                            {
                                mb.setText("\uD83D\uDEA9");
                            }
                            else if (mb.getText()=="\uD83D\uDEA9")
                            {
                                mb.setText("");
                            }

                        }
                        else
                        {
                            mb.setText("");
                        }
                    }

                });
                mn.setFont(new Font("Arial",Font.ITALIC,40));
                boardjp.add(mn);
            }
        }
    }

    public void checkMine(int rNumber, int cNumber) {
        if (rNumber<0 || rNumber>=rows || cNumber<0 || cNumber>=columns)
            return;

        MineButton btn=mineButtons[rNumber][cNumber];
        if (!btn.isEnabled())
            return;
        btn.setEnabled(false);
        clickedbuttons+=1;
        int founded=0;
        //iznad
        founded +=countMines(rNumber-1,cNumber-1);
        founded +=countMines(rNumber-1,cNumber);
        founded +=countMines(rNumber-1,cNumber+1);
        //levo i desno
        founded +=countMines(rNumber,cNumber-1);
        founded +=countMines(rNumber,cNumber+1);
        //ispod
        founded +=countMines(rNumber+1,cNumber-1);
        founded +=countMines(rNumber+1,cNumber);
        founded +=countMines(rNumber+1,cNumber+1);
        if (founded>0)
        {
            btn.setText(Integer.toString(founded));
        }
        else
        {
            btn.setText("");
            checkMine(rNumber-1,cNumber-1);
            checkMine(rNumber-1,cNumber);
            checkMine(rNumber-1,cNumber+1);
            //levo i desno
            checkMine(rNumber,cNumber-1);
            checkMine(rNumber,cNumber+1);
            //ispod
            checkMine(rNumber+1,cNumber-1);
            checkMine(rNumber+1,cNumber);
            checkMine(rNumber+1,cNumber+1);
        }

        if (clickedbuttons==((rows*columns)-mineList.size()))
        {
            gameover=true;
            minelb.setText("WIN");
        }

    }
    public int countMines(int rNumber,int cNumber)
    {
        if (rNumber<0 || rNumber>=rows || cNumber<0 || cNumber>=columns)
        {
            return 0;
        }
        if (mineList.contains(mineButtons[rNumber][cNumber]))
        {
            return 1;
        }
            return 0;
    }

    public void setMines()
    {
        int tmp=numberOfMines;
        while (tmp>0)
        {
            int r=random.nextInt(rows);
            int c=random.nextInt(columns);
            MineButton btn=mineButtons[r][c];
            if (!mineList.contains(btn))
            {
                mineList.add(btn);
                tmp-=1;
            }

        }
    }
    public void revealMines()
    {
        for (int i=0;i<mineList.size();i++)
        {
            MineButton mb=mineList.get(i);
            mb.setText("\uD83D\uDCA3");
        }
        gameover=true;
        minelb.setText("GAMEOVER");
    }
}
