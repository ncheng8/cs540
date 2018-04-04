package successor;

import java.util.*;

public class successor {
    public static class JugState {
        int[] Capacity = new int[]{0,0,0};
        int[] Content = new int[]{0,0,0};
        public JugState(JugState copyFrom)
        {
            this.Capacity[0] = copyFrom.Capacity[0];
            this.Capacity[1] = copyFrom.Capacity[1];
            this.Capacity[2] = copyFrom.Capacity[2];
            this.Content[0] = copyFrom.Content[0];
            this.Content[1] = copyFrom.Content[1];
            this.Content[2] = copyFrom.Content[2];
        }
        public JugState()
        {
        }
        public JugState(int A,int B, int C)
        {
            this.Capacity[0] = A;
            this.Capacity[1] = B;
            this.Capacity[2] = C;
        }
        public JugState(int A,int B, int C, int a, int b, int c)
        {
            this.Capacity[0] = A;
            this.Capacity[1] = B;
            this.Capacity[2] = C;
            this.Content[0] = a;
            this.Content[1] = b;
            this.Content[2] = c;
        }
 
        public void printContent()
        {
            System.out.println(this.Content[0] + " " + this.Content[1] + " " + this.Content[2]);
        }
 
        public ArrayList<JugState> getNextStates(JugState initial){
            ArrayList<JugState> successors = new ArrayList<>();
            
            for (int i = 0; i < 3; i++) 
            {
            	if (initial.Capacity[i] > initial.Content[i]) 
            	{
            		JugState a = new JugState(initial);
            		a.Content[i] = a.Capacity[i];
            		successors.add(a);
            	}
            }
            for (int j = 0; j < 3; j++) {
            	if (initial.Content[j] > 0)
            	{
            		JugState a = new JugState(initial);
            		a.Content[j] = 0;
            		successors.add(a);
            		
            		
            	}
            }
            for (int k = 0; k < 3; k++) {
            	if(initial.Content[k] < initial.Capacity[k]){
            		for(int l = 0; l < 3; l++){
            			if(l != k && initial.Content[l] > 0){
            				JugState a = new JugState(initial);
            				
            				int emptySpace = a.Capacity[k] - a.Content[k];
            				if(emptySpace < a.Content[l]){
            					
            					a.Content[k] = a.Content[k] + emptySpace;
            					a.Content[l] = a.Content[l] - emptySpace;
            				}
            				else if(emptySpace == a.Content[l]){
            					a.Content[k] = a.Capacity[k];
            					a.Content[l] = 0;
            				}
            				else{
            					a.Content[k] = a.Content[k] + a.Content[l];
            					a.Content[l]= 0;
            				}
            				
            				successors.add(a);
            				}
            			}
            		}
            	
            	}
            

            return successors;
        }
    }

    public static void main(String[] args) {
       if( args.length != 6 )
        {
            System.out.println("Usage: java successor [A] [B] [C] [a] [b] [c]");
            return;
        }

        // parse command line arguments
        JugState a = new JugState();
        a.Capacity[0] = Integer.parseInt(args[0]);
        a.Capacity[1] = Integer.parseInt(args[1]);
        a.Capacity[2] = Integer.parseInt(args[2]);
        a.Content[0] = Integer.parseInt(args[3]);
        a.Content[1] = Integer.parseInt(args[4]);
        a.Content[2] = Integer.parseInt(args[5]);
     
        // Implement this function
        ArrayList<JugState> asist = a.getNextStates(a);

        // Print out generated successors
        for(int i=0;i< asist.size(); i++)
        {
            asist.get(i).printContent();
        }

        return;
    }
}
