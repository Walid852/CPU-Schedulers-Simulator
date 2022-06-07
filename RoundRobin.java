package CPU_Simulator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Queue;

public class RoundRobin {

    static CPU cpu=new CPU();
    private static int NumOfP=0;
    private static int quantum=0;
    static Process[] Processes ;
    static Queue<Process> completed = new LinkedList<Process>();
    static Queue<Process> ReadyQue = new LinkedList<Process>();
    static int Context_Switch;
    public static void main(String[] args) {
        Inputs();
        OrderFirst();
        int Time=0;
        int Co_Sw=0;
        while (completed.size()!=NumOfP)
        {
            Receive(Time);

            //context switching
            if(cpu.Timer==0&&cpu.CurrentP.burst>0&&ReadyQue.size()!=0)
            {
                Co_Sw=Context_Switch;
                System.out.println(Time+" Context Switching ");
                while (Co_Sw!=0)
                {
                    Co_Sw--;
                    IncreasingWaitedTime();
                    Time++;
                    Receive(Time);
                }
            }

            //completion of a process
            if(cpu.CurrentP.burst==0)
            {
                CompletedQue(cpu.CurrentP,Time);
                cpu.Timer=0;
            }
            if(cpu.Timer==0)
            {
                System.out.print(" "+Time);
                if(cpu.CurrentP.burst>0) {
                    ReadyQue(cpu.CurrentP);
                }
                if(ReadyQue.size()!=0) {
                    System.out.print(" "+ReadyQue.peek().name+" "); //returns head without remove
                    cpu.Timer = quantum;
                    cpu.CurrentP(ReadyQue.poll()); //returns head and remove
                }
                else {
                    cpu.CurrentP=new Process();
                    Time++;
                    continue;
                }
            }
            cpu.CurrentP.burst--;
            cpu.Timer--;

            IncreasingWaitedTime();
            Time++;
        }
        System.out.println("");
        Statistic();
    }

    static void Statistic()
    {
        for (Process p:completed) {
            System.out.print(p.name+" Waited for "+p.waited);
            System.out.println(" with turnaround time of "+p.turnaround);
        }
        System.out.println("Average Waiting Time: ");
        System.out.println(" ="+AverageWaitingT());
        System.out.println("Average Turnaround Time: ");
        System.out.println(" ="+AverageTurnaround());
    }
    static float AverageTurnaround()
    {
        float sum=0;
        for (Process p:completed) {
            sum+=p.turnaround;
        }
        return (float)sum/NumOfP;
    }
    static void Receive(int arrival_T)
    {
        for (int i = 0; i <NumOfP ; i++) {
            if(Processes[i].arrival==arrival_T)
            {
                ReadyQue(Processes[i]);
            }
        }
    }
    static float AverageWaitingT()
    {
        float sum=0;
        for (Process p:completed) {
            sum+=p.waited;
        }
        return (float)sum/NumOfP;
    }
    
    static void CompletedQue(Process p,int T)
    {
        completed.offer(p);
        p.completion=T;
        p.setTurnaround();
    }
    static void IncreasingWaitedTime()
    {
        int n=ReadyQue.size();
        for (Process p:ReadyQue) {
            p.waited++;
        }
    }
    
    static void ReadyQue(Process p)
    {
        ReadyQue.offer(p);
    }
    private static void OrderFirst() 
    {
        for (int i = 0; i <NumOfP-1 ; i++) {
            for (int j = 0; j <NumOfP-1 ; j++) {
                if(Processes[j].arrival>Processes[j+1].arrival)
                    Swap(j,j+1);
            }
        }
    }
    static void Swap(int i, int j) {
        Process temp;
        temp=Processes[i];
        Processes[i]=Processes[j];
        Processes[j]=temp;
    }
 
    static void PrintProcesses(Process[] p)
    {
        int n=p.length;
        for (int i = 0; i <n ; i++) {

            System.out.println(p[i].name+" ");
        }
    }
    static void Initialization()
    {
        Processes = new Process[NumOfP];
        for (int i = 0; i < NumOfP ; i++) {
            Processes[i]=new Process();
        }
    }
    static void Inputs()
    {
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter number of Processes: ");
        NumOfP = scan.nextInt();
        System.out.println("Quantum Time: ");
        quantum= scan.nextInt();
        System.out.println("Context Switching: ");
        Context_Switch=scan.nextInt();
        Initialization();
        scan.nextLine();
        System.out.println("Enter processes names: ");
        for (int i = 0; i < NumOfP; i++) {
            Processes[i].name = scan.next();
        }
        System.out.println("Enter processes Arrival times: ");
        for (int i = 0; i < NumOfP; i++) {
            Processes[i].arrival = scan.nextInt();
        }
        System.out.println("Enter processes  Burst times: ");
        for (int i = 0; i < NumOfP; i++) {
            Processes[i].burst = scan.nextInt();
        }
        }
    }