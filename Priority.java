package CPU_Simulator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Queue;

public class Priority  {
    static CPU cpu=new CPU();
    private static int NumOfP=0;//num of priority
    static Process[] Processes ;
    static Queue<Process> readyQue = new LinkedList<Process>();
    static Queue<Process> completed = new LinkedList<Process>();
 public static void main(String[] args) {
        inputs();
        OrderFirst();
        int time=0;
        int limit=20;
        int q=5;
        //low number represent high priority
        while (completed.size()!=NumOfP)
        {
            Receive(time);
            //aging:
            for (Process p:readyQue) {
                if(p.starve%q==0&&p.starve!=0)
                {
                 p.priority--;
                }
            }
            OrderAgain();
            if(readyQue.size()!=0)
                if(readyQue.peek().priority<cpu.CurrentP.priority &&cpu.CurrentP.burst>0)
                {
                    InsertInPlace(cpu.CurrentP,readyQue);
                    cpu.CurrentP=readyQue.poll();
                    System.out.println(time+" "+cpu.CurrentP.name+"("+cpu.CurrentP.priority+")");
                }
            //first or after empty time
            if(cpu.CurrentP.burst<0)
            {
                if(readyQue.size()!=0)
                {
                    cpu.CurrentP=readyQue.poll();
                    System.out.println(time+" "+cpu.CurrentP.name+" ");
                }
            }

            //completed
            if(cpu.CurrentP.burst==0)
            {
                CompletedQue(cpu.CurrentP,time);
                if(readyQue.size()==0){cpu.CurrentP=new Process();
                    System.out.println(time);}
                else {cpu.CurrentP=readyQue.poll(); System.out.println(time+" "+cpu.CurrentP.name+" ");}
            }
            //running
            if(cpu.CurrentP.burst>0)
            {
                cpu.CurrentP.burst--;
                IncreasingWaitedTime();
                //starving:
                for (Process p:readyQue) {
                    if(p.waited>limit) p.starve++;
                }
            }
            time++;
        }
        for (Process p:completed) {
            System.out.println(p.name);
        }
        Statistic();
        
        //int x=0;
       //Scheduler class
       // while (x<NumOfP){
            Scheduler s = new Scheduler();
           // s.first_queue.add(new Process(Processes[x].name, Processes[x].burst, Processes[x].arrival, Processes[x].priority, Processes[x].getQuantum_time()));
            s.first_queue.add(new Process("p1", 4, 0, 1, 5));
            s.first_queue.add(new Process("p2", 3, 0, 3, 4));
            s.first_queue.add(new Process("p3", 8, 0, 7, 7));
            s.first_queue.add(new Process("p4", 5, 10, 8, 6));
            s.Start();
            s.Statistics();
           // x++;
        //}
    }
    static void CompletedQue(Process p,int T)
    {
        completed.offer(p);
        p.completion=T;
        p.setTurnaround();
    }
    static void Initialization()
    {
        Processes = new Process[NumOfP];
        for (int x = 0; x < NumOfP ; x++) {
            Processes[x]=new Process();
        }
    }
    static void OrderAgain()//reOrder
    {
        Process t;
        Queue<Process> temp = new LinkedList<Process>();
        int n=readyQue.size();
        for (int i = 0; i <n ; i++) {
            t=readyQue.poll();
            InsertInPlace(t,temp);
        }
        readyQue=temp;
    }
    static void Receive(int arrival_T)
    {
        for (int x = 0; x <NumOfP ; x++) {
            if(Processes[x].arrival==arrival_T)
            {
                InsertInPlace(Processes[x],readyQue);
            }
        }
    }

    private static void OrderFirst() {

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
    static void inputs()
    {
        Scanner scan=new Scanner(System.in);
        System.out.println("Enter number of Processes: ");
        NumOfP = scan.nextInt();
        Initialization();
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
        System.out.println("Enter processes Priority: ");
        for (int x = 0; x <NumOfP; x++) {
          Processes[x].priority = scan.nextInt();
       }
    }
    static void InsertInPlace(Process p,Queue<Process> Que)
    {
        if(Que.size()==0){Que.offer(p); return; }
        Process temp;
        boolean insertedP=false;
        int n=Que.size();

        for (int x=0;x<n;x++) {
            temp=Que.poll();
            if(p.priority<temp.priority&&!insertedP)
            {
                insertedP=true;
                Que.offer(p);
            }
            Que.offer(temp);
        }
        if(!insertedP) {
            Que.offer(p);
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
    static void Statistic()
    {
        for (Process p:completed) {
            System.out.print(p.name+" Waited for "+p.waited);
            System.out.println(" with turnaround time of "+p.turnaround);
        }
        System.out.println("Average Waiting Time: ");
        System.out.println(" ="+AverageWaitingT());
        System.out.println("Average Turnaround Time: ");
        System.out.println(" = "+AverageTurnaround());
    }

    static float AverageTurnaround()
    {
        float Sum=0;
        for (Process p:completed) {
            Sum+=p.turnaround;
        }
        return (float)Sum/NumOfP;
    }

    static void IncreasingWaitedTime()
    {
        int s=readyQue.size();
        for (Process p:readyQue) {
            p.waited++;
        }
    }
    

}