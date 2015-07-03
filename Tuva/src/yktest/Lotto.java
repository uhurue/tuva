package yktest;

public class Lotto
{

    public static void main(String[] args)
    {
        int i=0;
        int j=0;
        int num=0, order=0;
        int [] lots = new int [6];
        while(i<6)
        {
            order = (int)(Math.random()*100);
            while(j++<order)
            {
                num = (int)(Math.random()*45);
            }
            System.out.println("order=" + order + ", num=" + num);
            lots[i++]=num;
        }
        for(i=0; i<lots.length; i++)
        {
            System.out.println("lots[i]=" + lots[i]);
        }
    }
}
