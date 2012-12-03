import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Random;



/**
 * ISSUES: 
 * 		reset variables
 * 
 * @author ClintFrank
 *
 */
public class Circle {
	private LinkedList<Agent> community; //circular linked list used for return if normType is true //list that stores current adjacents
	private int comSize, searchSize; //size of the 2D array//size of searching radius. ex. 1 means one individual on each side, 2 means two on each side etc.
	private double cost; //cost of altruism
	private String fileString;
	//history for user to look through
	private int generations, altCountOld, altGroupCountOld, altCountNew, altGroupCountNew, egoGroupCountOld, egoGroupCountNew;
	private int altNumChange, altGroupChange, altAvgGroupChange, egoNumChange, egoGroupChange, egoAvgGroupChange;
	private int curGeneration;
	private String[] comPersonalityHistory;
	
	/**
	 * Called before each run to initialize grid variables, size, type, population data
	 * 
	 * @param comSize
	 * @param cost
	 * @param altNum
	 * @param avgAltSize
	 */
	public void gridInitialize(int comSize, double cost, int altNum, int avgAltSize, int generations, int searchSize){
		this.fileString = "Community Size: "+comSize+"\n"+
						  "Cost of Altruism: "+cost+	
						  "\n\nAltruists\n"+
						  "Starting Altruists: "+altNum+"\n"+
						  "Average Group Size: "+avgAltSize+"\n\nEgoists\n"+
						  "Starting Egoists: "+(comSize-altNum)+"\n"+
						  "Average Group Size: "+((comSize-altNum)/(altNum/avgAltSize))+"\n\n";		  
		this.comSize = comSize;
		this.cost = cost;
		this.searchSize = searchSize;
		altCountNew = altNum;
		altGroupCountNew = altNum/avgAltSize;
		egoGroupCountNew = (comSize-altNum)/((comSize-altNum)/(altNum/avgAltSize));
		this.generations = generations;
		comPersonalityHistory = new String[generations];
		community = new LinkedList<Agent>();
		generateCommunity(altNum,avgAltSize);
	}
	
