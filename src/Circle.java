import java.util.LinkedList;
import java.util.Random;

public class Circle {
	private LinkedList<Agent> community, tempAdjacents;; //circular linked list used for return if normType is true //list that stores current adjacents
	private int comSize; //size of the 2D array//cost of being Altruistic
	private double cost; //cost of altruism
	
	//history for user to look through
	private int generations;
	private int curGeneration;
	private int[][] comPersonalityHistory;
	private double[][] comPayoffHistory;
	
	/**
	 * Called before each run to initialize grid variables, size, type, population data
	 * 
	 * @param comSize
	 * @param cost
	 * @param altNum
	 * @param avgAltSize
	 */
	public void gridInitialize(int comSize, double cost, int altNum, int avgAltSize, int generations){
		this.comSize = comSize;
		this.cost = cost;
		curGeneration = 0;
		this.generations = generations;
		comPersonalityHistory = new int[generations][comSize];
		comPayoffHistory = new double[generations][comSize];
		community = new LinkedList<Agent>();
		generateCommunity(altNum,avgAltSize);
	}
	
	public void andHereWeGo(){
		while (curGeneration!=generations){
			for (Agent a : community){
				System.out.print(a.getPersonality());
			}
			System.out.println();
			oneGeneration();
			curGeneration++;
		}
		
	}
	
	/**
	 * Runs one generation of this shiiiit
	 */
	public void oneGeneration(){
		int[] tempAdj = new int[2]; //our temp array that holds adjacent individuals
		boolean planFlag = false;  //true when changing plans 
		int i=0;
		while (true){ //for each individual in the community
			//Setting the value for each Agent
			tempAdj = getAdjacent(i); //get the adjacent neighbors
			double tempVal = 0;          //Initialize temporary variables
			int tempPersonality = community.get(i).getPersonality();
			comPersonalityHistory[curGeneration][i] = tempPersonality; //records current strategy for history
			for (int neighbor:tempAdj){  //add value for each neighbor
				tempVal+=interactValue(tempPersonality,neighbor); //add the value for current neighbor
			}
			community.get(i).setCurPayoff(tempPersonality==2 ? tempVal-cost:tempVal); //value is set
			comPayoffHistory[curGeneration][i]=(tempPersonality==2 ? tempVal-cost:tempVal); //saved for history
			//System.out.println("Indiv "+i+" Person|Val: "+community.get(i).getPersonality()+"|"+community.get(i).getCurPayoff());
			//Planning
			if (planFlag || i>=2){
				double[] values = {community.get(ind(i-1)).getCurPayoff(),community.get(ind(i-2)).getCurPayoff(),community.get(i).getCurPayoff()};   //1/2 parallel arrays: for values
				int[] personalities = {community.get(ind(i-1)).getPersonality(),community.get(ind(i-2)).getPersonality(),community.get(i).getPersonality()}; //2/2 parallel arrays: for personalities
				double maxVal = values[0];
				int bestPersonality = personalities[0];
				for (int j=1;j<values.length;j++){
					if (maxVal<values[j]){ //if a new max is found
						maxVal=values[j]; //it is set in value
						bestPersonality=personalities[j]; //then set with personality
					}
				}
				community.get(ind(i-1)).setPersonality(bestPersonality);
			}
			i++;
			if(i==comSize){
				planFlag = true;
				i=0;
			}
			if (planFlag && i>=2){
				break;
			}
		}
	}
	
	public int ind(int i){
		if (i<0){
			return comSize+i;
		}
		else if (i>comSize){
			return i%comSize;
		}
		return i;
	}
	
	/**
	 * given two individuals, this function returns self's payoff NOT INCLUDING ALTRUIST COST
	 * 
	 * @param self
	 * @param partner
	 * @return
	 */
	public double interactValue(int self, int partner){
		return (partner==1? 0:1);
	}
	
	/**
	 * This function generates a community based on 3 variables: 
	 * 
	 * altNum: how many altruistic Agents you want
	 * egoNum: how many egoist Agents you want
	 * avgAltSize: how big each group of altruists should be on average
	 * 
	 * From these numbers we derive the following in function:
	 * numAltGroups: the number of altruistic groups we will have
	 * numEgoGroups: the number of egoist groups we will have
	 * maxAltChange: the maximum deviation from the average tolerable for altruist groups
	 * maxEgoChange: the maximum deviation from the average tolerable for egoist groups
	 * 
	 * 30 total, 20 altNum, 10 egoNum, avgAltSize 5
	 * 20/5 = 4 groups 
	 * 10/4 = 2 average Ego Group Size
	 * 
	 * 
	 * @param altNum
	 * @param egoNum
	 * @param avgAltSize
	 */
	public void generateCommunity(int altNum, int avgAltSize){
		Random rand = new Random();
		int egoNum = comSize - altNum;
		int numAltGroups = altNum/avgAltSize; //using the average size of Altruistic groups, we get the number of groups we will need
		int numEgoGroups = numAltGroups;  //this number of groups is the same as the Egoist number of groups we need
		int avgEgoSize = egoNum/numEgoGroups; //average size of Egoist groups
		int maxAltChange = (int) (avgAltSize/1.5); //maximum deviation from averageAltSize
		int maxEgoChange = (int) (avgEgoSize/1.5);  //maximum deviation from averageEgoSize
		int tempAdd;
		while (altNum>0 || egoNum>0){ //while loop that adds groups to 
			//ALTRUIST ADDING
			if (altNum>0){
				tempAdd = avgAltSize; //set tempAdd to our average group size
				tempAdd+= (avgAltSize==1? 0:rand.nextInt(2*maxAltChange) - maxAltChange); //add or subtract a random value less than maxSize
				if (altNum<tempAdd){
					tempAdd = altNum;
				}
				while (tempAdd>0){
					community.add(new Agent(2));
					altNum--; //subtract the used altruistic agents
					tempAdd--;
				}
			}
			//EGOIST ADDING
			if (egoNum>0){
				tempAdd = avgEgoSize; //set tempAdd to our average group size
				tempAdd+= (avgEgoSize==1?0:rand.nextInt(2*maxEgoChange) - maxEgoChange); //add or subtract a random value less than maxSize
				if (egoNum<tempAdd){
					tempAdd = egoNum;
				}
				while (tempAdd>0){
					community.add(new Agent(1));
					egoNum--; //subtract the used Egoist agents
					tempAdd--;
				} 
			}
		}
	}
	
	/**
	 * returns a generation of personalities
	 * 
	 * @param gen
	 * @return
	 */
	public int[] getGenerationPersonalities(int gen){
		if (gen>generations-1 || gen<0){
			return null;
		}
		return comPersonalityHistory[gen];
	}
	
	/**
	 * returns a generation of payoffs
	 * 
	 * @param gen
	 * @return
	 */
	public double[] getGenerationPayoffs(int gen){
		if (gen>generations-1 || gen<0){
			return null;
		}
		return comPayoffHistory[gen];
	}
	
//	public String getGenerationData(int gen){
//		String genData = "";
//		
//	}
	
	/**
	 * gets the forward and backward adjacent players to inputed individual
	 * 
	 * @param row
	 * @return
	 */
	public int[] getAdjacent(int row){
		int[] tempAdj = new int[2];
		tempAdj[0] = community.get((row==0?comSize-1:row)).getPersonality();  //try to get the previous element
		tempAdj[1] = community.get((row+1)%comSize).getPersonality();  //try to get the next element
		return tempAdj;
	}
	
	public static void main(String[] args){
		Circle test = new Circle();
		test.gridInitialize(200,.6,30,2,20);
		test.andHereWeGo();
	}
}