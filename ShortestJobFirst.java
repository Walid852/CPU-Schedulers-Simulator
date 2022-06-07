package CPU_Simulator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
public class ShortestJobFirst {
    
   static CPU cpu=new CPU();

    private static int NumOfP=0;
    static Process[] Processes ;
    static Queue<Process> readyQue = new LinkedList<Process>();
    static Queue<Process> completed = new LinkedList<Process>();
    static int ContextSwitching;
    public static void main(String[] args) {
        Inputs();
        OrderFirst();
        int T=0;
        int coSw=0;
        while (completed.size()!=NumOfP)
        {
            Receive(T);
            if(readyQue.size()!=0)
            if(readyQue.peek().burst<cpu.CurrentP.burst && cpu.CurrentP.burst>0)
            {
                coSw=ContextSwitching;
                while (coSw!=0)
                {
                    coSw--;
                    IncreasingWaitedTime();
                    T++;
                    Receive(T);
                }
                InsertInPlace(cpu.CurrentP);
                cpu.CurrentP=readyQue.poll();
            }
            if(cpu.CurrentP.burst<0)
            {
                if(readyQue.size()!=0)
                {
                    cpu.CurrentP=readyQue.poll();
                }
            }
            if(cpu.CurrentP.burst==0)
            {
                CompletedQue(cpu.CurrentP,T);
                if(readyQue.size()==0)cpu.CurrentP=new Process();
                else cpu.CurrentP=readyQue.poll();
            }
            if(cpu.CurrentP.burst>0)
            {
                cpu.CurrentP.burst--;
                IncreasingWaitedTime();
            }

            T++;
        }
        for (Process p:completed) {
            System.out.println(p.name);
        }

        Statistic();

    }
    static void Inputs()
    {
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter number of Processes: ");
        NumOfP = scan.nextInt();
        Initialization();
        System.out.println("Context Switching: ");
        ContextSwitching=scan.nextInt();
        scan.nextLine();
        System.out.println("Enter processes names: ");
        for (int x = 0; x < NumOfP; x++) {
            Processes[x].name = scan.next();
        }
        System.out.println("Enter processes Arrival times: ");
        for (int x = 0; x < NumOfP; x++) {
            Processes[x].arrival = scan.nextInt();
        }
        System.out.println("Enter processes  Burst times: ");
        for (int x = 0; x < NumOfP; x++) {
            Processes[x].burst = scan.nextInt();
        }
    }
    static void Receive(int arrival_T)
    {
        for (int x = 0; x <NumOfP ; x++) {
            if(Processes[x].arrival==arrival_T)
            {
                InsertInPlace(Processes[x]);
            }
        }
    }
    private static void OrderFirst()
    {
        for (int x = 0; x <NumOfP-1 ; x++) {
            for (int i = 0; i <NumOfP-1 ; i++) {

                if(Processes[i].arrival>Processes[i+1].arrival)
                {
                    Process temp=Processes[i];
                    Processes[i]=Processes[i+1];
                    Processes[i+1]=temp;
                }
            }
        }
    }
    static void CompletedQue(Process p,int T)
    {
        completed.offer(p);
        p.completion=T;
        p.setTurnaround();
    }
    
    static void InsertInPlace(Process p)
    {
        if(readyQue.size()==0){readyQue.offer(p); return;
        }

        Process temp;
        boolean inserted=false;
        int n=readyQue.size();
        for (int x=0;x<n;x++) {
            temp=readyQue.poll();
            if(p.burst<temp.burst&&!inserted)
            {
                inserted=true;
                readyQue.offer(p);
            }
            readyQue.offer(temp);
        }
        if(!inserted) {
            readyQue.offer(p);
        }
    }
    
    
    static void Initialization()
    {
        Processes = new Process[NumOfP];
        for (int x = 0; x < NumOfP ; x++) {
            Processes[x]=new Process();
        }
    }
    static void Statistic()
    {
        for (Process p:completed) {
            System.out.print(p.name+" Waited for "+p.waited);
            System.out.println(" with turnaround time of "+p.turnaround);
        }
        System.out.println("Average Waiting Time: ");
        System.out.println(" ="+AverageWaitingTime());
        System.out.println("Average Turnaround Time: ");
        System.out.println(" ="+AverageTurnaround());
    }
    static void IncreasingWaitedTime()
    {
        int n=readyQue.size();
        for (Process p:readyQue) {
            p.waited++;
        }
    }
    static float AverageTurnaround()
    {
        float sum=0;
        for (Process p:completed) {
            sum+=p.turnaround;
        }
        return (float)sum/NumOfP;
    }
    static float AverageWaitingTime()
    {
        float sum=0;
        for (Process p:completed) {
            sum+=p.waited;
        }
        return (float)sum/NumOfP;
    }
}