	/**
	 * Runs multiple generations (using oneGeneration()) as specified by user
	 */
	public void runEpoch(){
		curGeneration = altCountOld = altGroupCountOld = egoGroupCountOld = 0; //to keep track of variables for each generation
		comPersonalityHistory = new String[generations];
		altCountOld = altCountNew;
		altGroupCountOld = altGroupCountNew;
		egoGroupCountOld = altGroupCountNew;
		
		while (curGeneration!=generations){
			fileString+="Generation "+curGeneration+":\n";
			altNumChange = altCountNew-altCountOld;
			altGroupChange = altGroupCountNew-altGroupCountOld;
			altAvgGroupChange = (altGroupCountNew==0?altGroupCountOld:(altCountNew/altGroupCountNew)-(altCountOld/altGroupCountOld));
			egoNumChange = (comSize-altCountNew)-(comSize-altCountOld);
			egoGroupChange = egoGroupCountNew-egoGroupCountOld;
			egoAvgGroupChange = (egoGroupCountNew==0?egoGroupCountOld:(comSize-altCountNew)/(egoGroupCountNew)-((comSize-altCountOld)/(egoGroupCountOld)));
			fileString+="\nAltruists\n"+
					"Individuals: "+altCountNew+"          Change: "+altNumChange+"\n"+
					"Groups: "+altGroupCountNew+"                Change: "+altGroupChange+"\n"+
					"Average Group Size: "+(altCountNew/(altGroupCountNew==0?1:altGroupCountNew))+"     Change: "+(altAvgGroupChange)+"\n\nEgoists\n"+
					"Individuals: "+(comSize-altCountNew)+"           Change: "+(egoNumChange)+"\n"+
					"Groups: "+altGroupCountNew+"                 Change: "+(egoGroupChange)+"\n"+
					"Average Group Size: "+((comSize-altCountNew)/(egoGroupCountNew==0?1:egoGroupCountNew)+"    Change: "+(egoAvgGroupChange)+"\n\n");
			egoAvgGroupChange = egoGroupChange = egoNumChange = altAvgGroupChange=altGroupChange = altNumChange=0; //reset change variables
			altCountOld = altCountNew; 
			altGroupCountOld = altGroupCountNew;
			egoGroupCountOld = egoGroupCountNew;
			altCountNew = altGroupCountNew = egoGroupCountNew = 0;
			for (Agent a : community){
				System.out.print(a.getPersonality());
				fileString+=(a.getPersonality()==2?"a":"E");
			}
			fileString+="\n\n";
			oneGeneration();
			curGeneration++;
		}
		try{
			// Create file 
			FileWriter fstream = new FileWriter("results.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(fileString);
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		System.out.println(fileString);
	}
	
	/**
	 * Runs one generation through value assignment and personality switching
	 */
	public void oneGeneration(){
		//assign all values
		for (int i=0;i<community.size();i++){  
			int tempPersonality = community.get(i).getPersonality(); //get out current personality
			comPersonalityHistory[curGeneration]+=tempPersonality==2?"a":"E"; //records current strategy for history
			community.get(i).setCurPayoff(getPayoff(i));  //gets payoff and sets value for current dude.
		}
		//Calculate changes
		double altVal, egoVal, altNum, egoNum; //set necessary variables for average findings
		for (int i=0;i<community.size();i++){
			altVal = egoVal = altNum = egoNum = 0;
			for (int j=(i-searchSize);j<=(i+searchSize);j++){
				//System.out.println(j+"("+ind(j)+"): "+community.get(ind(j)).getPersonality());
				if (community.get(ind(j)).getPersonality()==2){ //if the current individual is an altruist
					altVal+=community.get(ind(j)).getCurPayoff();  //add payoff
					altNum++;
				}
				else {  //if the current individual is an egoist 
					egoVal+=community.get(ind(j)).getCurPayoff();
					egoNum++;
				}
			}
//			System.out.println(curGeneration+"-"+i+" P|V: "+community.get(i).getPersonality()+"|"+community.get(i).getCurPayoff());
//			System.out.println("From: "+(ind(i-searchSize))+" To: "+(ind(i+searchSize)));
//			System.out.println("altNum: "+altNum);
//			System.out.println("altVal: "+altVal);
//			System.out.println("egoNum: "+egoNum);
//			System.out.println("egoVal: "+egoVal);
//			System.out.println("altVal/altNum: "+altVal/altNum);
//			System.out.println("egoVal/egoNum: "+egoVal/egoNum);
			if (altNum==0){  //if no altruists
				//System.out.println("Egoist\n");
				community.get(i).setTempPersonality(1); //set to egoist
			}
			else if (egoNum==0){ //if no egoists
				//System.out.println("Altruist\n");
				community.get(i).setTempPersonality(2); //set to altruist
			}
			else{
				//System.out.println((altVal/altNum>egoVal/egoNum?"Altruist\n":"Egoist\n"));
				community.get(i).setTempPersonality((altVal/altNum>egoVal/egoNum?2:1));
			}
		}
		int prevPers = community.get(comSize-1).getPersonality(); //for comparison
		for (Agent a: community){
			a.setPersonality(a.getTempPersonality());
			if (a.getPersonality()==2){
				altCountNew++;
			}
			if (prevPers!=a.getPersonality()){
				if (a.getPersonality()==2){
					altGroupCountNew++;
				}
				else{
					egoGroupCountNew++;
				}
			}
			if (altGroupCountNew==0){
				if (community.get(0).getPersonality()==2){
					altGroupCountNew++;
				}
				else{
					egoGroupCountNew++;
				}
			}
			prevPers = a.getPersonality();
		}
	}
	
	public int ind(int i){
		if (i<0){
			return comSize+i;
		}
		else if (i>=comSize){
			return i%comSize;
		}
		return i;
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
		int avgEgoSize = egoNum/(numEgoGroups==0?1:numEgoGroups); //average size of Egoist groups
		int maxAltChange = (int) (avgAltSize/2); //maximum deviation from averageAltSize
		int maxEgoChange = (int) (avgEgoSize/2);  //maximum deviation from averageEgoSize
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
	 * Returns the community
	 * @return
	 */
	public LinkedList<Agent> getCommunity(){
		return community;
	}
	
	/**
	 * returns a generation of personalities
	 * 
	 * @param gen
	 * @return
	 */
	public String getGenerationPersonalities(int gen){
		if (gen>generations-1 || gen<0){
			return null;
		}
		return comPersonalityHistory[gen];
	}
	
	/**
	 * Gets adjacent individuals and gives back their value-2 for actual value and -.5 if you are an altruist
	 * 
	 * Altruists = 2
	 * Ego = 1
	 * 
	 * EXE = 2
	 * EXA or AXE = 3
	 * AXA = 4
	 * 
	 * If you subtract 2 from this value, you get the actual appraisal. 
	 * 
	 * @param pos
	 * @return
	 */
	public double getPayoff(int pos){
		return (community.get(ind(pos-1)).getPersonality() + community.get(ind(pos+1)).getPersonality())-2 + (community.get(pos).getPersonality()==2? -cost : 0);
	}
	
	public static void main(String[] args){
		Circle test = new Circle();
		test.gridInitialize(2000,.5,100,20,10,1);
		test.runEpoch();
	}
}