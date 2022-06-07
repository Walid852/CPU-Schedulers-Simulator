package CPU_Simulator;

import static java.lang.Math.ceil;
import java.util.Vector;

public class Scheduler {
    public Vector <Process> savedProcesses = new Vector <>(); //while moving process btwn objects its saved here
    Vector <Process> first_queue = new Vector <>();// FCFS for just
    Vector <Process> second_queue = new Vector <>(); //Non-Preemptive priority
    int time = 0;

    void setWhole() {
        int i=0;
        for (Process process : first_queue) {
            process.indexforall = 0;
            savedProcesses.add(process);
            i++;
        }
    }

    int MinArrival() {
        int min = 10000;

        // First Queue 
        int i = 0;
        int target = -1;
        for (Process process : first_queue) {
            if (process.getArrival_time() < min) {
                target = i;
                min = process.getArrival_time();
            }
            i++;
        }
        return target;
    }

    int MinPriority() {
        int min = 10000;

        // second_queue Queue 
        int i = 0;
        int goal = 0;
        for (Process process : second_queue) {
            if (process.getPriority() < min) {
                min = process.getPriority();
                goal = i;
            }
            i++;
        }

        return goal;
    }

    void TurnAround(){
        for (Process process : savedProcesses) {
            process.setTurnaround_Time(process.getTurnaround_Time()+process.waiting_time);
        }
    }
    void Statistics(){
        double wait =0;double turns = 0 ;
        for (Process process : savedProcesses) {
            wait += process.waiting_time;turns+=process.getTurnaround_Time();
            System.out.println(process.getName() +" : waiting time is "+process.waiting_time +", TurnAroundTime is "+process.getTurnaround_Time());
        }
        System.out.println("The Average Waiting Time is "+wait/savedProcesses.size());
        System.out.println("The Average Turnaround Time is "+turns/savedProcesses.size());
    }
    void Start() {
        //while True there's a Process in the first_queue branch
        //we'll try only now FCFS
        setWhole();
        Process selected;

        int index_target = 0;
        while (true) {
            boolean f = false;
            System.out.println("\n FirstQueue \n");
            while (first_queue.size() != 0) {
                index_target = MinArrival();

                if (first_queue.elementAt(index_target).getArrival_time() > time)
                    break; //it compares the min arrival time by currently time

                selected = first_queue.remove(index_target);//get the first_queue arrival process

                selected.waiting_time = (time - selected.getArrival_time()); //calculate the waiting time
                int passedTime = (int) ceil(0.25 * selected.getQuantum_time()); // how much take in the first_queue queue
                selected.setBurst_time(selected.getBurst_time() - passedTime); // reduce the burst time done
                selected.setQuantum_time(selected.getQuantum_time() + 2); //increase the quantum time
                //selected.setTurnaround_Time(selected.waiting_time+selected.getBurst_time());
                System.out.println("The start time is " + time);

                time += passedTime; //how much time has passed
                selected.stopped_time = time;
                if (selected.getBurst_time() > 0) { //if still has time to do add to the second_queue
                    second_queue.add(selected);
                } else {
                    selected.setQuantum_time(0); //all the burst time has finished
                }

                System.out.println(selected.getName() + " started, waited till now  " + selected.waiting_time);
                System.out.println("It has worked for only "+passedTime);
                System.out.println("New Quantum is " + selected.getQuantum_time());
                System.out.println();

                savedProcesses.set(selected.indexforall, selected);


                //during second_queue branch if found any in first_queue its time has come
                //we should check each loop if anyone has arrived at the first_queue one

            }
            System.out.println("\n SecondQueue \n");
            while (second_queue.size() != 0) {
                index_target = MinPriority();
                selected = second_queue.remove(index_target);//get the highest priority
                System.out.println("The start time is " + time);

                selected.waiting_time += (time - selected.stopped_time); //calculate the waiting time
                int passedTime = (int) ceil(0.25 * selected.getQuantum_time()); // how much take in the second_queue queue
                selected.setBurst_time(selected.getBurst_time() - passedTime); // reduce the burst time done
                selected.setQuantum_time(selected.getQuantum_time() + (int) ceil(selected.getQuantum_time() / 2.0)); //increase the quantum time

                time += passedTime; //how much time has passed
                selected.stopped_time = time;
               /* if (selected.getBurst_time() > 0) { //if still has time to do add to the second_queue
                    third.add(selected);
                } else {
                    selected.setQuantum_time(0); //all the burst time has finished
                }*/
                savedProcesses.set(selected.indexforall, selected);


                System.out.println(selected.getName() + "  started, waited till now  " + selected.waiting_time);
                System.out.println("It has worked for only "+passedTime);
                System.out.println("New Quantum is " + selected.getQuantum_time());
                System.out.println();

                //we here want to check if any have arrived to the first_queue
                index_target = MinArrival();
                if (index_target != -1) {
                    if (first_queue.elementAt(index_target).getArrival_time() <= time) {
                        f = true;
                        break;
                    }
                }
            }
            
            if (f) continue;

            break;
        }
        System.out.println("\n Finished \n");
        System.out.println();
        //TurnAround();
    }



/*
For each scheduler output the following:
	Processes execution order
	Waiting time for each process
	turnaround  time  for each process
	Average Waiting time        //for the whole scheduler
	Average turnaround  time    //for the whole scheduler brdo
	Print all history update of quantum time for each process (AG  Scheduling)

*/

}
