import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.LinkedList;
import java.util.Random;



/**
 * ISSUES: 
nn * 		reset variables
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
	private int generations, altCountOld, altGroupCountOld, altAvgGroupSizeNew, egoAvgGroupSizeNew, altCountNew, altGroupCountNew, egoGroupCountOld, egoGroupCountNew, altAvgGroupSizeOld, egoAvgGroupSizeOld;
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
		this.fileString = "Generation Stats of Generation Zero\n"+
						  "Community Size: "+comSize+"\n"+
						  "Cost of Altruism: "+cost+	
						  "\n\nAltruists\n"+
						  "Starting Altruists: "+altNum+"\n"+
						  "Avg. Group Size: "+avgAltSize+"\n\nEgoists\n"+
						  "Starting Egoists: "+(comSize-altNum)+"\n"+
						  "Avg. Group Size: "+((comSize-altNum)/((altNum/avgAltSize)==0?1:(altNum/avgAltSize)))+"\n\n";		  
		this.comSize = comSize;
		this.cost = cost;
		this.searchSize = searchSize;
		this.generations = generations;
		comPersonalityHistory = new String[generations];
		community = new LinkedList<Agent>();
		generateCommunity(altNum,avgAltSize);
	}
	
	/**
	 * Runs multiple generations (using oneGeneration()) as specified by user
	 */
	public void runEpoch(){
		curGeneration = altCountOld = altGroupCountOld = egoGroupCountOld = altAvgGroupSizeOld = egoAvgGroupSizeOld = 0; //to keep track of variables for each generation
		comPersonalityHistory = new String[generations];
		altCountOld = altCountNew;
		altGroupCountOld = altGroupCountNew;
		egoGroupCountOld = altGroupCountNew;
		while (curGeneration!=generations){
			System.out.println("");
			fileString+="\n\n";
			oneGeneration();
			calcAndPrint();
			for (Agent a : community){
				fileString+=(a.getPersonality()==2?"a":"E");
			}
			curGeneration++;
		}
		try{
			// Create file 
			FileWriter fstream = new FileWriter("media/results.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write(fileString);
			//Close the output stream
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		//System.out.println(fileString);
	}
	
	/**
	 * Runs one generation through value assignment and personality switching
	 */
	public void oneGeneration(){
		//assign all values
		for (int i=0;i<community.size();i++){  
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
	}
	
	/**
	 * Switches the current generation to their desired personality for the next gen, then calculates all stat tracking and adds to print string
	 */
	public void calcAndPrint(){
		//set new stats to 0
		altCountNew = altGroupCountNew = egoGroupCountNew = altAvgGroupSizeNew = egoAvgGroupSizeNew = 0;
		//how many of each type is in the population?
		//how many groups of each are there?
		comPersonalityHistory[curGeneration]="";
		int prevPers = community.get(comSize-1).getPersonality(); //for comparison
		for (Agent a: community){  //for each member
			if (curGeneration!=0){
				a.setPersonality(a.getTempPersonality());  //set their personality to the decision they made
			}
			comPersonalityHistory[curGeneration]+=a.getPersonality()==2?"a":"E";
			if (a.getPersonality()==2){  //if we find an altruist, we up the altCountNew
				altCountNew++;
				//System.out.println("altCountNew: "+altCountNew);
			}
			if (prevPers!=a.getPersonality()){  //if the previous dude has an opposite personality as the current dude
				if (a.getPersonality()==2){  //if the personality of the current dude is altruist
					altGroupCountNew++;      //add to the group count of altruists
				}
				else{
					egoGroupCountNew++;      //if not, we add to the group count of egoist
				}
			}
			prevPers = a.getPersonality();
		}
		altGroupCountNew = egoGroupCountNew = Math.min(altGroupCountNew,egoGroupCountNew); //corrects odd error
		if (altGroupCountNew==0 || egoGroupCountNew==0){ //if there is only one group
			if (community.get(0).getPersonality() == 2){ //if the group contains an altruist, it is all altruist
				altGroupCountNew++; //so add to the altruist group count
			}
			else{
				egoGroupCountNew++;  //otherwise, we add to the ego group count
			}
		}
		altAvgGroupSizeNew = altCountNew/(altGroupCountNew==0?1:altGroupCountNew);  //get average group size for alts
		egoAvgGroupSizeNew = (comSize-altCountNew)/(egoGroupCountNew==0?1:egoGroupCountNew); //get average group size for egos
		//what were the changes from last time?
		if (curGeneration!=0){
			altNumChange = altCountNew - altCountOld;
			altGroupChange = altGroupCountNew - altGroupCountOld;
			altAvgGroupChange = altAvgGroupSizeNew - altAvgGroupSizeOld;
			egoNumChange = (comSize-altCountNew) - (comSize-altCountOld);
			egoGroupChange = egoGroupCountNew - egoGroupCountOld;
			egoAvgGroupChange = egoAvgGroupSizeNew - egoAvgGroupSizeOld;
		}
		//add the stats to the filestring
	    fileString+="Generation "+curGeneration+":\n";
		fileString+="\nAltruists\n"+
				"Individuals: "+altCountNew+"          Change: "+altNumChange+"\n"+
				"Groups: "+altGroupCountNew+"                Change: "+altGroupChange+"\n"+
				"Average Group Size: "+altAvgGroupSizeNew+"     Change: "+(altAvgGroupChange)+"\n\nEgoists\n"+
				"Individuals: "+(comSize-altCountNew)+"           Change: "+(egoNumChange)+"\n"+
				"Groups: "+altGroupCountNew+"                 Change: "+(egoGroupChange)+"\n"+
				"Average Group Size: "+egoAvgGroupSizeNew+"    Change: "+(egoAvgGroupChange)+"\n\n";
		//set new stats to old
		altCountOld = altCountNew;
		altGroupCountOld = altGroupCountNew;
		altAvgGroupSizeOld = altAvgGroupSizeNew;
		egoGroupCountOld = egoGroupCountNew;
		egoAvgGroupSizeOld = egoAvgGroupSizeNew;
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
		if (comSize<=3){
			for (int i=comSize; i!=0; i--){
				community.add(new Agent(rand.nextInt(1)+1));
			}
			return;
		}
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
				try{
					tempAdd+= (avgEgoSize==1?0:rand.nextInt(2*maxEgoChange) - maxEgoChange); //add or subtract a random value less than maxSize
				}
				catch (Exception e){
					tempAdd = 1;
				}
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
	    if (gen>generations-1){
		gen=generations-1;
	    }
	    else if (gen<0){
		gen=0;
	    }
	    curGeneration = gen;
	    return comPersonalityHistory[curGeneration];
	}
    
    public int getCurGeneration() {
	return curGeneration;
    }

    public int getNumGenerations() {
	return generations;
    }

    public String getFileString() {
	return fileString;
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
		//int comSize, double cost, int altNum, int avgAltSize, int generations, int searchSize
		test.gridInitialize(10,.4,8,1,5,1);
		test.runEpoch();
	}
}