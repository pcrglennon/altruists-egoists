
public class Agent {
	private int personality, tempPersonality; //0-none,1-ego,2-altruist
	private double curPayoff; //payoff of Agent this generation

	public Agent(int personality){
		this.personality=personality;
	}
	
	/**
	 * @return the personality type //0-none,1-ego,2-altruist
	 */
	public int getPersonality() {
		return personality;
	}

	/**
	 * @param personality the personality type to set //0-none,1-ego,2-altruist
	 */
	public void setPersonality(int personality) {
		this.personality = personality;
	}

	/**
	 * @return the curPayoff
	 */
	public double getCurPayoff() {
		return curPayoff;
	}

	/**
	 * @param the curPayoff to set
	 */
	public void setCurPayoff(double curVal) {
		this.curPayoff = curVal;
	}

	/**
	 * @return the tempPersonality
	 */
	public int getTempPersonality() {
		return tempPersonality;
	}

	/**
	 * @param tempPersonality the tempPersonality to set
	 */
	public void setTempPersonality(int tempPersonality) {
		this.tempPersonality = tempPersonality;
	}
}
